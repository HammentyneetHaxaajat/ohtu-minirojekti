package hammentyneethaxaajat.viiteapuri.viite;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 */

public class ViiteKasittelija {

    private Map<String, Viite> viitteet;

    public ViiteKasittelija() {
        viitteet = new HashMap<>();
    }

    public void lisaaViite(Viite uusi) {
        viitteet.put(uusi.getNimi(), uusi);
    }

    public Viite haeViite(String nimi) {
        return viitteet.get(nimi);
    }

    public String viitteetBibtexina() {
        return viitteet.values().stream().map(v -> v.toString()).collect(Collectors.joining("\n"));
    }

    public Collection<Viite> getViitteet() {
        return viitteet.values();
    }
    
    public String viitteetListauksena(){
        return viitteet.values().stream().map(s -> s.listaus()).collect(Collectors.joining("----------------------------------------\n", "--------------------Listataan viitteet--------------------\n", "--------------------END--------------------\n"));
    }

}
