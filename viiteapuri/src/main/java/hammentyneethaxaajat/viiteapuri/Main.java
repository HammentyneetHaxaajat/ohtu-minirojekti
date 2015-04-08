package hammentyneethaxaajat.viiteapuri;

import hammentyneethaxaajat.viiteapuri.UI.Tekstikayttoliittyma;
import hammentyneethaxaajat.viiteapuri.validaattori.Validaattori;
import hammentyneethaxaajat.viiteapuri.viite.ViiteKasittelija;
import hammentyneethaxaajat.viiteapuri.IO.KomentoriviIO;
import hammentyneethaxaajat.viiteapuri.IO.IO;

public class Main {

    /**
     * @param args the command line arguments
     */
    
    public static void main(String[] args) {
        
        ViiteKasittelija viiteKasittelija = new ViiteKasittelija();
        Validaattori validaattori = new Validaattori(viiteKasittelija);
        IO io = new KomentoriviIO();
        new Tekstikayttoliittyma(viiteKasittelija, validaattori, io).run();
    }
}