package hammentyneethaxaajat.viiteapuri.UI.toiminnot;

import hammentyneethaxaajat.viiteapuri.IO.BibtexIO;
import hammentyneethaxaajat.viiteapuri.IO.IO;
import hammentyneethaxaajat.viiteapuri.resurssit.Tulosteet;
import hammentyneethaxaajat.viiteapuri.validaattori.Validoija;
import hammentyneethaxaajat.viiteapuri.viite.Viite;
import hammentyneethaxaajat.viiteapuri.viite.ViiteKasittelija;
import java.io.IOException;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

public class tuoBibtex extends Toiminto{
    private BibtexIO tiedostokasittelija;
    
    public tuoBibtex(IO io, ViiteKasittelija viiteKasittelija, Validoija validaattori, BibtexIO tiedostokasittelija) {
        super(io, viiteKasittelija, validaattori);
        this.tiedostokasittelija = tiedostokasittelija;
    }

    @Override
    public void suorita() {
        String polku = io.lueRivi(Tulosteet.KYSY_TIEDOSTO_POLKU);
        
        try {
            Collection<Viite> lisattavat = tiedostokasittelija.haeViitteetTiedostosta(polku);
            for (Viite lisattava : lisattavat) {
                viiteKasittelija.lisaaViite(lisattava);
            }
        } catch (IOException ex) {
            io.tulosta(Tulosteet.TIEDOSTONLUKU_EI_ONNISTUNUT);            
        }
    }
    
}
