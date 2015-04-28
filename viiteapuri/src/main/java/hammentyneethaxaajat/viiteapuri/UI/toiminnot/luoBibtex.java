package hammentyneethaxaajat.viiteapuri.UI.toiminnot;

import hammentyneethaxaajat.viiteapuri.IO.BibtexIO;
import hammentyneethaxaajat.viiteapuri.IO.IO;
import static hammentyneethaxaajat.viiteapuri.resurssit.Tulosteet.*;
import hammentyneethaxaajat.viiteapuri.validaattori.Validoija;
import hammentyneethaxaajat.viiteapuri.viite.ViiteKasittelija;
import java.io.IOException;

/**
 * Toiminto bibtex-tiedoston luontiin.
 */
class luoBibtex extends Toiminto {
    private BibtexIO tiedostokasittelija;

    public luoBibtex(IO io, ViiteKasittelija viitekasittelija, Validoija validaattori, BibtexIO tiedostokasittelija) {
        super(io, viitekasittelija, validaattori);
        this.tiedostokasittelija = tiedostokasittelija;
    }

    /**
     * Lue ohjelmassa olevien viitteiden perusteella BIBTeX-tiedoston.
     * Pyytää käyttäjää nimeämään tiedoston
     */
    @Override
    public void suorita() {
        //TODO Vaihtakaa polun yms. kysely johonkin järkevään. 
        String nimi = hankiValidiSyote(KYSY_TIEDOSTO_NIMI, false);
        String polku = ""; // = hankiValidiSyote(KYSY_TIEDOSTO_POLKU, false);

        if (nimi.isEmpty()) {
            nimi = "viitteet";
        }
        if (polku.matches("")) {
            polku = "";
        }

        try {
            tiedostokasittelija.kirjoitaBibtex(viiteKasittelija.viitteetBibtexina(), polku + nimi);
            io.tulosta(TIEDOSTONLUONTI_ONNISTUI + nimi + "\n");
        } catch (IOException e) {
            io.tulosta(TIEDOSTONLUONTI_EI_ONNISTUNUT);
        }
        if (!ohjelmassaViitteita()) {
            io.tulosta("Huom! Luotu tiedosto ei sisällä yhtään viitettä.\n");
        }
    }

    @Override
    public String toString() {
        return KOMENTO_LUO_BIBTEX;
    }
    
}
