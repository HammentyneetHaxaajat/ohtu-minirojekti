package hammentyneethaxaajat.viiteapuri.UI;

import hammentyneethaxaajat.viiteapuri.validaattori.Validoija;
import hammentyneethaxaajat.viiteapuri.viite.ViiteTyyppi;
import hammentyneethaxaajat.viiteapuri.viite.ViiteKasittelija;
import hammentyneethaxaajat.viiteapuri.viite.Viite;
import hammentyneethaxaajat.viiteapuri.IO.IO;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 *
 * @author Markus
 */
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
    private String kysele(String teksti) {
        io.tulosta(teksti + ":\n");
        return io.seuraavaRivi();
    }

    private String kysyKomento() {
        return kysele("Syötä komento");
    }

    /**
     * Kysyy käyttäjältä syötettä kunnes annettu syöte läpäisee testit.
     *
     * @param nimi
     * @param epatyhja
     * @return
     */
    private String hankiValidiSyöte(String nimi, Boolean epatyhja) {
        String syöte = "";
        while (true) {
            syöte = kysele(nimi);
            if (epatyhja && syöte.trim().equals("")) {
                io.tulosta("Kentän arvo ei saa olla tyhjä!\n");
                continue;
            }
            if (validaattori.validoi(nimi, syöte)) {
                break;
            } else {
                //TODO paranna virheen tulostusta, pitäisi varmaan käyttää validaattorin heittämiä exceptioneita.
                io.tulosta("Virheellinen syöte!\n");
            }
        }
        return syöte;
    }

    private void suoritaToiminto(String komento) {
        switch (komento) {
            case "uusi":
                uusiViite();
                break;
            case "listaa":
                listaaViitteet();
                break;
            case "bibtex":
                tulostaibtex();
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

    private void uusiViite() {
        //Se on ruma mutta toimii. korjatkaa toki....
        io.tulosta("Luodaan uusi viite.\n");
        Viite uusi = new Viite();

        io.tulosta("Seuraavat arvot ovat pakollisia, joten kenttiä ei voi jättää tyhjiksi.\n");
        //Hommataan nimi
        uusi.setNimi(hankiValidiSyöte("nimi", true));
        //Hommataan typpi
        uusi.setTyyppi(ViiteTyyppi.valueOf(hankiValidiSyöte("tyyppi", true)));
        //Kysellään kutakin attribuuttia vastaava arvo ja mapitetaan ne.
        Map<String, String> pArvot = uusi.getTyyppi().getPakolliset().stream().collect(Collectors.toMap(s -> s.name(), s -> hankiValidiSyöte(s.name(), true)));
        //Asetetaan arvot.
        pArvot.keySet().stream().forEach(s -> uusi.setAttribuutti(s, pArvot.get(s)));
        //sama valinnaisille... parempi ratkaisu lienee olemassa mutta slack :3
        io.tulosta("Loput arvoista ovat valinnaisia. Kentät voi jättää tyhjäksi.\n");
        Map<String, String> vArvot = uusi.getTyyppi().getValinnaiset().stream().collect(Collectors.toMap(s -> s.name(), s -> hankiValidiSyöte(s.name(), false)));
        vArvot.keySet().stream().forEach(s -> uusi.setAttribuutti(s, vArvot.get(s)));

        viiteKasittelija.lisaaViite(uusi);
        io.tulosta("Viite lisätty onnistuneesti!\n");
    }

    private void listaaViitteet() {
        io.tulosta(viiteKasittelija.viitteetListauksena());
    }

    private void tulostaibtex() {
        io.tulosta(viiteKasittelija.viitteetBibtexina());
    }

    private void listaaKomennot() {
        io.tulosta("Tuetut komennot: uusi, listaa, bibtex, lopeta.\n");
    }

}
