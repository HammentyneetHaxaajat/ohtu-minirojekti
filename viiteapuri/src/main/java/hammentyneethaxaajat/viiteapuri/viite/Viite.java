package hammentyneethaxaajat.viiteapuri.viite;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 */
public class Viite {

    private ViiteTyyppi tyyppi;
    private String nimi;
    private Map<String, Attribuutti> attribuutit;

    public Viite() {
        attribuutit = new HashMap<>();
    }

    public ViiteTyyppi getTyyppi() {
        return tyyppi;
    }

    public String getNimi() {
        return nimi;
    }

    public Attribuutti getAttribuutti(String attr) {
        return attribuutit.get(attr);
    }

    public String getAttribuutinArvo(String attr) {
        return attribuutit.get(attr).getArvo();
    }

    public void setTyyppi(ViiteTyyppi tyyppi) {
        this.tyyppi = tyyppi;
    }

    public void setNimi(String nimi) {
        this.nimi = nimi;
    }

    public void setAttribuutti(String attr, String arvo) {
        if (attribuutit.containsKey(attr)) {
            attribuutit.get(attr).setArvo(arvo);
        } else {
            attribuutit.put(attr, new Attribuutti(AttrTyyppi.valueOf(attr), arvo));
        }
    }

    public void setAttribuutti(Attribuutti attr) {
        attribuutit.put(attr.getTyyppi().name(), attr);
    }

    /**
     * Palauttaa selkokielisen listan viitteen kenttien arvoista.
     *
     * @return
     */
    public String listaus() {
        return attribuutit.values().stream().map(a -> a.getTyyppi().name() + ": " + a.getArvo()).collect(Collectors.joining("\n", "nimi: " + this.nimi + "\ntyyppi: " + this.tyyppi.name() + "\n", "\n"));
    }

    /**
     * Palauttaa bibtexmuotoisen tekstiesityksen
     *
     * @return
     */
    @Override
    public String toString() {
        return attribuutit.values().stream().filter(s -> !s.getArvo().equals("")).map(a -> a.toString()).collect(Collectors.joining("\n", "@" + this.tyyppi.name() + "{" + this.nimi + ",\n", "\n}\n"));
    }

}
