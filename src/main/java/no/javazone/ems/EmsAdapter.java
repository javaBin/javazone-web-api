package no.javazone.ems;

import net.hamnaberg.json.Collection;
import net.hamnaberg.json.Item;
import net.hamnaberg.json.Link;
import net.hamnaberg.json.parser.CollectionParser;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class EmsAdapter {
    private final WebTarget emsWebTarget;

    public EmsAdapter(String emsHost) {
        Client client = ClientBuilder.newClient();
        emsWebTarget = client.target("http://" + emsHost);
    }

    public List<Session> getSessions(String eventId) {
        WebTarget webTarget = emsWebTarget
                .path("/ems/server/events")
                .path(eventId)
                .path("sessions");

        String response = webTarget.request().buildGet().invoke(String.class);

        try {
            Collection collection = new CollectionParser().parse(response);
            return mapToForedragsliste(collection);

        } catch (IOException e) {
            return Collections.emptyList();
        }
    }

    private List<Session> mapToForedragsliste(Collection collection) throws IOException {
        return collection
                .getItems()
                .stream()
                .map(EmsAdapter::mapItemTilForedrag)
                .collect(Collectors.toList());
    }

    private static Session mapItemTilForedrag(Item item) {
        return new Session(
                mapItemProperty(item, "title"),
                getForedragsholdere(item.linkByRel("speaker collection")));
    }

    private static List<Foredragsholder> getForedragsholdere(Optional<Link> link) {
        if (link.isPresent()) {
            WebTarget webTarget = ClientBuilder.newClient()
                    .target(link.get().getHref());
            String response = webTarget.request().buildGet().invoke(String.class);

            try {
                Collection collection = new CollectionParser().parse(response);
                return collection
                        .getItems()
                        .stream()
                        .map(EmsAdapter::mapItemTilForedragsholder)
                        .collect(Collectors.toList());
            } catch (IOException e) {
                throw new RuntimeException("Finner ikke speakers");
            }
        } else {
            throw new RuntimeException("Speakerlink finnes ikke");
        }
    }

    private static Foredragsholder mapItemTilForedragsholder(Item item) {
        return new Foredragsholder(
                mapItemProperty(item, "name"),
                mapItemProperty(item, "bio"));
    }

    private static String mapItemProperty(Item item, String property) {
        return item.propertyByName(property).get().getValue().get().asString();
    }
}