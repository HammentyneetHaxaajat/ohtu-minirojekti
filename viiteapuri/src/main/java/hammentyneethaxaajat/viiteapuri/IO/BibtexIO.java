package hammentyneethaxaajat.viiteapuri.IO;

import static hammentyneethaxaajat.viiteapuri.resurssit.Tulosteet.*;
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
        if (!polku.endsWith(".bib")) {
            polku = polku + ".bib";
        }
        String sisalto = haeTiedostonSisalto(polku);
        return luoViitteetStringista(sisalto);
    }

    /**
     * Luo annetusta Stringista vastaavat Viite oliot.
     *
     * @param sisalto String joka sisältää viitteet
     * @return Collection joka sisälrää Stringin sisältäneet viitteet.
     * @throws IOException Jos String ei vastaa odotettua formaattia.
     */
    private Collection<Viite> luoViitteetStringista(String sisalto) throws IOException {
        try {
            Collection<Viite> viitteet = new HashSet<>();
            String viitteetStringeina[] = sisalto.split("@");
            for (int i = 1; i < viitteetStringeina.length; i++) {
                String ViiteStringina = viitteetStringeina[i];
                Viite viite = luoViiteStringista(ViiteStringina);
                viitteet.add(viite);
            }
            return viitteet;
        } catch (Exception e) {
            throw new IOException(VIRHE_TIEDOSTOFORMAATTI);
        }
    }

    /**
     * Hakee parametrina saadun Strining osoittamasta kohteesta tiedoston ja
     * palauttaa sen sisällön Striginä
     *
     * @param polku Tiedostopolku josta tiedosto yritetään lukea.
     * @return Tiedoston sisältö Stringinä.
     * @throws IOException Jos polku ei osoita kelvolliseen tiedostoon.
     */
    private String haeTiedostonSisalto(String polku) throws IOException {
        String sisalto = "";
        try {
            Path asdf = Paths.get(polku);
            sisalto = Files.readAllLines(asdf).stream().collect(Collectors.joining("\n"));
        } catch (IOException iOException) {
            throw new IOException(VIRHE_OLEMATONTIEDOSTO);
        }
        return sisalto;
    }

    /**
     * Luo Viitteen annetusta Stringistä
     *
     * @param viiteStringina String josta viite luodaan.
     * @return Stringin sisältämä Viite.
     */
    private Viite luoViiteStringista(String viiteStringina) {
        Viite viite = new Viite(ViiteTyyppi.valueOf(viiteStringina.substring(0, viiteStringina.indexOf("{"))));
        String attribuutit[] = viiteStringina.substring(viiteStringina.indexOf("{") + 1).split(",\n");
        asetaAttribuutit(viite, attribuutit);
        return viite;
    }

    /**
     * Asettaa viitteelle attribuutit attribuuttien String esityksistä.
     *
     * @param viite Viite jolle attribuutit asetetaan
     * @param attribuutit Attribuuttien String esitykset sisältävä array.
     */
    private void asetaAttribuutit(Viite viite, String[] attribuutit) {
        viite.setBibtexAvain(attribuutit[0]);

        for (int j = 1; j < attribuutit.length - 1; j++) {
            String attribuutti = attribuutit[j];
            String avainArvoPari[] = attribuutti.split("=");
            viite.setAttribuutti(avainArvoPari[0].trim(), avainArvoPari[1].substring(avainArvoPari[1].indexOf("{") + 1, avainArvoPari[1].lastIndexOf("}")));
        }
    }
}
