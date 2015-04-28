package hammentyneethaxaajat.viiteapuri.UI.toiminnot;

import hammentyneethaxaajat.viiteapuri.IO.IO;
import static hammentyneethaxaajat.viiteapuri.resurssit.Tulosteet.*;
import hammentyneethaxaajat.viiteapuri.validaattori.Validoija;
import hammentyneethaxaajat.viiteapuri.viite.Viite;
import hammentyneethaxaajat.viiteapuri.viite.ViiteKasittelija;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 *
 * @author Markus
 */
public class HaeViitteita extends Toiminto {

    public HaeViitteita(IO io, ViiteKasittelija viitekasittelija, Validoija validaattori) {
        super(io, viitekasittelija, validaattori);
    }

    @Override
    public void suorita() {
        if (!ohjelmassaViitteita()) {
            io.tulosta(VIRHE_EIVIITTEITA);
        } else {
            String attribuutti = hankiValidiSyote(ATTRIBUUTTI, false);
            String arvo = kysele("Haettava arvo");
            Collection<Viite> viitteet = viiteKasittelija.haeViitteet(attribuutti, arvo);
            if (viitteet.isEmpty()) {
                io.tulosta("Haulla ei löydetty yhtään viitettä.\n");
            } else {
                io.tulosta(viitteet.stream()
                .map(viite -> viite.listaus(false))
                .collect(Collectors.joining(VIIVA + "\n", "Ehdon toteuttavat viitteet:\n", VIIVA + "\n")));
            }
        }
    }

    @Override
    public String toString() {
        return KOMENTO_HAE_VIITTEITA;
    }

}
