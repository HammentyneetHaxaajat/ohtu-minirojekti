
package hammentyneethaxaajat.viiteapuri.UI.toiminnot;

import hammentyneethaxaajat.viiteapuri.IO.IO;
import hammentyneethaxaajat.viiteapuri.UI.Tekstikayttoliittyma;
import hammentyneethaxaajat.viiteapuri.resurssit.Tulosteet;
import hammentyneethaxaajat.viiteapuri.validaattori.Validoija;
import hammentyneethaxaajat.viiteapuri.viite.ViiteKasittelija;

class Lopeta extends Toiminto {
    Tekstikayttoliittyma ui;

    public Lopeta(IO io, ViiteKasittelija viitekasittelija, Validoija validaattori, Tekstikayttoliittyma ui) {
        super(io, viitekasittelija, validaattori);
        this.ui = ui;
    }

    @Override
    public void suorita() {
        io.tulosta(Tulosteet.VIESTI_HEIHEI);
        ui.abort();
    }
    
    
    public String toString() {
        return Tulosteet.KOMENTO_LOPETA;
    }
}
