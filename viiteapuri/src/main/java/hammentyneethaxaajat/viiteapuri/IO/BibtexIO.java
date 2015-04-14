package hammentyneethaxaajat.viiteapuri.IO;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

/**
 * Luokka joka vastaa .bib tyyppiseten tiedostojen käsittelystä.
 */
public class BibtexIO {

    /**
     * Luo tai uudelleenkirjoittaa polun määrittämässä sijainnissa olevan .bib tiedoston parametrina saadulla datalla.
     * @param tieto Tiedoston kirjoitettava bibtex muotoinen string
     * @param polku Polku tiedostoon johon kirjoitetaan.
     * @throws IOException Jos jotain menee pieleen...
     */
    public void kirjoitaBibtex(String tieto, String polku) throws IOException {
        //Tämä varmistaa että rivivaihto toimii
        tieto = tieto.replace("\n", System.getProperty("line.separator"));
        try (BufferedWriter kirjoittaja = new BufferedWriter(new FileWriter(polku + ".bib"))) {
            kirjoittaja.write(tieto);
        }
    }
}
