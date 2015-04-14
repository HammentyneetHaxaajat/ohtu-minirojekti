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
import java.util.logging.Level;
import java.util.logging.Logger;
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
    protected String hankiValidiSyote(String nimi, boolean epatyhja) {
        while (true) {
            String syote = kysele(nimi + (epatyhja ? "*" : ""));

            if (epatyhja && syote.trim().equals("")) {
                io.tulosta(ARVO_EI_SAA_OLLA_TYHJA);
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
        //TODO Tee tästä kaunis....

        io.tulosta(UUDEN_VIITTEEN_LUONTI);
        Viite uusiViite = new Viite();

        //Hommataan nimi
        uusiViite.setNimi(hankiValidiSyote(NIMI, true));

        //Hommataan typpi      
        uusiViite.setTyyppi(ViiteTyyppi.valueOf(hankiValidiSyote(TYYPPI, true)));

        //Määritä mahdollinen ristiviittaus
        String crossref = hankiValidiSyote(CROSSREF, false);
        uusiViite.setAttribuutti(CROSSREF, crossref);

        // Miten voi refaktoroida alla olevan yhteen erilliseen metodikutsuun vai yritetäänkö Java 7:lla kirjoittaa?
        //Kysellään kutakin pakollista attribuuttia vastaava arvo ja mapitetaan ne.
        uusiViite.getTyyppi().getPakolliset().stream()//Tehdään stream pakollisita AttrTyypeistä
                .map(s -> s.name())//vaihdetaan AttrTyypit vastaaviin Stringeihin
                .sorted()//Laitetaan aakkosjärjestykseen
                .forEach(s -> uusiViite.setAttribuutti(s, hankiValidiSyote(s, onPakollinen(s, crossref)))); // Kerätään mapiksi, jos crossfer kohde määritelty ja sisältää attribuutin arvon niin sitä attribuuttia ei tarvitse syöttää.

        //sama valinnaisille... parempi ratkaisu lienee olemassa mutta slack :3
        uusiViite.getTyyppi().getValinnaiset().stream()
                .map(s -> s.name())
                .sorted()
                .forEach(s -> uusiViite.setAttribuutti(s, hankiValidiSyote(s, false)));

        viiteKasittelija.lisaaViite(uusiViite);
        io.tulosta(VIITE_LISATTY_ONNISTUNEESTI);
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
        String nimi = hankiValidiSyote(KYSY_TIEDOSTO_NIMI, false);
        String polku = hankiValidiSyote(KYSY_TIEDOSTO_POLKU, false);

        if (nimi.matches("")) {
            nimi = "test";
        }
        if (polku.matches("")) {
            polku = "target/";
        }

        try {
            tiedostonKasittelija.kirjoitaBibtex(viiteKasittelija.viitteetBibtexina(), polku + nimi);
            io.tulosta(TIEDOSTONLUONTI_ONNISTUI + polku + nimi + ".bib\n");
        } catch (IOException ex) {
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

    /**
     * Ohjattu viitteenmuokkastoiminto. Antaa käyttäjän muokata olemassaolevia
     * viitteitä
     */
    private void muokkaaViitetta() {
        //TODO Kaunistelkaa. Pitääkö nimen olla vaihdettavissa?
        if (!ohjelmassaViitteita()) {
            io.tulosta(VIRHE_EIVIITTEITA);
        } else {
            io.tulosta(viiteKasittelija.getViitteet().stream()
                    .map(s -> s.getNimi())
                    .collect(Collectors.joining(", ", "Muokattavissa olevat viitteet: ", "\n")));
            Viite viite = viiteKasittelija.haeViite(hankiValidiSyote(VIITE, true));
            io.tulosta("Viitteen tiedot:\n" + viite.listaus());
            //FIXIT validaattori ei nykyisellään kykene tarkistamaan tätä joten tarkistetaan tässä
            String attribuutti;
            while (true) {
                attribuutti = kysele(ATTIBUTTIKYSELY);
                try {
                    AttrTyyppi.valueOf(attribuutti);
                    break;
                } catch (Exception e) {
                    io.tulosta("Syötä jokin viitteen tietojen listauksessa näkyvistä attribuuteista.\n");
                }
            }
            io.tulosta("Uusi arvo attribuutille ");
            String uusiArvo = hankiValidiSyote(attribuutti, onPakollinen(attribuutti, viite.getAttribuutti("crossref").getArvo()));
            viite.setAttribuutti(attribuutti, uusiArvo);
        }

    }

    private void poistaViite() {
        if (!ohjelmassaViitteita()) {
            io.tulosta(VIRHE_EIVIITTEITA);
        } else {
            io.tulosta(viiteKasittelija.getViitteet().stream()
                    .map(s -> s.getNimi())
                    .collect(Collectors.joining(", ", "Poistettavissa olevat viitteet: ", "\n")));
            Viite viite = viiteKasittelija.haeViite(hankiValidiSyote(VIITE, true));
            io.tulosta("Viitteen tiedot:\n" + viite.listaus());
            String varmistus = kysele(KYSY_VARMISTUS);
            if (varmistus.equals("poista")) {
                viiteKasittelija.poistaViite(viite);
            }
        }

    }

    private boolean ohjelmassaViitteita() {
        return !viiteKasittelija.getViitteet().isEmpty();
    }
}
