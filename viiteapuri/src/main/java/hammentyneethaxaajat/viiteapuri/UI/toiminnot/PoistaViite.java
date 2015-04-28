package hammentyneethaxaajat.viiteapuri.UI.toiminnot;

import hammentyneethaxaajat.viiteapuri.IO.IO;
import hammentyneethaxaajat.viiteapuri.resurssit.Tulosteet;
import static hammentyneethaxaajat.viiteapuri.resurssit.Tulosteet.*;
import hammentyneethaxaajat.viiteapuri.validaattori.Validoija;
import hammentyneethaxaajat.viiteapuri.viite.AttrTyyppi;
import hammentyneethaxaajat.viiteapuri.viite.Viite;
import hammentyneethaxaajat.viiteapuri.viite.ViiteKasittelija;

/**
 * Toiminto viitteen poistamiseen.
 * @author matti
 */
class PoistaViite extends Toiminto {

    public PoistaViite(IO io, ViiteKasittelija viitekasittelija, Validoija validaattori) {
        super(io, viitekasittelija, validaattori);
    }

    @Override
    public void suorita() {
        if (!ohjelmassaViitteita()) {
            io.tulosta(VIRHE_EIVIITTEITA);
        } else {
            listaaViitteet(POISTETTAVISSA_OLEVAT_VIITTEET);

            Viite viite = haeViiteKayttajanSyotteenPerusteella();
            tulostaViitteenTiedot(viite);

            String varmistus = kysele(KYSY_VARMISTUS + VARMISTA_POISTO);
            if (varmistus.equals(VARMISTA_POISTO)) {
                viiteKasittelija.poistaViite(viite);
                io.tulosta("Viite poistettiin onnistuneesti.\n");
                //Tekee tarvittavat muutokset viitteisiin joiden crossref oli poistettu viite. 
                if (!viiteKasittelija.viittaavatViitteet(viite).isEmpty()) {
                    viiteKasittelija.viittaavatViitteet(viite).stream()
                            .forEach(s -> {
                                viite.getAttribuutit().values().stream()
                                .filter(a -> s.getTyyppi().getPakolliset().contains(a.getTyyppi())
                                        && s.getAttribuutti(a.getTyyppi().name()).getArvo().equals(""))
                                .forEach(a -> s.setAttribuutti(a.getTyyppi().name(), a.getArvo()));
                                s.setAttribuutti(AttrTyyppi.crossref.name(), "");
                            });
                    io.tulosta("Ohjelman teki muutoksia viiteisiin, joiden crossref kenttä viittasi poistettuun viitteeseen.\n");
                }
            } else {
                io.tulosta("Viitettä ei poistettu.\n");
            }
        }

    }
    
    
    public String toString() {
        return Tulosteet.KOMENTO_POISTA;
    }
}
