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
     * Käynnistää ohjelman suorituksen. Suoritusta jatketaan kunnes käyttäjä
     * sulkee ohjelman.
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
     * Kysyy käyttäjältä seuraavaksi suoritettavan komennon.
     *
     * @return käyttäjän antama syöte.
     */
    protected String kysyKomento() {
        return kysele(SYOTA_KOMENTO);
    }

    /**
     * Kysyy käyttäjältä syötettä kunnes annetaan kelvollinen syöte.
     *
     * @param nimi Kysyttävän arvon nimi.
     * @param epatyhja Tieto siitä näytetäänkö käyttäjälle merkki joka ilmaisee
     * kentän pakollisuutta.
     * @return käyttäjän antama validoitu syöte.
     */
    protected String hankiValidiSyote(String nimi, boolean epatyhja) {
        while (true) {
            String syote = kysele(nimi + (epatyhja ? "*" : ""));

            try {
                validaattori.validoi(nimi, syote);
                return syote;

            } catch (IllegalArgumentException e) {
                tulostaVirheIlmoitus(e);
            }
        }
    }

    /**
     * Kysyy käyttäjältä syötettä kunnes annetaan kelvollinen syöte.
     *
     * @param viite Viite jolle kysytty arvo tulee
     * @param attr Attribuutti jonka arvo halutaan
     * @param epatyhja Tieto siitä näytetäänkö käyttäjälle merkki joka ilmaisee
     * kentän pakollisuutta.
     * @return käyttäjän antama validoitu syöte.
     */
    protected String hankiValidiSyote(Viite viite, String attr, boolean epatyhja) {
        while (true) {
            String syote = kysele(attr + (epatyhja ? "*" : ""));

            try {
                validaattori.validoi(viite, attr, syote);
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
            case KOMENTO_UUSI:
                uusiViite();
                break;
            case KOMENTO_LISTAA:
                listaaViitteidenTiedot();
                break;
            case KOMENTO_MUOKKAA:
                muokkaaViitetta();
                break;
            case KOMENTO_POISTA:
                poistaViite();
                break;
            case KOMENTO_BIBTEX:
                luoBibtex();
                break;
            case KOMENTO_LOPETA:
                io.tulosta(VIESTI_HEIHEI);
                kaynnissa = false;
                break;
            default:
                io.tulosta(TUNTEMATON_KOMENTO);
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

        uusiViite.setNimi(hankiValidiSyote(NIMI, true));
        uusiViite.setTyyppi(ViiteTyyppi.valueOf(hankiValidiSyote(TYYPPI, true)));
        uusiViite.setAttribuutti(AttrTyyppi.crossref.name(), hankiValidiSyote(AttrTyyppi.crossref.name(), false));

        hankiJaAsetaAttribuuttienArvot(uusiViite.getTyyppi().getPakolliset(), uusiViite);
        hankiJaAsetaAttribuuttienArvot(uusiViite.getTyyppi().getValinnaiset(), uusiViite);

        //Alempi toimii yksin kahden ylemmän sijaan. Rikkoo tosin testit koska ne antavat vastaukset kiinteässä järjestyksessä.
//        hankiJaAsetaAttribuuttienArvot(uusiViite.getTyyppi().getKaikki(), uusiViite);
        viiteKasittelija.lisaaViite(uusiViite);
        tulostaViitteenTiedot(uusiViite);
        io.tulosta('\n' + VIITE_LISATTY_ONNISTUNEESTI);
    }

    /**
     * Hankkii käyttäjältä kullekin attribuutille kelvollisen arvon ja asettaa
     * sen parametrina saadulle viitteelle
     *
     * @param tyypit Käyttäjältä kyseltävät Attribuutit
     * @param viite Viite jolle attribuuttien arvot asetetaan
     */
    protected void hankiJaAsetaAttribuuttienArvot(Collection<AttrTyyppi> tyypit, Viite viite) {
        tyypit.stream()
                .map(attr -> attr.name())//vaihdetaan AttrTyypit vastaaviin Stringeihin
                .sorted()//Laitetaan aakkosjärjestykseen
                .forEach(attr -> viite.setAttribuutti(attr, hankiValidiSyote(viite, attr, attribuutinPakollisuus(viite, attr))));
    }

    /**
     * Tulostaa selkokielisen listan ohjelman sisältämistä viitteistä ja niiden
     * sisältämistä arvoista.
     */
    protected void listaaViitteidenTiedot() {
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
        String polku = ""; // = hankiValidiSyote(KYSY_TIEDOSTO_POLKU, false);

        if (nimi.isEmpty()) {
            nimi = "viitteet";
        }
        if (polku.matches("")) {
            polku = "";
        }

        try {
            tiedostonKasittelija.kirjoitaBibtex(viiteKasittelija.viitteetBibtexina(), polku + nimi);
            io.tulosta(TIEDOSTONLUONTI_ONNISTUI + nimi + "\n");
        } catch (IOException e) {
            io.tulosta(TIEDOSTONLUONTI_EI_ONNISTUNUT);
        }
        if (!ohjelmassaViitteita()) {
            io.tulosta("Huom! Luotu tiedosto ei sisällä yhtään viitettä.\n");
        }
    }

    /**
     * Ohjattu viitteenmuokkastoiminto. Antaa käyttäjän muokata olemassaolevia
     * viitteitä
     */
    private void muokkaaViitetta() {
        if (!ohjelmassaViitteita()) {
            io.tulosta(VIRHE_EIVIITTEITA);
        } else {
            listaaViitteet("Muokattavissa olevat viitteet: ");

            Viite viite = haeViiteKayttajanSyotteenPerusteella();
            tulostaViitteenTiedot(viite);

            String attribuutti = hankiValidiSyote(viite, ATTRIBUUTTI, false);

            io.tulosta("Uusi arvo kentälle ");
            String uusiArvo = hankiValidiSyote(viite, attribuutti, attribuutinPakollisuus(viite, attribuutti));

            //Jos vaihdetaan nimi niin päivitetään viitaukset.
            if (attribuutti.equals(NIMI)) {
                //TODO tämä säädön voisi siirtää viitekäsittelijän metodiksi. paivitaNimi(Viite viite, String nimi).
                viiteKasittelija.viittaavatViitteet(viite).stream().forEach(s -> s.setAttribuutti(AttrTyyppi.crossref.name(), uusiArvo));
                viiteKasittelija.poistaViite(viite);
                viite.setNimi(uusiArvo);
                viiteKasittelija.lisaaViite(viite);
            } 
//                else if(attribuutti.equals(CROSSREF)){
            //                //TODO Tähän voi määrittää toiminnan kun/jos crossref muutetaan. Muista päivittää myös validaattorin vastaava toiminta.
            //            } 
                else {
                viite.setAttribuutti(attribuutti, uusiArvo);
            }
            tulostaViitteenTiedot(viite);
            io.tulosta("Kentän arvo päivitetty onnistuneesti.\n");
        }
    }

    /**
     * Ohjattu viitten poistamistoiminto
     */
    private void poistaViite() {
        if (!ohjelmassaViitteita()) {
            io.tulosta(VIRHE_EIVIITTEITA);
        } else {
            listaaViitteet("Poistettavissa olevat viitteet: ");

            Viite viite = haeViiteKayttajanSyotteenPerusteella();
            tulostaViitteenTiedot(viite);

            String varmistus = kysele(KYSY_VARMISTUS + VARMISTA_POISTO);
            if (varmistus.equals(VARMISTA_POISTO)) {
                viiteKasittelija.poistaViite(viite);
                io.tulosta("Viite poistettiin onnistuneesti.\n");
                //Tekee tarvittavat muutokset viitteisiin joiden crossref oli poistettu viite. 
                if (!viiteKasittelija.viittaavatViitteet(viite).isEmpty()) {
                    viiteKasittelija.viittaavatViitteet(viite).stream()
                            .forEach(s -> {
                                viite.getAttribuutit().values().stream()
                                .filter(a -> s.getTyyppi().getPakolliset().contains(a.getTyyppi())
                                        && s.getAttribuutti(a.getTyyppi().name()).getArvo().equals(""))
                                .forEach(a -> s.setAttribuutti(a.getTyyppi().name(), a.getArvo()));
                                s.setAttribuutti(AttrTyyppi.crossref.name(), "");
                            });
                    io.tulosta("Ohjelman teki muutoksia viiteisiin, joiden crossref kenttä viittasi poistettuun viitteeseen.\n");
                }
            } else {
                io.tulosta("Viitettä ei poistettu.\n");
            }
        }

    }

    /**
     * Kertoo onko ohjelmassa yhtäkään viitettä
     *
     * @return True jos ohjelma sisältää viitteitä, muulloin false.
     */
    private boolean ohjelmassaViitteita() {
        return !viiteKasittelija.getViitteet().isEmpty();
    }

    /**
     * Tulostaa Listan kaikista ohjelmaan syötetyista viitteistä (vain nimet)
     * sekä mahdollisesti Viestin, joka edeltää listausta.
     *
     * @param viesti String joka sisältää viiteiden listausta edeltävän viestin.
     */
    private void listaaViitteet(String viesti) {
        io.tulosta(viiteKasittelija.getViitteet().stream()
                .map(s -> s.getNimi())
                .collect(Collectors.joining(", ", viesti, "\n")));
    }

    /**
     * Tulostaa yhden viitteen tiedot käyttäjän nähtäväksi.
     *
     * @param viite Viite jonka tiedot tulostetaan käyttäjän nähtäviksi.
     */
    private void tulostaViitteenTiedot(Viite viite) {
        io.tulosta("\nViitteen tiedot:\n" + viite.listaus());
    }

    /**
     * Tulostaa parametrina saatavan virheen viestin.
     *
     * @param virhe Virhe jonka viesti tulostetaan.
     */
    private void tulostaVirheIlmoitus(Exception virhe) {
        io.tulosta(virhe.getMessage());
    }

    /**
     * Palauttaa käyttäjän syötettä vastaavan viitteen.
     *
     * @return Viite jonka nimi vastaa käyttäjän syötettä.
     */
    private Viite haeViiteKayttajanSyotteenPerusteella() {
        return viiteKasittelija.haeViite(hankiValidiSyote(POISTETTAVA_VIITE, false));
    }

    /**
     * Kertoo onko parametrina saatava attribuutti pakollinen parametrina
     * saatavalle viittelle.
     *
     * @param viite Viite jolle attribuutti kuuluu.
     * @param attr Attribuutti jonka pakollisuus tarkistetaan.
     * @return
     */
    private boolean attribuutinPakollisuus(Viite viite, String attr) {
        return viiteKasittelija.pakollisetAttribuutit(viite).stream().anyMatch(a -> a.name().equals(attr));
    }

    /**
     * Listaa ohjelmassa tuetut komennot.
     */
    protected void listaaKomennot() {
        io.tulosta(TUETUT_KOMENNOT);
    }
}
