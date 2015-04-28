package hammentyneethaxaajat.viiteapuri.viite;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *  Viitettä kuvaava luokka, joka sisältää viitteen tyypin, nimen sekä useita luokalle kuuluvia attribuutteja.
 */

public class Viite {

    private final ViiteTyyppi tyyppi;
    private String bibtexAvain;
    private Map<String, Attribuutti> attribuutit;

    public Viite(ViiteTyyppi tyyppi) {
        this.tyyppi = tyyppi;
        attribuutit = new HashMap<>();
        setAttribuutti(AttrTyyppi.crossref.name(), "");
        this.tyyppi.getKaikki().stream().forEach(attribuutti -> setAttribuutti(attribuutti.name(), ""));
    }

    public ViiteTyyppi getTyyppi() {
        return tyyppi;
    }

    public String getBibtexAvain() {
        return bibtexAvain;
    }

    public Attribuutti getAttribuutti(String attr) {
        return attribuutit.get(attr);
    }
    

    public void setBibtexAvain(String avain) {
        this.bibtexAvain = avain;
    }
    
    public Map<String, Attribuutti> getAttribuutit() {
        return attribuutit;
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
     * @param listaaTyhjat
     * @return String joka sisältää kaikki viitteen sisältämät arvot.
     */
    
    public String listaus(boolean listaaTyhjat) {
        return attribuutit.values().stream()
                .filter(attribuutti -> listaaTyhjat ? true : !attribuutti.getArvo().isEmpty())
                .sorted((a, b) -> a.getTyyppi().name().compareTo(b.getTyyppi().name()))
                .map(a -> a.getTyyppi().name() + ": " + a.getArvo())
                .collect(Collectors.joining("\n", "bibtexavain: " + this.bibtexAvain + "\ntyyppi: " + this.tyyppi.name() + "\n", "\n"));
    }
    
    public String listaus() {
        return listaus(true);
    }


    /**
     * Asettaa viitteen pakollisten tai valinnaisten attribuuttien arvot
     * riippuen saamastaan attribuuttijoukosta.
     * @param arvot viitteen pakolliset tai valinnaiset arvot
     */
    
//    public void asetaAttribuuttienArvot(Map<String, String> arvot) {
//        arvot.keySet().stream().forEach(s -> this.setAttribuutti(s, arvot.get(s)));
//    }
    
    
    /**
     * Palauttaa bibtexmuotoisen tekstiesityksen
     *
     * @return
     */
    @Override
    public String toString() {
        return attribuutit.values().stream()
                .filter(s -> !s.getArvo().equals(""))
                .sorted((a,b) -> a.getTyyppi().name().compareTo(b.getTyyppi().name()))
                .map(a -> a.toString())
                .collect(Collectors.joining("\n", "@" + this.tyyppi.name() + "{" + this.bibtexAvain + ",\n", "\n}\n"));
    }
}
