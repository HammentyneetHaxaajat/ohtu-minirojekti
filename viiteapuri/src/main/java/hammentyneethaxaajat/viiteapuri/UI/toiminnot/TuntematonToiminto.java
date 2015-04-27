package hammentyneethaxaajat.viiteapuri.UI.toiminnot;

import hammentyneethaxaajat.viiteapuri.IO.IO;
import static hammentyneethaxaajat.viiteapuri.resurssit.Tulosteet.*;
import hammentyneethaxaajat.viiteapuri.validaattori.Validoija;
import hammentyneethaxaajat.viiteapuri.viite.ViiteKasittelija;

class TuntematonToiminto extends Toiminto {
    
    public TuntematonToiminto(IO io, ViiteKasittelija kasittelija, Validoija validaattori) {
        super(io, kasittelija, validaattori);
    }

    @Override
    public void suorita() {
        io.tulosta(TUNTEMATON_KOMENTO);
    }

    @Override
    public String toString() {
        return TUNTEMATON_KOMENTO;
    }
    
}
