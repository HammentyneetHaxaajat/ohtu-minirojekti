package hammentyneethaxaajat.viiteapuri.UI;

import hammentyneethaxaajat.viiteapuri.IO.BibtexIO;
import hammentyneethaxaajat.viiteapuri.validaattori.Validoija;
import hammentyneethaxaajat.viiteapuri.viite.ViiteKasittelija;
import hammentyneethaxaajat.viiteapuri.IO.IO;
import hammentyneethaxaajat.viiteapuri.UI.toiminnot.Toimintotehdas;
import static hammentyneethaxaajat.viiteapuri.resurssit.Tulosteet.*;

/**
 * Tekstikäyttöliittymä, joka toimii ikään kuin "pääohjelmana". Käyttöliittymän
 * kautta kutsutaan muiden luokkien olioita tekemään osan ohjelman suorituksesta
 * puolestaan.
 */
public class Tekstikayttoliittyma implements Runnable {

    private Toimintotehdas toiminnot;
    private IO io;
    private boolean kaynnissa;

    public Tekstikayttoliittyma(ViiteKasittelija viiteKasittelija, Validoija validaattori, IO io) {
        this.io = io;
        toiminnot = new Toimintotehdas(this, io, viiteKasittelija, validaattori, new BibtexIO());
        kaynnissa = true;
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
     * Valitseen suoritettavan komennon parametrin perusteella.
     *
     * @param komennonTunniste Halutun komennon nimi tai numero.
     */
    protected void suoritaToiminto(String komennonTunniste) {
        toiminnot.suorita(komennonTunniste);
    }

    /**
     * Listaa ohjelmassa tuetut komennot.
     */
    protected void listaaKomennot() {
        io.tulosta(toiminnot.ohjeet());
    }
    
    public void abort() {
        kaynnissa = false;
    }
}
