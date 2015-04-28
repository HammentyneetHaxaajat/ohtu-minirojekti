package hammentyneethaxaajat.viiteapuri.UI.toiminnot;

import hammentyneethaxaajat.viiteapuri.IO.BibtexIO;
import hammentyneethaxaajat.viiteapuri.IO.IO;
import hammentyneethaxaajat.viiteapuri.UI.Tekstikayttoliittyma;
import hammentyneethaxaajat.viiteapuri.resurssit.Tulosteet;
import hammentyneethaxaajat.viiteapuri.validaattori.Validoija;
import hammentyneethaxaajat.viiteapuri.viite.ViiteKasittelija;
import static hammentyneethaxaajat.viiteapuri.resurssit.Tulosteet.*;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Tehdas luokka toiminnoille.
 * Pitää käyttöliittymään kuuluvien toimintoja omissa luokissaan mapissa.
 */
public class Toimintotehdas {

    private Map<String, Toiminto> toiminnotKirjain;
    private Map<String, Toiminto> toiminnotNumero;
    private Toiminto tuntematon;

    public Toimintotehdas(Tekstikayttoliittyma ui, IO io, ViiteKasittelija kasittelija, Validoija validoija, BibtexIO tiedostokasittelija) {
        toiminnotKirjain = new HashMap<>();
        toiminnotNumero = new HashMap<>();
        alusta(io, kasittelija, validoija, tiedostokasittelija, ui);
        tuntematon = new TuntematonToiminto(io, kasittelija, validoija);
    }

    /**
     * Hakee mapista halutun toiminnon ja suorittaa sen.
     * @param toiminto Toiminnon nimi.
     */
    public void suorita(String toiminto) {
        if (toiminnotKirjain.containsKey(toiminto)) {
            toiminnotKirjain.get(toiminto).suorita();
        } else if (toiminnotNumero.containsKey(toiminto)) {
            toiminnotNumero.get(toiminto).suorita();
        } else {
            tuntematon.suorita();
        }
    }

    /**
     * Alustaa jokaisen toiminnon luomalla toiminto-olion ja laittaa ne mappiin
     * @param io Input output luokka.
     * @param kasittelija Viitteiden käsittelijä
     * @param validoija Validointitoiminnot
     * @param tiedostokasittelija Luokka.
     * @param ui Ohjelman tekstikäyttöliittymä.
     */
    private void alusta(IO io, ViiteKasittelija kasittelija, Validoija validoija, BibtexIO tiedostokasittelija, Tekstikayttoliittyma ui) {
        toiminnotKirjain.put(Tulosteet.KOMENTO_UUSI, new UusiViite(io, kasittelija, validoija));
        toiminnotKirjain.put(Tulosteet.KOMENTO_MUOKKAA, new MuokkaaViitetta(io, kasittelija, validoija));
        toiminnotKirjain.put(Tulosteet.KOMENTO_POISTA, new PoistaViite(io, kasittelija, validoija));
        toiminnotKirjain.put(Tulosteet.KOMENTO_LISTAA, new ListaaViitteet(io, kasittelija, validoija));
        toiminnotKirjain.put(Tulosteet.KOMENTO_HAE_VIITTEITA, new HaeViitteita(io, kasittelija, validoija));
        toiminnotKirjain.put(Tulosteet.KOMENTO_LUO_BIBTEX, new luoBibtex(io, kasittelija, validoija, tiedostokasittelija));
        toiminnotKirjain.put(Tulosteet.KOMENTO_TUO_BIBTEX, new tuoBibtex(io, kasittelija, validoija, tiedostokasittelija));
        toiminnotKirjain.put(Tulosteet.KOMENTO_LOPETA, new Lopeta(io, kasittelija, validoija, ui));

        toiminnotNumero.put(Tulosteet.KOMENTO_UUSI_NUMERO, new UusiViite(io, kasittelija, validoija));
        toiminnotNumero.put(Tulosteet.KOMENTO_MUOKKAA_NUMERO, new MuokkaaViitetta(io, kasittelija, validoija));
        toiminnotNumero.put(Tulosteet.KOMENTO_POISTA_NUMERO, new PoistaViite(io, kasittelija, validoija));
        toiminnotNumero.put(Tulosteet.KOMENTO_LISTAA_NUMERO, new ListaaViitteet(io, kasittelija, validoija));
        toiminnotNumero.put(Tulosteet.KOMENTO_HAE_VIITTEITA_NUMERO, new HaeViitteita(io, kasittelija, validoija));
        toiminnotNumero.put(Tulosteet.KOMENTO_LUO_BIBTEX_NUMERO, new luoBibtex(io, kasittelija, validoija, tiedostokasittelija));
        toiminnotNumero.put(Tulosteet.KOMENTO_TUO_BIBTEX_NUMERO, new tuoBibtex(io, kasittelija, validoija, tiedostokasittelija));
        toiminnotNumero.put(Tulosteet.KOMENTO_LOPETA_NUMERO, new Lopeta(io, kasittelija, validoija, ui));
    }

    /**
     * Palauttaa ohjelman toiminnot ja niiden käyttöohjeet.
     * @return komennot ja ohjeet stringinä.
     */
    public String ohjeet() {

        return TUETUT_KOMENNOT + OHJEET;
    }
}
