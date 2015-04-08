package hammentyneethaxaajat.viiteapuri.validaattori;

import hammentyneethaxaajat.viiteapuri.viite.AttrTyyppi;
import hammentyneethaxaajat.viiteapuri.viite.ViiteTyyppi;
import hammentyneethaxaajat.viiteapuri.viite.ViiteKasittelija;
import java.util.Arrays;

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
                tarkistaEttaNimiUniikki(arvo);
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
        try {
            Arrays.stream(ViiteTyyppi.values()).map(s -> s.name()).anyMatch(s -> s.equals(tyyppi));
        }
        catch (IllegalArgumentException e) {
            heitaException("Viitteen tyyppi ei validi.");
        }
    }

    /**
     * Tarkistaa, että saman nimistä viiteolioita ei ole jo viitelistassa.
     * @param nimi
     * @throws IllegalArgumentException jos nimi on jo käytössä
     */
    
    protected void tarkistaEttaNimiUniikki(String nimi) {
        try {
            viiteKasittelija.getViitteet().stream().map(s -> s.getNimi()).noneMatch(s -> s.equals(nimi));
        }
        catch (IllegalArgumentException e) {
            heitaException("Samalla nimellä oleva viite on jo olemassa.\nValitse toinen nimi.\n");
        }
    }

    protected void validoiAttribuutti(String nimi, String arvo) {
        AttrTyyppi tyyppi = null;
        
        try {
            tyyppi = AttrTyyppi.valueOf(nimi);
        }
        catch (IllegalArgumentException e) {
            heitaException("Tämän nimistä attribuuttia ei viitteellä ole.\nSyötä jokin sille kuuluvista attribuuteista.\n");
        }
        
        if (tyyppi != null && ! arvo.matches(tyyppi.getMuoto())) {
            heitaException("Attribuutin kirjoitusasu ei täsmää BibTex -muodon kanssa.\nSyötä attribuutti uudelleen.\n");
        }
    }
    
    protected void heitaException(String msg) {
        throw new IllegalArgumentException(msg);
    }

}
