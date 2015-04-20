package hammentyneethaxaajat.viiteapuri.validaattori;

import hammentyneethaxaajat.viiteapuri.viite.AttrTyyppi;
import hammentyneethaxaajat.viiteapuri.viite.ViiteTyyppi;
import hammentyneethaxaajat.viiteapuri.viite.ViiteKasittelija;
import java.util.Arrays;
import java.util.stream.Collectors;
import static hammentyneethaxaajat.viiteapuri.resurssit.Tulosteet.*;
import hammentyneethaxaajat.viiteapuri.viite.Viite;

/**
 * Luokka, joka validoi viitteen kenttiin tulevia arvoja.
 */
public class Validaattori implements Validoija {

    private ViiteKasittelija viiteKasittelija;

    public Validaattori(ViiteKasittelija viiteKasittelija) {
        this.viiteKasittelija = viiteKasittelija;
    }

    /**
     * Validoi annetun arvon sitä vastaavan tunnisteen mukaan.
     *
     * @param tyyppi Validoitavan tiedon tunniste/nimi.
     * @param arvo Validoitava arvo.
     */
    @Override
    public void validoi(String tyyppi, String arvo) {
        switch (tyyppi) {
            case KYSY_TIEDOSTO_NIMI:
                validoiTiedostoNimi(arvo);
                break;
            case KYSY_TIEDOSTO_POLKU:
                //TODO IMPLEMENTOI tarkistus hyväksyttävälle polulle
                validoiTiedostoPolku(arvo);
                break;
            case TYYPPI:
                validoiViiteTyyppi(arvo);
                break;
            case NIMI:
                validoiNimi(arvo);
                break;
            case VIITE:
                tarkistaTyhjyys(arvo);
            case CROSSREF:
                validoiViite(arvo);
                break;
            default:
                heitaException("Tuntematon validoitava.\n");
        }
    }

    /**
     * Validoi viitteelle asetettavia attribuutteja sekä niiden arvoja.
     *
     * @param viite Viite jolle attribuutti kuuluu.
     * @param attr Attribuutti joka tarkistetaan
     * @param arvo Attribuutin arvo.
     */
    @Override
    public void validoi(Viite viite, String attr, String arvo) {
        switch (attr) {
            case ATTRIBUUTTI:
                validoiAttribuutinTyyppi(viite, arvo);
                break;
            case NIMI:
                validoiNimi(arvo);
                break;
            default:
                validoiAttribuutti(viite, attr, arvo);
        }
    }

    /**
     * Validoi viitteelle kuuluvan "tavallisen" attribuutin
     *
     * @param viite Viite jolle attribuutti kuuluu.
     * @param attr Validoitava attribuutti.
     * @param arvo Validoitava arvo.
     * @throws IllegalArgumentException Jos Arvo ei täytä attribuutin ja viitteen sille asettamia vaatimuksia.
     */
    protected void validoiAttribuutti(Viite viite, String attr, String arvo) {
        if (viiteKasittelija.pakollisetAttribuutit(viite).stream()
                .anyMatch(s -> s.name().equals(attr))) {
            tarkistaTyhjyys(arvo);
        }
        validoiAttribuutinArvo(attr, arvo);
    }

    /**
     * Validoi viitteen tyypin.
     *
     * @param tyyppi Validoitava tyyppi.
     * @throws IllegalArgumentException Jos Tyyppi ei ole kelvollinen.
     */
    protected void validoiViiteTyyppi(String tyyppi) {
        //Tarkistaa vastaako yksikään ViiteTyyppi luokassa määritetty tyyppi annettua
        if (Arrays.stream(ViiteTyyppi.values())
                .map(s -> s.name())
                .noneMatch(s -> s.equals(tyyppi))) {
            //Listataan tuetut tyypit virheilmoituksessa.
            String tuetutTyypit = Arrays.stream(ViiteTyyppi.values())
                    .map(s -> s.name())
                    .collect(Collectors.joining(", ", "Tuetut tyypit: ", ".\n"));
            heitaException(TUNTEMATON_VIITETYYPPI + tuetutTyypit);
        }
    }

    /**
     * Validoi viitteelle annettavan nimen.
     *
     * @param nimi Validoitava nimi.
     * @throws IllegalArgumentException jos nimi on jo käytössä tai se ei vastaa
     * syntaksia.
     */
    protected void validoiNimi(String nimi) {
        tarkistaTyhjyys(nimi);
        if (viiteKasittelija.getViitteet().stream()
                .map(s -> s.getNimi())
                .anyMatch(s -> s.equals(nimi))) {
            heitaException(NIMI_VARATTU);
            //TODO MÄÄRITÄ NIMEN SYNTAKSI. voi tehdä AttrTyyppi enumin sille niin voi määrittää samassa paikassa muiden kanssa.
        } else if (!nimi.matches("[\\p{L}\\w\\s]*")) {
            heitaException(NIMI_EI_VASTAA_SEN_SYNTAKSIA);
        }
    }

    /**
     * Validoi tavallisen viitteen attribuutin arvon.
     *
     * @param nimi Validoitavan arvon nimi/tyyppi.
     * @param arvo Validoitava arvo.
     * @throws IllegalArgumentException Arvo ei vastaan nimen määrittämää
     * muotoa.
     */
    protected void validoiAttribuutinArvo(String nimi, String arvo) {
        AttrTyyppi tyyppi = null;
        try {
            tyyppi = AttrTyyppi.valueOf(nimi);
        } catch (Exception e) {
            heitaException("Tämän nimistä attribuuttia ei ole.\n");
        }

        if (tyyppi != null && !arvo.matches(tyyppi.getMuoto())) {
            heitaException("Syöte ei vastaa hyväksyttyä muotoa.\n");
        }
    }

    /**
     * Validoi ristiviittauksen.
     *
     * @param arvo Validoitava viittaus.
     * @throws IllegalArgumentException jos ristiviitattavaa viitettä ei ole
     * olemassa.
     */
    protected void validoiViite(String arvo) {
        Boolean eiLoydy = viiteKasittelija.getViitteet().stream()
                .map(s -> s.getNimi())
                .noneMatch(s -> s.equals(arvo));
        if (!arvo.isEmpty() && eiLoydy) {
            heitaException(arvo + VIRHE_VIITETTA_EI_OLE);
        }
    }

    /**
     * Tarkistaa onko annettu String tyhjä
     *
     * @param syote Tarkistettava String.
     */
    private void tarkistaTyhjyys(String syote) {
        if (syote.isEmpty()) {
            heitaException(ARVO_EI_SAA_OLLA_TYHJA);
        }
    }

    /**
     * Heittää uuden IllegalArgumentExceptionin määritetyllä viestillä.
     *
     * @param msg Exceptionin viesti.
     */
    protected void heitaException(String msg) {
        throw new IllegalArgumentException(msg);
    }

    /**
     * Tarkistaa että annettu attributti kuuluu viitteelle ja sen arvo voidaan
     * muuttaa.
     *
     * @param viite Viite jolle attribuuti kuuluu
     * @param tyyppi Attribuutin tyyppi
     */
    protected void validoiAttribuutinTyyppi(Viite viite, String tyyppi) {
        if (tyyppi.equals(AttrTyyppi.crossref.name())) {
            //  TODO Kommentoi alempi rivi jos crossreffin muuttaminen sallitaan joskus. Crossreffin muuttamisen toiminnallisuus pitää myös korjata muualla koodissa jos sallitaan.
            heitaException("Crossref kentän arvoa ei voi muuttaa asettamisen jälkeen.\n");
        } 
        
        // Nimen vaihtaminen OK!
        // else if (attr.equals(NIMI)) {
        //  TODO Kommentoi alempi rivi jos nimen vaihtaminen sallitaan.
        //  heitaException("Nimi kentän arvoa ei voi muuttaa asettamisen jälkeen.\n");
        
        else if (viite.getTyyppi().getKaikki().stream().noneMatch(a -> a.name().equals(tyyppi))) {
            heitaException("Viitteellä ei ole " + tyyppi + " nimistä kenttää.\n");
        }
    }

    private void validoiTiedostoNimi(String arvo) {
        if (arvo.matches(".*\\W.*")) {
            heitaException(VIRHE_TIEDOSTONIMI);
        }
    }

    private void validoiTiedostoPolku(String arvo) {
        if (!arvo.matches("(.*/)*")) {
            heitaException(VIRHE_TIEDOSTOPOLKU);
        }
    }
}
