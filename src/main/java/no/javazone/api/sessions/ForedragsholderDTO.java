package no.javazone.api.sessions;

public class ForedragsholderDTO {
    public final String navn;

    public ForedragsholderDTO(String navn) {
        this.navn = navn;
    }

    private ForedragsholderDTO() {
        this.navn = null;
    }
}
