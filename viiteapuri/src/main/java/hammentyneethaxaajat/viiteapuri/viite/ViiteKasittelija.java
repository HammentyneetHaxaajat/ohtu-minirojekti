package hammentyneethaxaajat.viiteapuri.viite;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Luokka joka sisältää Viite-olioiden käsittelyyn liityviä metodeja.
 */
public class ViiteKasittelija {

    private Map<String, Viite> viitteet;

    public ViiteKasittelija() {
        viitteet = new HashMap<>();
    }

    /**
     * Viitteen lisääminen lisää viitteen viitelistaan
     *
     * @param uusi lisättävä viite
     */
    public void lisaaViite(Viite uusi) {
        viitteet.put(uusi.getNimi(), uusi);
    }
    
    /**
     * Poistaa viitteen parametrina saadun viitteen ohjelmasta.
     * @param viite Poistettava viite.
     */
    public void poistaViite(Viite viite){
        viitteet.remove(viite.getNimi());
    }

    /**
     * Hakee viitteen sille annetun nimen perusteella.
     *
     * @param nimi Haettavan viitteen nimi
     * @return haun perusteella palautettava Viite-olio
     */
    public Viite haeViite(String nimi) {
        return viitteet.get(nimi);
    }

    public String viitteetBibtexina() {
        return viitteet.values().stream().map(v -> v.toString()).collect(Collectors.joining("\n", "\\usepackage[utf8]{inputenc}\n\n",""));
    }
    
    public Collection<Viite> getViitteet() {
        return viitteet.values();
    }

    /**
     * Palauttaa selkokielisen listan kaikista Viitekäsittelijalle annetuista
     * viitteistä.
     *
     * @return String, joka sisältää kaikki Viitekäsittelijän sisältämät
     * viitteet.
     */
    public String viitteetListauksena() {
        return viitteet.values().stream()
                .map(s -> s.listaus())
                .collect(Collectors.joining("----------------------------------------\n", "--------------------Listataan viitteet--------------------\n", "--------------------END--------------------\n"));
    }
}
