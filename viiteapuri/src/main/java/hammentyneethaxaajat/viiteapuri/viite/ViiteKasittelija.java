package hammentyneethaxaajat.viiteapuri.viite;

import static hammentyneethaxaajat.viiteapuri.resurssit.Tulosteet.*;
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
    
    protected int bibtexJarjestys(Viite viiteA, Viite viiteB) {
        if (tarkistaAttribuutinTyhjyys(viiteA, "crossref") & !tarkistaAttribuutinTyhjyys(viiteB, "crossref")) {
            return -1;
        } else if (tarkistaAttribuutinTyhjyys(viiteB, "crossref") & !tarkistaAttribuutinTyhjyys(viiteA, "crossref")) {
            return 1;
        }
        
        String a = tarkistaVartailtavaAttribuutti(viiteA);
        String b = tarkistaVartailtavaAttribuutti(viiteB);
        
        return a.compareTo(b);
    }

    protected String tarkistaVartailtavaAttribuutti(Viite viite) {
        String a = "";
        if (!tarkistaAttribuutinTyhjyys(viite, "author")) {
            a = viite.getAttribuutti("author").getArvo();
        } else if (!tarkistaAttribuutinTyhjyys(viite, "editor")) {
            a = viite.getAttribuutti("editor").getArvo();
        } else if (!tarkistaAttribuutinTyhjyys(viite, "key")) {
            a = viite.getAttribuutti("key").getArvo();
        }
        
        return a;
    }
    
    protected boolean tarkistaAttribuutinTyhjyys(Viite viite, String attribuutti) {
        if (viite.getAttribuutti(attribuutti) != null && !viite.getAttribuutti(attribuutti).getArvo().isEmpty()) {
            return false;
        }
        return true;
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
     *
     * @param viite Poistettava viite.
     */
    public void poistaViite(Viite viite) {
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

    /**
     * Palauttaa kaikki ohjelman sisältämät viitteet bibtex muotoisena
     * stringinä.
     *
     * @return String joka sisältää bibtex muotoisen esityksen kaikista
     * viitteistä
     */
    public String viitteetBibtexina() {
        return viitteet.values().stream()
                .sorted((a,b) -> bibtexJarjestys(a, b))
                .map(v -> v.toString())
                .collect(Collectors.joining("\n", "\\usepackage[utf8]{inputenc}\n\n", ""));
    }

    /**
     * Palauttaa kaikki ohjelman sisältämät viitteet.
     *
     * @return Collectoin, joka sisältää kaikki ohjelmaan syötetyt viitteet.
     */
    public Collection<Viite> getViitteet() {
        return viitteet.values();
    }

    /**
     * Palauttaa selkokielisen listan kaikista Viitekäsittelijalle annetuista
     * viitteistä.
     *
     * @return String, joka sisältää kaikki Viitekäsittelijän sisältämät
     * viitteet selkokielisessä muodossa.
     */
    public String viitteetListauksena() {
        return viitteet.values().stream()
                .map(s -> s.listaus())
                .collect(Collectors.joining("----------------------------------------\n", "--------------------Listataan viitteet--------------------\n", "--------------------END--------------------\n"));
    }

    /**
     * Palauttaa joukon AtriTyyppejä joka sisältää kaikki parametrina saadulle
     * viitteelle pakolliset AttrTyypit.
     *
     * @param viite Viiten jonka pakolliset attribuutit halutaan.
     * @return Collection joka sisältää kaikki viitteelle pakolliset AttrTyypit.
     */
    public Collection<AttrTyyppi> pakollisetAttribuutit(Viite viite) {
        String crossref = viite.getAttribuutti("crossref").getArvo();
        return viite.getTyyppi().getKaikki().stream()
                .filter(s -> ((viite.getTyyppi().getPakolliset().contains(s)) //Omat pakolliset
                        && (crossref.equals("") ? true : this.haeViite(crossref).getAttribuutti(s.name()) == null || this.haeViite(crossref).getAttribuutti(s.name()).getArvo().equals(""))) //Omat pakolliset eivät pakollisia jos crossreff antaa niille arvon
                        || this.viittaavatViitteet(viite).stream().anyMatch(v -> v.getTyyppi().getPakolliset().contains(s) && v.getAttribuutti(s.name()).getArvo().equals(""))) //Arvosta tulee pakollinen jos se antaa arvon toisen viitteen pakolliseen kenttään
                .collect(Collectors.toSet());
    }

    /**
     * Palauttaa joukon viitteitä, joka sisältää kaikki viitteet joilla
     * parametrina saatu viite on crossref attribuutin arvona.
     *
     * @param viite Viite johon viittaavat viitteet halutaan
     * @return Collection viitteitä, jotka viittaavat parametrina annetuun
     * viitteeseen crossref attribuutilla.
     */
    public Collection<Viite> viittaavatViitteet(Viite viite) {
        return this.getViitteet()
                .stream()
                .filter((v) -> (v.getAttribuutti(CROSSREF).getArvo().equals(viite.getNimi())))
                .collect(Collectors.toSet());
    }
}
