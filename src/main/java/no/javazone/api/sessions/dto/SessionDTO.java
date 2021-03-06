package no.javazone.api.sessions.dto;

import no.javazone.api.links.LinkDTO;

import java.util.ArrayList;
import java.util.List;

public class SessionDTO {
    public final String tittel;
    public final String format;
    public final String starter;
    public final String stopper;
    public final List<ForedragsholderDTO> foredragsholdere;
    public final String sprak;
    public final String niva;
    public final List<LinkDTO> links;
    public final String rom;
    public final List<String> nokkelord;

    public SessionDTO(
            String tittel,
            String format,
            String starter,
            String stopper,
            List<ForedragsholderDTO> foredragsholdere,
            String sprak,
            String niva,
            List<LinkDTO> links,
            String rom,
            List<String> nokkelord
    ) {
        this.tittel = tittel;
        this.format = format;
        this.starter = starter;
        this.stopper = stopper;
        this.foredragsholdere = foredragsholdere;
        this.sprak = sprak;
        this.niva = niva;
        this.links = links;
        this.rom = rom;
        this.nokkelord = nokkelord;
    }

    private SessionDTO() {
        // For (de)serialization
        this.tittel = null;
        this.format = null;
        this.starter = null;
        this.stopper = null;
        this.foredragsholdere = null;
        this.sprak = null;
        this.niva = null;
        this.links = null;
        this.rom = null;
        this.nokkelord = new ArrayList<>();
    }

}
