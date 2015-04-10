package hammentyneethaxaajat.viiteapuri.viite;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Luokka joka sis√§lt√§√§ Viite-olioiden k√§sittelyyn liityvi√§ metodeja.
 */

public class ViiteKasittelija {

    private Map<String, Viite> viitteet;

    public ViiteKasittelija() {
        viitteet = new HashMap<>();
    }

    /**
     * Viitteen lis‰‰minen lis‰‰ viitteen viitelistaan tai heitt‰‰ IllegalStatExceptionin
     * jos saman niminen viite on jo olemassa. T‰llaista ei voi ko. koodilla edes
     * tapahtua, mutta virhetilanteisiin silti fiksu varautua. *samoin k‰yt‰nnˆss‰
     * jos uusi = null..
     * @param uusi 
     */
    
    public void lisaaViite(Viite uusi) {
        if (viitteet.containsKey(uusi.getNimi())) {
            throw new IllegalStateException("Saman niminen viite on jo olemassa.");
        }
        
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
     * Palauttaa selkokielisen listan kaikista Viitek√§sittelijalle annetuista
     * viitteist√§.
     *
     * @return String, joka sis√§lt√§√§ kaikki Viitek√§sittelij√§n sis√§lt√§m√§t
     * viitteet.
     */
    public String viitteetListauksena() {
        return viitteet.values().stream()
                .map(s -> s.listaus())
                .collect(Collectors.joining("----------------------------------------\n", "--------------------Listataan viitteet--------------------\n", "--------------------END--------------------\n"));
    }
}