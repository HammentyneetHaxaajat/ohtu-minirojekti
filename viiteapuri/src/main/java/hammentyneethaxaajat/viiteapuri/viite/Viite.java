package hammentyneethaxaajat.viiteapuri.viite;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *  Viitettä kuvaava luokka, joka sisältää viitteen tyypin, nimen sekä useita luokalle kuuluvia attribuutteja.
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
    
    public void setTyyppi(ViiteTyyppi tyyppi) {
        this.tyyppi = tyyppi;
    }

    public void setNimi(String nimi) {
        this.nimi = nimi;
    }

    /**
     * Asettaa parametrina määritellylle attribuutille toisen parametrin
     * määrittämän arvon.
     *
     * @param attr Attribuutti jonka arvo asetetaan.
     * @param arvo Attribuutin uusi arvo.
     */
    
    public void setAttribuutti(String attr, String arvo) {
        if (attribuutit.containsKey(attr)) {
            attribuutit.get(attr).setArvo(arvo);
        } else {
            attribuutit.put(attr, new Attribuutti(AttrTyyppi.valueOf(attr), arvo));
        }
    }

    /**
     * Palauttaa selkokielisen listan viitteen kenttien arvoista.
     *
     * @return String joka sisältää kaikki viitteen sisältämät arvot.
     */
    
    public String listaus() {
        return attribuutit.values().stream()
                .sorted((a, b) -> a.getTyyppi().name().compareTo(b.getTyyppi().name()))
                .map(a -> a.getTyyppi().name() + ": " + a.getArvo())
                .collect(Collectors.joining("\n", "nimi: " + this.nimi + "\ntyyppi: " + this.tyyppi.name() + "\n", "\n"));
    }


    /**
     * Asettaa viitteen pakollisten tai valinnaisten attribuuttien arvot
     * riippuen saamastaan attribuuttijoukosta.
     * @param arvot viitteen pakolliset tai valinnaiset arvot
     */
    
    public void asetaAttribuuttienArvot(Map<String, String> arvot) {
        arvot.keySet().stream().forEach(s -> this.setAttribuutti(s, arvot.get(s)));
    }
    
    
    /**
     * Palauttaa bibtexmuotoisen tekstiesityksen
     *
     * @return
     */
//    @Override
//    public String toString() {
//        return attribuutit.values().stream().filter(s -> !s.getArvo().equals("")).map(a -> a.toString()).collect(Collectors.joining("\n", "@" + this.tyyppi.name() + "{" + this.nimi + ",\n", "\n}\n"));
//    }
}
