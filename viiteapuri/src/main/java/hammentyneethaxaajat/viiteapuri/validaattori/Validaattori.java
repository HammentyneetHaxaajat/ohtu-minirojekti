package hammentyneethaxaajat.viiteapuri.validaattori;

import hammentyneethaxaajat.viiteapuri.viite.AttrTyyppi;
import hammentyneethaxaajat.viiteapuri.viite.ViiteTyyppi;
import hammentyneethaxaajat.viiteapuri.viite.ViiteKasittelija;
import java.util.Arrays;
import java.util.stream.Collectors;

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
            case "tyyppi":
                validoiViiteTyyppi(arvo);
                break;
            case "nimi":
                validoiNimi(arvo);
                break;
            case "crossref":
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
            heitaException("Tuntematon viitteen tyyppi. " + tuetutTyypit);
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
            heitaException("Nimi varattu. Valitse toinen nimi.\n");
            //TODO MÄÄRITÄ NIMEN SYNTAKSI
        } else if (!nimi.matches(".*")) {
            heitaException("Nimi ei vastaa sille määrätyä syntaksia.\n");
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
    private void validoiRistiviite(String arvo) {
        Boolean eiLoydy = viiteKasittelija.getViitteet().stream()
                .map(s -> s.getNimi())
                .noneMatch(s -> s.equals(arvo));
        if (!arvo.isEmpty() && eiLoydy) {
            heitaException(arvo + " nimistä viitettä ei löydetty. Et voi viitata olemattomiin viitteisiin\n");
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

}
