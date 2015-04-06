package hammentyneethaxaajat.viiteapuri;

import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 *
 * @author Markus
 */
public class Tekstikayttoliittyma implements Runnable {

    private ViiteKasittelija viiteKasittelija;
    private Validaattori validaattori;
    private IO io;
    private boolean kaynnissa;

    public Tekstikayttoliittyma(ViiteKasittelija vk, Validaattori validaattori, IO io) {
        kaynnissa = true;
        this.viiteKasittelija = vk;
        this.validaattori = validaattori;
        this.io = io;
    }

    public Tekstikayttoliittyma(IO io) {
        this(new ViiteKasittelija(), new Validaattori(), io);
    }

    public Tekstikayttoliittyma() {
        this(new ViiteKasittelija(), new Validaattori(), new KomentoriviIO());
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
     * @param kysymys Käyttäjälle esitettävä tuloste.
     * @return Käyttäjän antama syöte.
     */
    private String kysele(String kysymys) {
        io.tulostaRivi(kysymys);
        return io.seuraavaRivi();
    }

    private String kysyKomento() {
        return kysele("Syötä komento:");
    }

    //TODO hae predikaatit stringin perusteella vasta myöhemmin?
    /**
     * Kysyy käyttäjältä syötettä kunnes annettu syöte läpäisee testit.
     * @param kysymys
     * @param testit
     * @return 
     */
    private String hankiValidiSyöte(String kysymys, Predicate<String>... testit) {
        String syöte = "";
        while (true) {
            syöte = kysele(kysymys);
            if (validaattori.validoi(syöte, testit)) {
                break;
            } else {
                //TODO paranna virheen tulostusta
                io.tulostaRivi("Virheellinen syöte");
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
                io.tulostaRivi("Tuntematon komento. ");
                listaaKomennot();
                break;
        }
    }

    private void uusiViite() {
        //Se on ruma mutta toimii. korjatkaa toki....
        io.tulostaRivi("Luodaan uusi viite.");
        Viite uusi = new Viite();

        io.tulostaRivi("Seuraavat arvot ovat pakollisia, joten kenttiä ei voi jättää tyhjiksi.");
        //Hommataan nimi
        uusi.setNimi(hankiValidiSyöte("Nimi: ", s -> !s.isEmpty()));
        //Hommataan typpi
        uusi.setTyyppi(ViiteTyyppi.valueOf(hankiValidiSyöte("Tyyppi: (Vain book toimii...)", s -> validaattori.validoiViiteTyyppi(s))));
        //Kysellään kutakin attribuuttia vastaava arvo ja mapitetaan ne.
        Map<String, String> pArvot = uusi.getTyyppi().getPakolliset().stream().collect(Collectors.toMap(s -> s.name(), s -> hankiValidiSyöte(s.name() + ":", a -> !a.isEmpty(), a -> a.matches(s.getMuoto()))));
        //Asetetaan arvot.
        pArvot.keySet().stream().forEach(s -> uusi.setAttribuutti(s, pArvot.get(s)));
        //sama valinnaisille... parempi ratkaisu lienee olemassa mutta slack :3
        io.tulostaRivi("Loput arvoista ovat valinnaisia. Kentän voi jättää tyhjäksi.");
        Map<String, String> vArvot = uusi.getTyyppi().getValinnaiset().stream().collect(Collectors.toMap(s -> s.name(), s -> hankiValidiSyöte(s.name() + ":", a -> a.matches(s.getMuoto()))));
        vArvot.keySet().stream().forEach(s -> uusi.setAttribuutti(s, vArvot.get(s)));

        viiteKasittelija.lisaaViite(uusi);
        io.tulostaRivi("Viite lisätty onnistuneesti!\n");
    }

    private void listaaViitteet() {
        io.tulostaRivi(viiteKasittelija.viitteetListauksena());
    }

    private void tulostaibtex() {
        io.tulostaRivi(viiteKasittelija.viitteetBibtexina());
    }

    private void listaaKomennot() {
        io.tulostaRivi("Tuetut komennot: uusi, listaa, bibtex, lopeta.");
    }

}
