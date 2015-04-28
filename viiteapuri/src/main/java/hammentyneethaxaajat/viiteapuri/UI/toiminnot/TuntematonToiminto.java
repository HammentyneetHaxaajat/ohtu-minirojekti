package hammentyneethaxaajat.viiteapuri.UI.toiminnot;

import hammentyneethaxaajat.viiteapuri.IO.IO;
import static hammentyneethaxaajat.viiteapuri.resurssit.Tulosteet.*;
import hammentyneethaxaajat.viiteapuri.validaattori.Validoija;
import hammentyneethaxaajat.viiteapuri.viite.ViiteKasittelija;

/**
 * T
 */
class TuntematonToiminto extends Toiminto {
    
    public TuntematonToiminto(IO io, ViiteKasittelija kasittelija, Validoija validaattori) {
        super(io, kasittelija, validaattori);
    }

    /**
     * Tulostaa käyttäjälle tiedon tuntemattomasta komennosta
     */
    @Override
    public void suorita() {
        io.tulosta(TUNTEMATON_KOMENTO);
    }

    @Override
    public String toString() {
        return TUNTEMATON_KOMENTO;
    }
    
}
