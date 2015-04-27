package hammentyneethaxaajat.viiteapuri.UI.toiminnot;
import hammentyneethaxaajat.viiteapuri.IO.IO;
import hammentyneethaxaajat.viiteapuri.viite.AttrTyyppi;
import hammentyneethaxaajat.viiteapuri.viite.Viite;
import hammentyneethaxaajat.viiteapuri.viite.ViiteKasittelija;
import java.util.Collection;
import static hammentyneethaxaajat.viiteapuri.resurssit.Tulosteet.*;
import hammentyneethaxaajat.viiteapuri.validaattori.Validoija;
import java.util.stream.Collectors;

public abstract class Toiminto {
    protected IO io;
    protected ViiteKasittelija viiteKasittelija;
    protected Validoija validaattori;
    
    

    public Toiminto(IO io, ViiteKasittelija viiteKasittelija, Validoija validaattori) {
        this.io = io;
        this.viiteKasittelija = viiteKasittelija;
        this.validaattori = validaattori;
        
    }
    
    /**
     * Suorittaa komennon.
     */
    public abstract void suorita();
    
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
     * Kertoo onko parametrina saatava attribuutti pakollinen parametrina
     * saatavalle viittelle.
     *
     * @param viite Viite jolle attribuutti kuuluu.
     * @param attr Attribuutti jonka pakollisuus tarkistetaan.
     * @return
     */
    protected boolean attribuutinPakollisuus(Viite viite, String attr) {
        return viiteKasittelija.pakollisetAttribuutit(viite).stream().anyMatch(a -> a.name().equals(attr));
    }
    
    /**
     * Tulostaa yhden viitteen tiedot käyttäjän nähtäväksi.
     *
     * @param viite Viite jonka tiedot tulostetaan käyttäjän nähtäviksi.
     */
    protected void tulostaViitteenTiedot(Viite viite) {
        io.tulosta("\nViitteen tiedot:\n" + viite.listaus());
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
     * Tulostaa parametrina saatavan virheen viestin.
     *
     * @param virhe Virhe jonka viesti tulostetaan.
     */
    protected void tulostaVirheIlmoitus(Exception virhe) {
        io.tulosta(virhe.getMessage());
    }
    
    /**
     * Kertoo onko ohjelmassa yhtäkään viitettä
     *
     * @return True jos ohjelma sisältää viitteitä, muulloin false.
     */
    protected boolean ohjelmassaViitteita() {
        return !viiteKasittelija.getViitteet().isEmpty();
    }
   
    /**
     * Tulostaa Listan kaikista ohjelmaan syötetyista viitteistä (vain nimet)
     * sekä mahdollisesti Viestin, joka edeltää listausta.
     *
     * @param viesti String joka sisältää viiteiden listausta edeltävän viestin.
     */
    protected void listaaViitteet(String viesti) {
        io.tulosta(viiteKasittelija.getViitteet().stream()
                .map(s -> s.getBibtexAvain())
                .collect(Collectors.joining(", ", viesti, "\n")));
    }
    
    /**
     * Palauttaa käyttäjän syötettä vastaavan viitteen.
     *
     * @return Viite jonka nimi vastaa käyttäjän syötettä.
     */
    protected Viite haeViiteKayttajanSyotteenPerusteella() {
        return viiteKasittelija.haeViite(hankiValidiSyote(VIITE, false));
    }
}
