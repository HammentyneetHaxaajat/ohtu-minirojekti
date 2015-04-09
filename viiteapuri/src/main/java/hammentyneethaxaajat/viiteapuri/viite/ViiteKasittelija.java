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

    public void lisaaViite(Viite uusi) {
        viitteet.put(uusi.getNimi(), uusi);
    }

    /**
     * Hakee viitteen sille annetun nimen perusteella.
     * @param nimi
     * @return 
     */
    
    public Viite haeViite(String nimi) {
        return viitteet.get(nimi);
    }

//    public String viitteetBibtexina() {
//        return viitteet.values().stream().map(v -> v.toString()).collect(Collectors.joining("\n"));
//    }
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