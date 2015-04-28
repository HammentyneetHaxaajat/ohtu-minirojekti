package hammentyneethaxaajat.viiteapuri.validaattori;

import static hammentyneethaxaajat.viiteapuri.resurssit.Tulosteet.*;
import hammentyneethaxaajat.viiteapuri.viite.AttrTyyppi;
import hammentyneethaxaajat.viiteapuri.viite.Viite;
import hammentyneethaxaajat.viiteapuri.viite.ViiteKasittelija;
import hammentyneethaxaajat.viiteapuri.viite.ViiteTyyppi;
import java.util.Arrays;
import java.util.HashMap;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Luokka, joka validoi viitteen kenttiin tulevia arvoja.
 */
public class Validaattori implements Validoija {

    private ViiteKasittelija viiteKasittelija;
    private HashMap<String, Consumer<String>> komennot;

    public Validaattori(ViiteKasittelija viiteKasittelija) {
        this.viiteKasittelija = viiteKasittelija;
        this.komennot = new HashMap<>();
        luoKomennot();
    }

    /**
     * Luo ohjelmassa käyttäjälle tulostettavat komennot
     */
    private void luoKomennot() {
        komennot.put(KYSY_TIEDOSTO_NIMI, nimi -> validoiTiedostoNimi(nimi));
        komennot.put(KYSY_TIEDOSTO_POLKU, polku -> validoiTiedostoPolku(polku));
        komennot.put(TYYPPI, tyyppi -> validoiViiteTyyppi(tyyppi));
        komennot.put(BIBTEXAVAIN, avain -> validoiBibtexAvain(avain));
        komennot.put(VIITE, arvo -> {
            tarkistaTyhjyys(arvo);
            validoiViite(arvo);
        });
        komennot.put(POISTETTAVA_VIITE, arvo -> {
            tarkistaTyhjyys(arvo);
            validoiViite(arvo);
        });
        komennot.put(CROSSREF, arvo -> validoiViite(arvo));
        komennot.put(ATTRIBUUTTI, attr -> validoiOnAttribuutti(attr));
        komennot.put(KYSY_HAKUSANA, arvo -> validoiHakusana(arvo));
    }

    /**
     * Validoi annetun arvon sitä vastaavan tunnisteen mukaan.
     *
     * @param tyyppi Validoitavan tiedon tunniste/nimi.
     * @param arvo Validoitava arvo.
     */
    @Override
    public void validoi(String tyyppi, String arvo) {
        Consumer komento = komennot.get(tyyppi);
        if (komento == null) {
            heitaException("Tuntematon validoitava.\n");
        }

        komento.accept(arvo);
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
        if (attr.equals(ATTRIBUUTTI)) {
            validoiMuokattavanAttribuutinTyyppi(viite, arvo);
        } else if (attr.equals(BIBTEXAVAIN)) {
            validoiBibtexAvain(arvo);
        } else {
            validoiViitteenAttribuutti(viite, attr, arvo);
        }
    }

    /**
     * Validoi viitteelle kuuluvan "tavallisen" attribuutin
     *
     * @param viite Viite jolle attribuutti kuuluu.
     * @param attr Validoitava attribuutti.
     * @param arvo Validoitava arvo.
     * @throws IllegalArgumentException Jos Arvo ei täytä attribuutin ja
     * viitteen sille asettamia vaatimuksia.
     */
    protected void validoiViitteenAttribuutti(Viite viite, String attr, String arvo) {
        if (viiteKasittelija.pakollisetAttribuutit(viite).stream()
                .anyMatch(s -> s.name().equals(attr))) {
            tarkistaTyhjyys(arvo);
        }
        validoiAttribuutinArvo(attr, arvo);
    }

    /**
     * Tarkistaa vastaako string yhtään ohjelman tunnistamaa attribuuttia
     *
     * @param attribuutti Validoitava attribuutti
     */
    protected void validoiOnAttribuutti(String attribuutti) {
        try {
            AttrTyyppi.valueOf(attribuutti);
        } catch (Exception e) {
            heitaException(VIRHE_EI_OLE_ATTRIBUUTTI);
        }
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
     * @param avain Validoitava nimi.
     * @throws IllegalArgumentException jos nimi on jo käytössä tai se ei vastaa
     * syntaksia.
     */
    protected void validoiBibtexAvain(String avain) {
        if (viiteKasittelija.getViitteet().stream()
                .map(s -> s.getBibtexAvain())
                .anyMatch(s -> s.equals(avain))) {
            heitaException(NIMI_VARATTU);
        } //TODO MÄÄRITÄ NIMEN SYNTAKSI. voi tehdä AttrTyyppi enumin sille niin voi määrittää samassa paikassa muiden kanssa.
        else if (!avain.matches("[\\p{L}\\w\\s]*")) {
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
                .map(s -> s.getBibtexAvain())
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
    protected void validoiMuokattavanAttribuutinTyyppi(Viite viite, String tyyppi) {
        if (tyyppi.equals(AttrTyyppi.crossref.name())) {
            //  TODO Kommentoi alempi rivi jos crossreffin muuttaminen sallitaan joskus. Crossreffin muuttamisen toiminnallisuus pitää myös korjata muualla koodissa jos sallitaan.
            heitaException("Crossref kentän arvoa ei voi muuttaa asettamisen jälkeen.\n");
        } else if (tyyppi.equals(BIBTEXAVAIN)) {
            //  TODO Kommentoi alempi rivi jos nimen vaihtaminen sallitaan.
//            heitaException("Nimi kentän arvoa ei voi muuttaa asettamisen jälkeen.\n");
        } else if (viite.getTyyppi().getKaikki().stream().noneMatch(a -> a.name().equals(tyyppi))) {
            heitaException("Viitteellä ei ole " + tyyppi + " nimistä attribuuttia.\n");
        }
    }

    /**
     * Validoi tiedostonimen.
     */
    private void validoiTiedostoNimi(String arvo) {
        if (arvo.matches(".*\\W.*")) {
            heitaException(VIRHE_TIEDOSTONIMI);
        }
    }
    
    /**
     * Tarkistaa että tiedostopolku on annettu oikeassa muodossa.
     */
    private void validoiTiedostoPolku(String arvo) {
        if (!arvo.matches("(.*/)*")) {
            heitaException(VIRHE_TIEDOSTOPOLKU);
        }
    }

    /**
     * Varmistaa, että annettu hakusana vastaa ohjelmassa olevia attribuutteja.
     * @param Käyttäjän syöttämä hakusana. 
     */
    private void validoiHakusana(String arvo) {
        if (Arrays.stream(AttrTyyppi.values()).map(attr -> attr.name()).noneMatch(attr -> attr.equals(arvo)) && !arvo.matches("tyyppi")) {
            heitaException("Tuntematon attribuutti.\n");
        }
    }
}
