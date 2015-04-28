package hammentyneethaxaajat.viiteapuri.UI.toiminnot;

import hammentyneethaxaajat.viiteapuri.IO.IO;
import static hammentyneethaxaajat.viiteapuri.resurssit.Tulosteet.*;
import hammentyneethaxaajat.viiteapuri.validaattori.Validoija;

import hammentyneethaxaajat.viiteapuri.viite.AttrTyyppi;
import hammentyneethaxaajat.viiteapuri.viite.Viite;
import hammentyneethaxaajat.viiteapuri.viite.ViiteKasittelija;
import hammentyneethaxaajat.viiteapuri.viite.ViiteTyyppi;



public class UusiViite extends Toiminto{

    public UusiViite(IO io, ViiteKasittelija viiteKasittelija, Validoija validaattori) {
        super(io, viiteKasittelija, validaattori);
    }
    
    
    @Override
    public void suorita() {
        io.tulosta(UUDEN_VIITTEEN_LUONTI);
        Viite uusiViite = new Viite(ViiteTyyppi.valueOf(hankiValidiSyote(TYYPPI, true)));      
        uusiViite.setAttribuutti(AttrTyyppi.crossref.name(), hankiValidiSyote(AttrTyyppi.crossref.name(), false));

        hankiJaAsetaAttribuuttienArvot(uusiViite.getTyyppi().getPakolliset(), uusiViite);
        hankiJaAsetaAttribuuttienArvot(uusiViite.getTyyppi().getValinnaiset(), uusiViite);
        
        uusiViite.setBibtexAvain(hankiValidiSyote(BIBTEXAVAIN, false));

        //Alempi toimii yksin kahden ylemmän sijaan. Rikkoo tosin testit koska ne antavat vastaukset kiinteässä järjestyksessä.
//        hankiJaAsetaAttribuuttienArvot(uusiViite.getTyyppi().getKaikki(), uusiViite);
        viiteKasittelija.lisaaViite(uusiViite);
        tulostaViitteenTiedot(uusiViite);
        io.tulosta('\n' + VIITE_LISATTY_ONNISTUNEESTI);
    }

    @Override
    public String toString() {
        return KOMENTO_UUSI;
    }
    
}
