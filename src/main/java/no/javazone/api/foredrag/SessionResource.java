package no.javazone.api.foredrag;

import no.javazone.ems.Event;
import no.javazone.ems.Foredragsholder;
import no.javazone.ems.Session;
import no.javazone.sessions.SessionRepository;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Path("/event/{eventId}/sessions")
@Produces(MediaType.APPLICATION_JSON)
public class SessionResource {

    private SessionRepository sessionRepository;

    public SessionResource(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @GET
    public Response getForedrag(@PathParam("eventId") String eventSlug) {
        Optional<Event> eventOptional = sessionRepository.getSessions(eventSlug);
        if (eventOptional.isPresent()) {
            System.out.println("is present");
            List<SessionDTO> response = eventOptional.get()
                    .getSessions()
                    .stream()
                    .map(foredrag -> new SessionDTO(
                            foredrag.getTittel(),
                            toDTO(foredrag.getForedragsholdere())))
                    .collect(Collectors.toList());
            return Response.ok().entity(response).build();

        } else {
            System.out.println("nope");
            return Response.status(503).build();
        }
    }

    private List<ForedragsholderDTO> toDTO(List<Foredragsholder> foredragsholdere) {
        return foredragsholdere
                .stream()
                .map(foredragsholder -> new ForedragsholderDTO(
                        foredragsholder.getNavn(), foredragsholder.getGravatarUrl()))
                .collect(Collectors.toList());
    }
}
