package hammentyneethaxaajat.viiteapuri.UI;

import hammentyneethaxaajat.viiteapuri.validaattori.Validoija;
import hammentyneethaxaajat.viiteapuri.viite.ViiteTyyppi;
import hammentyneethaxaajat.viiteapuri.viite.ViiteKasittelija;
import hammentyneethaxaajat.viiteapuri.viite.Viite;
import hammentyneethaxaajat.viiteapuri.IO.IO;
import java.util.Map;
import java.util.stream.Collectors;

public class Tekstikayttoliittyma implements Runnable {

    private ViiteKasittelija viiteKasittelija;
    private Validoija validaattori;
    private IO io;
    private boolean kaynnissa;

    public Tekstikayttoliittyma(ViiteKasittelija vk, Validoija validaattori, IO io) {
        kaynnissa = true;
        this.viiteKasittelija = vk;
        this.validaattori = validaattori;
        this.io = io;
    }

    @Override
    public void run() {
        listaaKomennot();
        String komento;

        while (kaynnissa) {
            komento = kysyKomento();
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
                io.tulosta("Kentän arvo ei saa olla tyhjä!\n");
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
        //Se on ruma mutta toimii. korjatkaa toki....
        io.tulosta("Luodaan uusi viite.\n");
        Viite uusi = new Viite();
        io.tulosta("Tähdellä(*) merkityt kentät ovat pakollisia.\n");

        //Hommataan nimi
        uusi.setNimi(hankiValidiSyöte("nimi", true));

        //Hommataan typpi      
        uusi.setTyyppi(ViiteTyyppi.valueOf(hankiValidiSyöte("tyyppi", true)));

        //Määritä mahdollinen ristiviittaus
        String crossref = hankiValidiSyöte("crossref", false);
        uusi.setAttribuutti("crossref", crossref);

        //Teen kaiken tästä eteenpäin paremmin jossain vaiheessa -marktuom
        //Kysellään kutakin attribuuttia vastaava arvo ja mapitetaan ne.
        Map<String, String> pakollisetArvot
                = uusi.getTyyppi().getPakolliset().stream()//Tehdään stream pakollisita AttrTyypeistä
                .map(s -> s.name())//vaihdetaan AttrTyypit vastaaviin Stringeihin
                .sorted()//Laitetaan aakkosjärjestykseen
                .collect(Collectors.toMap(s -> s, s -> hankiValidiSyöte(s, onPakollinen(s, crossref)))); // Kerätään mapiksi, jos crossfer kohde määritelty ja sisältää arvon niin sitä ei tarvitse syöttää.

        //Asetetaan arvot        
        pakollisetArvot.keySet().stream()
                .forEach(s -> uusi.setAttribuutti(s, pakollisetArvot.get(s)));

        //sama valinnaisille... parempi ratkaisu lienee olemassa mutta slack :3
        Map<String, String> valinnaisetArvot
                = uusi.getTyyppi().getValinnaiset().stream()
                .map(s -> s.name())
                .sorted()
                .collect(Collectors.toMap(s -> s, s -> hankiValidiSyöte(s, false)));

        valinnaisetArvot.keySet().stream()
                .forEach(s -> uusi.setAttribuutti(s, valinnaisetArvot.get(s)));

        viiteKasittelija.lisaaViite(uusi);
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
     * määritelty ristiviitattavassa viitteessä. TODO Tämän toiminnallisuuden
     * voisi siirtää myös viitteen metodiksi.
     *
     * @param attribuutti Attribuutti, jota etsitään viitattavasta luokasta
     * @param crossref Viitteen nimi jo ristiviittaus kohdistuu
     * @return False jos attribuutti on määritetty. Muulloin false.
     */
    protected boolean onPakollinen(String attribuutti, String crossref) {
        return crossref.equals("") ? true : viiteKasittelija.haeViite(crossref).getAttribuutti(attribuutti) == null || viiteKasittelija.haeViite(crossref).getAttribuutti(attribuutti).getArvo().equals("");
    }
}
