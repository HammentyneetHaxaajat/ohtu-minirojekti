package hammentyneethaxaajat.viiteapuri.IO;

import hammentyneethaxaajat.viiteapuri.viite.Viite;
import hammentyneethaxaajat.viiteapuri.viite.ViiteTyyppi;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Collectors;

/**
 * Luokka joka vastaa .bib tyyppiseten tiedostojen käsittelystä.
 */
public class BibtexIO {

    /**
     * Luo tai uudelleenkirjoittaa polun määrittämässä sijainnissa olevan .bib
     * tiedoston parametrina saadulla datalla.
     *
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

    /**
     * Lukee polun määrittämän .bib tiedoston ja palauttaa tiedostosta löytyneet
     * viitteet Collection oliossa.
     *
     * @param polku String polusta josta tiedosto yritetään lukea
     * @return Collection Collection olio joka sisältää tiedoston sisältämät
     * viitteet.
     * @throws java.io.IOException Jos tiedoston lukeminen epäonnistuu.
     */
    public Collection<Viite> haeViitteetTiedostosta(String polku) throws IOException {
        Path asdf = Paths.get(polku);
        String sisalto = Files.readAllLines(asdf).stream().collect(Collectors.joining("\n"));
        String viitteetStringeina[] = sisalto.split("@");
        Collection<Viite> viitteet = new HashSet<>();
        for (int i = 1; i < viitteetStringeina.length; i++) {
            String ViiteStringina = viitteetStringeina[i];
            Viite viite = new Viite(ViiteTyyppi.valueOf(ViiteStringina.substring(0, ViiteStringina.indexOf("{"))));
            String attribuutit[] = ViiteStringina.substring(ViiteStringina.indexOf("{") + 1).split(",\n");
            viite.setBibtexAvain(attribuutit[0]);
            for (int j = 1; j < attribuutit.length - 1; j++) {
                String attribuutti = attribuutit[j];
                String avainArvoPari[] = attribuutti.split("=");
                viite.setAttribuutti(avainArvoPari[0].trim(), avainArvoPari[1].substring(avainArvoPari[1].indexOf("{") + 1, avainArvoPari[1].lastIndexOf("}")));
            }
            viitteet.add(viite);
            //TODO Alempi rivi debugaukseen
//            System.out.println(viite);
        }
        return viitteet;
    }
}
