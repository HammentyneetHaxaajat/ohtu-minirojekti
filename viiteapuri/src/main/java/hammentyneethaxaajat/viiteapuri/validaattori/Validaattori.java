package hammentyneethaxaajat.viiteapuri.validaattori;

import hammentyneethaxaajat.viiteapuri.viite.AttrTyyppi;
import hammentyneethaxaajat.viiteapuri.viite.ViiteTyyppi;
import hammentyneethaxaajat.viiteapuri.viite.ViiteKasittelija;
import java.util.Arrays;
import java.util.stream.Collectors;
import static hammentyneethaxaajat.viiteapuri.resurssit.Tulosteet.*;
import hammentyneethaxaajat.viiteapuri.viite.Attribuutti;

/**
 * Luokka, joka validoi viitteen kenttiin tulevia arvoja.
 */
public class Validaattori implements Validoija {

    private ViiteKasittelija viiteKasittelija;

    public Validaattori(ViiteKasittelija viiteKasittelija) {
        this.viiteKasittelija = viiteKasittelija;
    }

    /**
     * Valitsee oikean validointifunktion parametrien perusteella.
     *
     * @param validoitava Validoitavan tiedon tunniste/nimi.
     * @param arvo Validoitava arvo.
     */
    @Override
    public void validoi(String validoitava, String arvo) {
        switch (validoitava) {
            case VIITE:
                tarkistaViitteenOlemassaolo(arvo);
                break;
            case ATTIBUTTIKYSELY:
                //TODO IMPLEMENTOI testi kuuluuko attribuutti tietylle viitteelle. Ja mille viitteelle onkin jotain mitä validaattori ei voi tietää.
                break;
            case KYSY_TIEDOSTO_NIMI:
                //TODO IMPLEMENTOI tarkistus hyväksyttävälle tiedostonimelle
                break;
            case KYSY_TIEDOSTO_POLKU:
                //TODO IMPLEMENTOI tarkistus hyväksyttävälle polulle
                break;
            case TYYPPI:
                validoiViiteTyyppi(arvo);
                break;
            case NIMI:
                validoiNimi(arvo);
                break;
            case CROSSREF:
                validoiRistiviite(arvo);
                break;
            default:
                validoiAttribuutti(validoitava, arvo);
                break;
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
     * @param nimi Validoitava nimi.
     * @throws IllegalArgumentException jos nimi on jo käytössä tai se ei vastaa
     * syntaksia.
     */
    protected void validoiNimi(String nimi) {
        if (viiteKasittelija.getViitteet().stream()
                .map(s -> s.getNimi())
                .anyMatch(s -> s.equals(nimi))) {
            heitaException(NIMI_VARATTU);
            //TODO MÄÄRITÄ NIMEN SYNTAKSI
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
    protected void validoiAttribuutti(String nimi, String arvo) {
        AttrTyyppi tyyppi = null;
        try {
            tyyppi = AttrTyyppi.valueOf(nimi);
        } catch (Exception e) {
            //Tämän ei pitäisi tapahtua ikinä nykyisellä UIlla...
            heitaException("Tämän nimistä attribuuttia ei ole.\n");
        }

        if (tyyppi != null && !arvo.matches(tyyppi.getMuoto())) {
            //TODO kaikille AtrTyypeille voisi määrittää sanallisen kuvauksen hyväksytystä syötteestä ja lisätä sen viestiin. esim yearin kohdalla "year tulee olla muotoa yyyy." tjs.
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
    protected void validoiRistiviite(String arvo) {
        Boolean eiLoydy = viiteKasittelija.getViitteet().stream()
                .map(s -> s.getNimi())
                .noneMatch(s -> s.equals(arvo));
        if (!arvo.isEmpty() && eiLoydy) {
            heitaException(arvo + RISTIVIITETTA_EI_OLE);
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

    private void tarkistaViitteenOlemassaolo(String arvo) {
        if (viiteKasittelija.getViitteet().stream().noneMatch(s -> s.getNimi().matches(arvo))) {
            heitaException(arvo + " ei vastaa olemassa olevaa viitettä.\n");
        }
    }
}
