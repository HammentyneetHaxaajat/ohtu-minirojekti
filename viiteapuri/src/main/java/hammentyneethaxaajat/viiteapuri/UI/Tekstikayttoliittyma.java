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
        io.tulosta(teksti + ":\n");
        return io.seuraavaRivi();
    }

    protected String kysyKomento() {
        return kysele("Syötä komento");
    }

    /**
     * Kysyy käyttäjältä syötettä kunnes annettu syöte läpäisee testit.
     *
     * @param nimi
     * @param epatyhja
     * @return
     */
    protected String hankiValidiSyöte(String nimi, boolean epatyhja) {
        while (true) {
            String syote = kysele(nimi);

            if (epatyhja && syote.trim().equals("")) {
                io.tulosta("Kentän arvo ei saa olla tyhjä!\n");
                continue;
            }

            try {
                validaattori.validoi(nimi, syote);
                return syote;
            } catch (IllegalArgumentException e) {
                io.tulosta(e.getMessage());
            }
        }
    }

    protected void suoritaToiminto(String komento) {
        switch (komento) {
            case "uusi":
                uusiViite();
                break;
            case "listaa":
                listaaViitteet();
                break;
            case "bibtex":
                tulostaBibtex();
                break;
            case "lopeta":
                kaynnissa = false;
                break;
            default:
                io.tulosta("Tuntematon komento. ");
                listaaKomennot();
                break;
        }
    }

    protected void uusiViite() {
        //Se on ruma mutta toimii. korjatkaa toki....
        io.tulosta("Luodaan uusi viite.\n");
        Viite uusi = new Viite();

        io.tulosta("Seuraavat arvot ovat pakollisia, joten kenttiä ei voi jättää tyhjiksi.\n");
        //Hommataan nimi
        uusi.setNimi(hankiValidiSyöte("nimi", true));
        //Hommataan typpi      
        uusi.setTyyppi(ViiteTyyppi.valueOf(hankiValidiSyöte("tyyppi", true)));
        //Kysellään kutakin attribuuttia vastaava arvo ja mapitetaan ne.
        Map<String, String> pakollisetArvot = uusi.getTyyppi().getPakolliset().stream().collect(Collectors.toMap(s -> s.name(), s -> hankiValidiSyöte(s.name(), true)));
        //Asetetaan arvot.
        pakollisetArvot.keySet().stream().forEach(s -> uusi.setAttribuutti(s, pakollisetArvot.get(s)));
        //sama valinnaisille... parempi ratkaisu lienee olemassa mutta slack :3
        io.tulosta("Loput arvoista ovat valinnaisia. Kentät voi jättää tyhjäksi.\n");
        Map<String, String> valinnaisetArvot = uusi.getTyyppi().getValinnaiset().stream().collect(Collectors.toMap(s -> s.name(), s -> hankiValidiSyöte(s.name(), false)));
        valinnaisetArvot.keySet().stream().forEach(s -> uusi.setAttribuutti(s, valinnaisetArvot.get(s)));

        viiteKasittelija.lisaaViite(uusi);
        io.tulosta("Viite lisätty onnistuneesti!\n");
    }

    protected void listaaViitteet() {
        io.tulosta(viiteKasittelija.viitteetListauksena());
    }

    protected void tulostaBibtex() {
        io.tulosta(viiteKasittelija.viitteetBibtexina());
    }

    protected void listaaKomennot() {
        io.tulosta("Tuetut komennot: uusi, listaa, bibtex, lopeta.\n");
    }
}
