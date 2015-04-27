
package hammentyneethaxaajat.viiteapuri.UI.toiminnot;

import hammentyneethaxaajat.viiteapuri.IO.IO;
import hammentyneethaxaajat.viiteapuri.resurssit.Tulosteet;
import hammentyneethaxaajat.viiteapuri.validaattori.Validoija;
import hammentyneethaxaajat.viiteapuri.viite.ViiteKasittelija;

class Lopeta extends Toiminto {

    public Lopeta(IO io, ViiteKasittelija viitekasittelija, Validoija validaattori) {
        super(io, viitekasittelija, validaattori);
    }

    @Override
    public void suorita() {
        System.out.println(Tulosteet.VIESTI_HEIHEI);
        System.exit(0);       
    }
    
    
    public String toString() {
        return Tulosteet.KOMENTO_LOPETA;
    }
}
