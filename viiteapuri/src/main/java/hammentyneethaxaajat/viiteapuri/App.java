package hammentyneethaxaajat.viiteapuri;

import hammentyneethaxaajat.viiteapuri.UI.Tekstikayttoliittyma;
import hammentyneethaxaajat.viiteapuri.validaattori.Validaattori;
import hammentyneethaxaajat.viiteapuri.viite.ViiteKasittelija;
import hammentyneethaxaajat.viiteapuri.IO.KomentoriviIO;
import hammentyneethaxaajat.viiteapuri.IO.IO;

/**
 *
 * @author Hämmentyneet Haxaajat
 */
public class App {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        ViiteKasittelija vk = new ViiteKasittelija();
        Validaattori v = new Validaattori(vk);
        IO io = new KomentoriviIO();
        new Tekstikayttoliittyma(vk, v, io).run();
        
    }

}
