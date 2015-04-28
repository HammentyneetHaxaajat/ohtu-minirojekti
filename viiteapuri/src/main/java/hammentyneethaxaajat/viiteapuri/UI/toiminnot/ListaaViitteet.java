package hammentyneethaxaajat.viiteapuri.UI.toiminnot;

import hammentyneethaxaajat.viiteapuri.IO.IO;
import hammentyneethaxaajat.viiteapuri.resurssit.Tulosteet;
import static hammentyneethaxaajat.viiteapuri.resurssit.Tulosteet.*;
import hammentyneethaxaajat.viiteapuri.validaattori.Validoija;
import hammentyneethaxaajat.viiteapuri.viite.ViiteKasittelija;
/**
 * Viitteiden listaus komento.
 */
class ListaaViitteet extends Toiminto {

    public ListaaViitteet(IO io, ViiteKasittelija viitekasittelija, Validoija validaattori) {
        super(io, viitekasittelija, validaattori);
    }

    /**
     * Listaa kaikki ohjemaan lisätyt viitteet
     */
    @Override
    public void suorita() {
        if (!ohjelmassaViitteita()) {
            io.tulosta(VIRHE_EIVIITTEITA);
        } else {
            io.tulosta(viiteKasittelija.viitteetListauksena());
        }
    }
    
    
    @Override
    public String toString() {
        return Tulosteet.KOMENTO_UUSI;
    }
}
