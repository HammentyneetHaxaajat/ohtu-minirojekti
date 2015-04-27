package hammentyneethaxaajat.viiteapuri.UI.toiminnot;

import hammentyneethaxaajat.viiteapuri.IO.IO;
import hammentyneethaxaajat.viiteapuri.resurssit.Tulosteet;
import static hammentyneethaxaajat.viiteapuri.resurssit.Tulosteet.*;
import hammentyneethaxaajat.viiteapuri.validaattori.Validoija;
import hammentyneethaxaajat.viiteapuri.viite.AttrTyyppi;
import hammentyneethaxaajat.viiteapuri.viite.Viite;
import hammentyneethaxaajat.viiteapuri.viite.ViiteKasittelija;

class MuokkaaViitetta extends Toiminto {

    public MuokkaaViitetta(IO io, ViiteKasittelija viitekasittelija, Validoija validaattori) {
        super(io, viitekasittelija, validaattori);
    }

    @Override
    public void suorita() {
        if (!ohjelmassaViitteita()) {
            io.tulosta(VIRHE_EIVIITTEITA);
        } else {
            listaaViitteet("Muokattavissa olevat viitteet: ");

            Viite viite = haeViiteKayttajanSyotteenPerusteella();
            tulostaViitteenTiedot(viite);

            String attribuutti = hankiValidiSyote(viite, ATTRIBUUTTI, false);

            io.tulosta("Uusi arvo kentälle ");
            String uusiArvo = hankiValidiSyote(viite, attribuutti, attribuutinPakollisuus(viite, attribuutti));

            //Jos vaihdetaan nimi niin päivitetään viitaukset.
            if (attribuutti.equals(BIBTEXAVAIN)) {
                //TODO tämä säädön voisi siirtää viitekäsittelijän metodiksi. paivitaNimi(Viite viite, String nimi).
                viiteKasittelija.viittaavatViitteet(viite).stream().forEach(s -> s.setAttribuutti(AttrTyyppi.crossref.name(), uusiArvo));
                viiteKasittelija.poistaViite(viite);
                viite.setBibtexAvain(uusiArvo);
                viiteKasittelija.lisaaViite(viite);
            } 
//                else if(attribuutti.equals(CROSSREF)){
            //                //TODO Tähän voi määrittää toiminnan kun/jos crossref muutetaan. Muista päivittää myös validaattorin vastaava toiminta.
            //            } 
                else {
                viite.setAttribuutti(attribuutti, uusiArvo);
            }
            tulostaViitteenTiedot(viite);
            io.tulosta("Kentän arvo päivitetty onnistuneesti.\n");
        }
    }
    
    
    public String toString() {
        return Tulosteet.KOMENTO_MUOKKAA;
    }
}
