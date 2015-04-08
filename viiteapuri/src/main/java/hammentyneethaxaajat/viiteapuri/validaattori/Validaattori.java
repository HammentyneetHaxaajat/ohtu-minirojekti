package hammentyneethaxaajat.viiteapuri.validaattori;

import hammentyneethaxaajat.viiteapuri.viite.AttrTyyppi;
import hammentyneethaxaajat.viiteapuri.viite.ViiteTyyppi;
import hammentyneethaxaajat.viiteapuri.viite.ViiteKasittelija;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Luokka, joka validoi syötteitä
 */
public class Validaattori implements Validoija {

    //TODO Laita testit heittämään exception virheen ilmaisua varten muualla.
    private ViiteKasittelija viiteKasittelija;

    public Validaattori(ViiteKasittelija viiteKasittelija) {
        this.viiteKasittelija = viiteKasittelija;
    }

    @Override
    public void validoi(String validoitava, String arvo) {
        switch (validoitava) {
            case "tyyppi":
                validoiViiteTyyppi(arvo);
                break;
            case "nimi":
                validoiNimi(arvo);
                break;
            default:
                validoiAttribuutti(validoitava, arvo);
                break;
        }
    }

    /**
     *
     * @param tyyppi
     * @throws IllegalArgumentException jos tyyppi ei validi. EI TOIMI ???
     */
    protected void validoiViiteTyyppi(String tyyppi) {
        if (Arrays.stream(ViiteTyyppi.values()).map(s -> s.name()).noneMatch(s -> s.equals(tyyppi))) {
            String tuetutTyypit = Arrays.stream(ViiteTyyppi.values()).map(s -> s.name()).collect(Collectors.joining(", ", "Tuetut tyypit: ", ".\n"));
            heitaException("Tuntematon viitteen tyyppi. " + tuetutTyypit);
        }
    }

    /**
     * Tarkistaa, että saman nimistä viiteolioita ei ole jo viitelistassa.
     *
     * @param nimi
     * @throws IllegalArgumentException jos nimi on jo käytössä
     */
    protected void validoiNimi(String nimi) {
        if(viiteKasittelija.getViitteet().stream().map(s -> s.getNimi()).anyMatch(s -> s.equals(nimi))) {
            heitaException("Nimi varattu. Valitse toinen nimi.\n");
        } else if(!nimi.matches("[a-zA-Z0-9öÖäÄåÅ]*")){
            heitaException("Nimi saa sisältää vain aakkosia tai numeroita eikä siinä saa olla välejä.\n");
        }
    }

    protected void validoiAttribuutti(String nimi, String arvo) {
        AttrTyyppi tyyppi = null;

        try {
            tyyppi = AttrTyyppi.valueOf(nimi);
        } catch (Exception e) {
            //Tämän ei pitäisi tapahtua ikinä nykyisellä UIlla...
            heitaException("Tämän nimistä attribuuttia ei viitteellä ole.\nSyötä jokin sille kuuluvista attribuuteista.\n");
        }
        
        if (tyyppi != null && !arvo.matches(tyyppi.getMuoto())) {
            //TODO kaikille AtrTyypeille voisi määrittää sanallisen kuvauksen hyväksytystä syötteestä ja lisätä sen tähän.
            heitaException("Syöte ei vastaa hyväksyttyä muotoa.\n");
        }
    }

    protected void heitaException(String msg) {
        throw new IllegalArgumentException(msg);
    }

}
