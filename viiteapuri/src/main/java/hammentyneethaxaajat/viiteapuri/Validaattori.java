package hammentyneethaxaajat.viiteapuri;

import java.util.Arrays;

/**
 * Luokka, joka validoi syötteitä
 */
public class Validaattori implements Validoija {

    //TODO Laita testit heittämään exception virheen ilmaisua varten muualla.
    private ViiteKasittelija vk;

    public Validaattori(ViiteKasittelija vk) {
        this.vk = vk;
    }

    @Override
    public boolean validoi(String nimi, String arvo) {
        switch (nimi) {
            case "tyyppi":
                return validoiViiteTyyppi(arvo);
            case "nimi":
                return validoiNimi(arvo);
            default:
                return validoiAttribuutti(nimi, arvo);
        }
    }

    private boolean validoiViiteTyyppi(String tyyppi) {
        return Arrays.stream(ViiteTyyppi.values()).map(s -> s.name()).anyMatch(s -> s.equals(tyyppi));
    }

    private boolean validoiNimi(String arvo) {
        return vk.getViitteet().stream().map(s -> s.getNimi()).noneMatch(s -> s.equals(arvo));
    }

    private boolean validoiAttribuutti(String nimi, String arvo) {
        try {
            AttrTyyppi tyyppi = AttrTyyppi.valueOf(nimi);
            return arvo.matches(tyyppi.getMuoto());
        } catch (Exception e) {
            return false;
        }
    }

}
