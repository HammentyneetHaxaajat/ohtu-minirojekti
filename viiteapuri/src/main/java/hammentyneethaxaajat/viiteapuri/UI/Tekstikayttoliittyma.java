package hammentyneethaxaajat.viiteapuri.UI;

import hammentyneethaxaajat.viiteapuri.IO.BibtexIO;
import hammentyneethaxaajat.viiteapuri.validaattori.Validoija;
import hammentyneethaxaajat.viiteapuri.viite.ViiteTyyppi;
import hammentyneethaxaajat.viiteapuri.viite.ViiteKasittelija;
import hammentyneethaxaajat.viiteapuri.viite.Viite;
import hammentyneethaxaajat.viiteapuri.IO.IO;
import static hammentyneethaxaajat.viiteapuri.resurssit.Tulosteet.*;
import hammentyneethaxaajat.viiteapuri.viite.AttrTyyppi;
import java.io.IOException;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Tekstikäyttöliittymä, joka toimii ikään kuin "pääohjelmana". Käyttöliittymän
 * kautta kutsutaan muiden luokkien olioita tekemään osan ohjelman suorituksesta
 * puolestaan.
 */
public class Tekstikayttoliittyma implements Runnable {

    private ViiteKasittelija viiteKasittelija;
    private Validoija validaattori;
    private IO io;
    private boolean kaynnissa;
    private BibtexIO tiedostonKasittelija;

    public Tekstikayttoliittyma(ViiteKasittelija viiteKasittelija, Validoija validaattori, IO io) {
        kaynnissa = true;
        this.viiteKasittelija = viiteKasittelija;
        this.validaattori = validaattori;
        this.io = io;
        tiedostonKasittelija = new BibtexIO();
    }

    /**
     * Ohjelman toiminta perustuu siihen että niin kauan kuin ohjelma on
     * käynnissä, kysytään käyttäjältä syöte ja suoritetaan sitä vastaava
     * toiminnallisuus alla olevassa while-loopissa.
     */
    @Override
    public void run() {

        while (kaynnissa) {
            listaaKomennot();
            String komento = kysyKomento();
            suoritaToiminto(komento);
        }
    }

    /**
     * Pyytää käyttäjältä syöteen.
     *
     * @param teksti Käyttäjälle esitettävä tuloste.
     * @return Käyttäjän antama syöte.
     */
    protected String kysele(String teksti) {
        return io.lueRivi(teksti + ":\n");
    }

    /**
     * Kutsuu metodia kysele valmiilla kehotteella SYOTA_KOMENTO. Kehote haetaan
     * luokasta Tulosteet.
     *
     * @return käyttäjän antama syöte.
     */
    protected String kysyKomento() {
        return kysele(SYOTA_KOMENTO);
    }

    /**
     * Kysyy käyttäjältä syötettä kunnes annetaan kelvollinen syöte.
     *
     * @param nimi Kysyttävän arvon nimi
     * @param epatyhja Tieto siitä pitääkö käyttäjän syöttää arvoa lainkaan
     * @return käyttäjän antama syöte
     */
    protected String hankiValidiMahdTyhjaSyote(String nimi) {
        return hankiValidiSyote(nimi, false);
    }
    
    protected String hankiValidiEpaTyhjaSyote(String nimi) {
        return hankiValidiSyote(nimi, true);
    }
    
    protected String hankiValidiSyote(String nimi, boolean epatyhja) {
        while (true) {
             String syote = kysele(nimi + (epatyhja ? "*" : ""));
            
            try {
                if (epatyhja) {
                    validaattori.validoiEttaEpaTyhja(syote);
                }
                
                validaattori.validoi(nimi, syote);
                return syote;
                
            } catch (IllegalArgumentException e) {
                tulostaVirheIlmoitus(e);
            }
        }
    }

    /**
     * Valitseen suoritettavan komennon parametrin perusteella.
     *
     * @param komento Haluttu komento.
     */
    protected void suoritaToiminto(String komento) {
        switch (komento) {
            case UUSI:
                uusiViite();
                break;
            case LISTAA:
                listaaViitteet();
                break;
            case MUOKKAA:
                muokkaaViitetta();
                break;
            case POISTA:
                poistaViite();
                break;
            case BIBTEX:
                luoBibtex();
                break;
            case LOPETA:
                io.tulosta(VIESTI_HEIHEI);
                kaynnissa = false;
                break;
            default:
                io.tulosta(TUNTEMATON_KOMENTO);
                listaaKomennot();
                break;
        }
    }

    /**
     * Kyselee kaikki uuden viitteen luomiseen tarvittavat arvot ja antaa uuden
     * viitteen viitteenkäsittelijälle.
     */
    protected void uusiViite() {
        io.tulosta(UUDEN_VIITTEEN_LUONTI);
        Viite uusiViite = new Viite();

        uusiViite.setNimi(hankiValidiEpaTyhjaSyote(NIMI));   
        uusiViite.setTyyppi(ViiteTyyppi.valueOf(hankiValidiEpaTyhjaSyote(TYYPPI)));
        uusiViite.setAttribuutti(CROSSREF, hankiValidiMahdTyhjaSyote(CROSSREF));

        syotaKaikki(uusiViite.getTyyppi().getPakolliset(), uusiViite);
        syotaKaikki(uusiViite.getTyyppi().getValinnaiset(), uusiViite);

        viiteKasittelija.lisaaViite(uusiViite);
        tulostaViitteenTiedot(uusiViite);
        io.tulosta('\n' + VIITE_LISATTY_ONNISTUNEESTI);
    }
    
    protected void syotaKaikki(Collection<AttrTyyppi> tyypit, Viite viite) {
        tyypit.stream()
        .map(attr -> attr.name())//vaihdetaan AttrTyypit vastaaviin Stringeihin
        .sorted()//Laitetaan aakkosjärjestykseen
        .forEach(attr -> viite.setAttribuutti(attr, hankiValidiSyote(attr, viiteKasittelija.pakollinenSyotettavaAttribuutti(viite, attr))));
    }

    /**
     * Palauttaa selkokielisen listan ohjelman sisältämistä viitteistä
     */
    protected void listaaViitteet() {
        if (!ohjelmassaViitteita()) {
            io.tulosta(VIRHE_EIVIITTEITA);
        } else {
            io.tulosta(viiteKasittelija.viitteetListauksena());
        }
    }

    /**
     * Tekee .bib tiedoston ohjelman sisältämistä viitteistä.
     */
    protected void luoBibtex() {
        //TODO Vaihtakaa polun yms. kysely johonkin järkevään. 
        String nimi = hankiValidiMahdTyhjaSyote(KYSY_TIEDOSTO_NIMI);
        String polku = hankiValidiMahdTyhjaSyote(KYSY_TIEDOSTO_POLKU);

        if (nimi.matches("")) {
            nimi = "test";
        }
        if (polku.matches("")) {
            polku = "target/";
        }

        try {
            tiedostonKasittelija.kirjoitaBibtex(viiteKasittelija.viitteetBibtexina(), polku + nimi);
            io.tulosta(TIEDOSTONLUONTI_ONNISTUI + polku + nimi + ".bib\n");
        } catch (IOException e) {
            io.tulosta(TIEDOSTONLUONTI_EI_ONNISTUNUT);
        }
        if (!ohjelmassaViitteita()) {
            io.tulosta("Huom! Luotu tiedosto ei sisällä yhtään viitettä.\n");
        }
    }

    /**
     * Listaa ohjelmassa tuetut komennot.
     */
    protected void listaaKomennot() {
        io.tulosta(TUETUT_KOMENNOT);
    }

//    /**
//     * Apumetodi, joka tarkistaa onko Attribuutti pakko syöttää vai onko se jo
//     * määritelty ristiviitattavassa viitteessä.
//     *
//     * @param attribuutti Attribuutti, jota etsitään viitattavasta luokasta
//     * @param crossref Viitteen nimi jo ristiviittaus kohdistuu
//     * @return false jos attribuutti on määritetty. Muulloin true.
//     */
//    protected boolean onPakollinen(String attribuutti, String crossref) {
//        // TODO Tämän toiminnallisuuden voisi siirtää myös viitteen metodiksi. viite siis osaisi kertoa listan pakollisista, valinnaisista ja/tai kaikista attributteistaan. 
//        return crossref.equals("") ? true : viiteKasittelija.haeViite(crossref).getAttribuutti(attribuutti) == null || viiteKasittelija.haeViite(crossref).getAttribuutti(attribuutti).getArvo().equals("");
//    }

    /**
     * Ohjattu viitteenmuokkastoiminto. Antaa käyttäjän muokata olemassaolevia
     * viitteitä
     */
    private void muokkaaViitetta() {
        //TODO Kaunistelkaa. Pitääkö nimen olla vaihdettavissa?
        if (!ohjelmassaViitteita()) {
            io.tulosta(VIRHE_EIVIITTEITA);
        } else {
            tulostaViestiJaViitteet("Muokattavissa olevat viitteet: ");

            Viite viite = haeViiteKayttajanSyotteenPerusteella();
            tulostaViitteenTiedot(viite);
            
            String attribuutti = muokattavaAttribuutti(viite);

            io.tulosta("Uusi arvo attribuutille ");
            String uusiArvo = hankiValidiSyote(attribuutti, viiteKasittelija.pakollinenSyotettavaAttribuutti(viite, attribuutti));
            viite.setAttribuutti(attribuutti, uusiArvo);
        }
    }
    
    /**
     * Selvittää käyttäjältä viitteen attribuutin, jota käyttäjä haluaa muokata.
     * @return 
     */
    private String muokattavaAttribuutti(Viite viite) {
        while (true) {
            String attribuutti = kysele(ATTRIBUUTTIKYSELY);
            
            try {
                validaattori.validoi(viite, attribuutti, "muokkaa");
                return attribuutti;
            } catch (IllegalArgumentException e) {
                tulostaVirheIlmoitus(e);
            }
        }
    }

    private void poistaViite() {
        if (!ohjelmassaViitteita()) {
            io.tulosta(VIRHE_EIVIITTEITA);
        } else {
            tulostaViestiJaViitteet("Poistettavissa olevat viitteet: ");

            Viite viite = haeViiteKayttajanSyotteenPerusteella();
            tulostaViitteenTiedot(viite);
            
            String varmistus = kysele(KYSY_VARMISTUS);
            if (varmistus.equals("poista")) {
                viiteKasittelija.poistaViite(viite);
            } else {
                io.tulosta("Viitettä ei poistettu.\n");
            }
        }

    }

    private boolean ohjelmassaViitteita() {
        return !viiteKasittelija.getViitteet().isEmpty();
    }
    
    private void tulostaViestiJaViitteet(String viesti) {
        io.tulosta(viiteKasittelija.getViitteet().stream()
                .map(s -> s.getNimi())
                .collect(Collectors.joining(", ", viesti, "\n")));
    }
    
    private void tulostaViitteenTiedot(Viite viite) {
        io.tulosta("\nViitteen tiedot:\n" + viite.listaus());
    }
    
    private void tulostaVirheIlmoitus(IllegalArgumentException virhe) {
        io.tulosta(virhe.getMessage());
    }
    
    private Viite haeViiteKayttajanSyotteenPerusteella() {
        return viiteKasittelija.haeViite(hankiValidiEpaTyhjaSyote(VIITE));
    }
}
