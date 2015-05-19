package no.javazone;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import no.javazone.api.foredrag.SessionResource;
import no.javazone.ems.EmsAdapter;
import no.javazone.sessions.SessionRepository;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class HelloWorldApplication extends Application<HelloWorldConfiguration> {

    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            args = new String[]{"server", "configuration.yaml"};
        }
        new HelloWorldApplication().run(args);
    }

    @Override
    public String getName() {
        return "hello-world";
    }

    @Override
    public void initialize(Bootstrap<HelloWorldConfiguration> bootstrap) {
        // nothing to do yet
    }

    @Override
    public void run(
            HelloWorldConfiguration configuration,
            Environment environment
    ) {
        final HelloWorldResource resource = new HelloWorldResource();
        environment.jersey().register(resource);

        final EmsAdapter emsAdapter = new EmsAdapter(configuration.getEmsHost());

        final SessionRepository sessionRepository = new SessionRepository(emsAdapter);

        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(
                () -> sessionRepository.refresh(), 0, 10, TimeUnit.MINUTES);

        final SessionResource sessionResource = new SessionResource(sessionRepository);
        environment.jersey().register(sessionResource);
    }

}