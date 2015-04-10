package hammentyneethaxaajat.viiteapuri.UI;

import hammentyneethaxaajat.viiteapuri.validaattori.Validoija;
import hammentyneethaxaajat.viiteapuri.viite.ViiteTyyppi;
import hammentyneethaxaajat.viiteapuri.viite.ViiteKasittelija;
import hammentyneethaxaajat.viiteapuri.viite.Viite;
import hammentyneethaxaajat.viiteapuri.IO.IO;
import java.util.Map;
import java.util.stream.Collectors;
import static resurssit.Tulosteet.*;

/**
 * Tekstikäyttöliittymä, joka toimii ikään kuin
 * "pääohjelmana". Käyttöliittymän kautta kutsutaan
 * muiden luokkien olioita tekemään osan ohjelman
 * suorituksesta puolestaan.
 */

public class Tekstikayttoliittyma implements Runnable {

    private ViiteKasittelija viiteKasittelija;
    private Validoija validaattori;
    private IO io;
    private boolean kaynnissa;

    public Tekstikayttoliittyma(ViiteKasittelija viiteKasittelija, Validoija validaattori, IO io) {
        kaynnissa = true;
        this.viiteKasittelija = viiteKasittelija;
        this.validaattori = validaattori;
        this.io = io;
    }

    /**
     * Ohjelman toiminta perustuu siihen että niin kauan kuin ohjelma on
     * käynnissä, kysytään käyttäjältä syöte ja suoritetaan sitä vastaava
     * toiminnallisuus alla olevassa while-loopissa.
     */
    
    @Override
    public void run() {
        listaaKomennot();

        while (kaynnissa) {
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

    protected String kysyKomento() {
        return kysele("Syötä komento");
    }

    /**
     * Kysyy käyttäjältä syötettä kunnes annetaan kelvollinen syöte.
     *
     * @param nimi Kysyttävän arvon nimi
     * @param epatyhja Tieto siitä pitääkö käyttäjän syöttää arvoa lainkaan
     * @return
     */
    
    protected String hankiValidiSyöte(String nimi, boolean epatyhja) {
        while (true) {
            String syote = kysele(nimi + (epatyhja ? "*" : ""));

            if (epatyhja && syote.trim().equals("")) {
                io.tulosta(VIRHE_EI_TYHJA);
                continue;
            }

            try {
                if (!syote.isEmpty()) {
                    validaattori.validoi(nimi, syote);
                }
                return syote;
            } catch (IllegalArgumentException e) {
                io.tulosta(e.getMessage());
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
            case "uusi":
                uusiViite();
                break;
            case "listaa":
                listaaViitteet();
                break;
//            case "bibtex":
//                tulostaBibtex();
//                break;
            case "lopeta":
                kaynnissa = false;
                break;
            default:
                io.tulosta("Tuntematon komento. ");
                listaaKomennot();
                break;
        }
    }

    /**
     * Kyselee kaikki uuden viitteen luomiseen tarvittavat arvot ja antaa uuden
     * viitteen viitteenkäsittelijälle.
     */
    
    protected void uusiViite() {
        //TODO Tee tästä kaunis....
        io.tulosta("Luodaan uusi viite.\n");
        Viite uusiViite = new Viite();
        io.tulosta("Tähdellä(*) merkityt kentät ovat pakollisia.\n");

        //Hommataan nimi
        uusiViite.setNimi(hankiValidiSyöte("nimi", true));

        //Hommataan typpi      
        uusiViite.setTyyppi(ViiteTyyppi.valueOf(hankiValidiSyöte("tyyppi", true)));

        //Määritä mahdollinen ristiviittaus
        String crossref = hankiValidiSyöte("crossref", false);
        uusiViite.setAttribuutti("crossref", crossref);

        // Miten voi refaktoroida alla olevan yhteen erilliseen metodikutsuun vai yritetäänkö Java 7:lla kirjoittaa?
        
        //Kysellään kutakin pakollista attribuuttia vastaava arvo ja mapitetaan ne.
        uusiViite.getTyyppi().getPakolliset().stream()//Tehdään stream pakollisita AttrTyypeistä
                .map(s -> s.name())//vaihdetaan AttrTyypit vastaaviin Stringeihin
                .sorted()//Laitetaan aakkosjärjestykseen
                .forEach(s -> uusiViite.setAttribuutti(s, hankiValidiSyöte(s, onPakollinen(s, crossref)))); // Kerätään mapiksi, jos crossfer kohde määritelty ja sisältää attribuutin arvon niin sitä attribuuttia ei tarvitse syöttää.

        //sama valinnaisille... parempi ratkaisu lienee olemassa mutta slack :3
        uusiViite.getTyyppi().getValinnaiset().stream()
                .map(s -> s.name())
                .sorted()
                .forEach(s -> uusiViite.setAttribuutti(s, hankiValidiSyöte(s, false)));
        
        viiteKasittelija.lisaaViite(uusiViite);
        io.tulosta("Viite lisätty onnistuneesti!\n");
    }

    protected void listaaViitteet() {
        io.tulosta(viiteKasittelija.viitteetListauksena());
    }

//    protected void tulostaBibtex() {
//        io.tulosta(viiteKasittelija.viitteetBibtexina());
//    }
    
    protected void listaaKomennot() {
        io.tulosta("Tuetut komennot: uusi, listaa, lopeta.\n");
    }

    /**
     * Apumetodi, joka tarkistaa onko Attribuutti pakko syöttää vai onko se jo
     * määritelty ristiviitattavassa viitteessä.
     *
     * @param attribuutti Attribuutti, jota etsitään viitattavasta luokasta
     * @param crossref Viitteen nimi jo ristiviittaus kohdistuu
     * @return false jos attribuutti on määritetty. Muulloin true.
     */
    
    protected boolean onPakollinen(String attribuutti, String crossref) {
        // TODO Tämän toiminnallisuuden voisi siirtää myös viitteen metodiksi. viite siis osaisi kertoa listan pakollisista, valinnaisista ja/tai kaikista attributteistaan. 
        return crossref.equals("") ? true : viiteKasittelija.haeViite(crossref).getAttribuutti(attribuutti) == null || viiteKasittelija.haeViite(crossref).getAttribuutti(attribuutti).getArvo().equals("");
    }
}
