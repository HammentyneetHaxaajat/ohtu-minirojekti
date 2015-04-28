package hammentyneethaxaajat.viiteapuri.UI.toiminnot;

import hammentyneethaxaajat.viiteapuri.IO.BibtexIO;
import hammentyneethaxaajat.viiteapuri.IO.IO;
import static hammentyneethaxaajat.viiteapuri.resurssit.Tulosteet.*;
import hammentyneethaxaajat.viiteapuri.validaattori.Validoija;
import hammentyneethaxaajat.viiteapuri.viite.Viite;
import hammentyneethaxaajat.viiteapuri.viite.ViiteKasittelija;
import java.io.IOException;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

public class tuoBibtex extends Toiminto {

    private BibtexIO tiedostokasittelija;

    public tuoBibtex(IO io, ViiteKasittelija viiteKasittelija, Validoija validaattori, BibtexIO tiedostokasittelija) {
        super(io, viiteKasittelija, validaattori);
        this.tiedostokasittelija = tiedostokasittelija;
    }

    @Override
    public void suorita() {
        String polku = kysele(KYSY_TIEDOSTO_POLKU);
        int hylatyt = 0;
        try {
            Collection<Viite> lisattavat = tiedostokasittelija.haeViitteetTiedostosta(polku);
            io.tulosta(VIESTI_TUODUT_VIITTEET);
            for (Viite lisattava : lisattavat) {
                if (ValidoiViitteenAttribuutit(lisattava)) {
                    io.tulosta(lisattava.listaus(false) + "\n");
                    viiteKasittelija.lisaaViite(lisattava);
                } else {
                    hylatyt++;
                }
            }
            io.tulosta(TIEDOSTONTUONTI_ONNISTUI + (hylatyt > 0 ? VIRHE_VIITE_HYLATTIIN + hylatyt + "\n" : ""));
        } catch (IOException ex) {
            io.tulosta(ex.getMessage());
        }
    }

    private boolean ValidoiViitteenAttribuutit(Viite viite) {
        try {
            viite.getAttribuutit().values().stream().forEach(attribuutti -> validaattori.validoi(viite, attribuutti.getTyyppi().name(), attribuutti.getArvo()));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String toString() {
        return KOMENTO_TUO_BIBTEX;
    }

}
