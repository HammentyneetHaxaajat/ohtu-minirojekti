package hammentyneethaxaajat.viiteapuri.UI.toiminnot;

import hammentyneethaxaajat.viiteapuri.IO.IO;
import hammentyneethaxaajat.viiteapuri.resurssit.Tulosteet;
import static hammentyneethaxaajat.viiteapuri.resurssit.Tulosteet.*;
import hammentyneethaxaajat.viiteapuri.validaattori.Validoija;
import hammentyneethaxaajat.viiteapuri.viite.AttrTyyppi;
import hammentyneethaxaajat.viiteapuri.viite.Viite;
import hammentyneethaxaajat.viiteapuri.viite.ViiteKasittelija;

/**
 * Toiminto viitteen muokkaamiseen
 */
class MuokkaaViitetta extends Toiminto {

    public MuokkaaViitetta(IO io, ViiteKasittelija viitekasittelija, Validoija validaattori) {
        super(io, viitekasittelija, validaattori);
    }

    /**
     * Listaa muokattavissa olevat viitteet ja suorittaa muokkaustoiminnot.
     */
    @Override
    public void suorita() {
        if (!ohjelmassaViitteita()) {
            io.tulosta(VIRHE_EIVIITTEITA);
        } else {
            listaaViitteet(MUOKATTAVISSA_OLEVAT_VIITTEET);
            Viite viite = haeViiteKayttajanSyotteenPerusteella();
            tulostaViitteenTiedot(viite);
            String attribuutti = hankiValidiSyote(viite, ATTRIBUUTTI, false);
            io.tulosta(KYSY_UUSIARVO);
            String uusiArvo = hankiValidiSyote(viite, attribuutti, attribuutinPakollisuus(viite, attribuutti));
            viiteKasittelija.paivitaArvo(viite, attribuutti, uusiArvo);
            io.tulosta(KENTTA_PAIVITETTY);
            tulostaViitteenTiedot(viite);
        }
    }

    public String toString() {
        return Tulosteet.KOMENTO_MUOKKAA;
    }
}
