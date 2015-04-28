package hammentyneethaxaajat.viiteapuri.viite;

import static hammentyneethaxaajat.viiteapuri.resurssit.Tulosteet.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;
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
        return viite.getAttribuutti(attribuutti) == null
                || viite.getAttribuutti(attribuutti).getArvo().isEmpty();
    }

    /**
     * Viitteen lisääminen lisää viitteen viitelistaan
     *
     * @param viite lisättävä viite
     */
    public void lisaaViite(Viite viite) {
        if (viite.getBibtexAvain().isEmpty()) {
            viite.setBibtexAvain(generoiAvain(viite));
        }

        viitteet.put(viite.getBibtexAvain(), viite);
    }

    /**
     * Erottele sukuNimi siten että ainoastaan yhden authorin sukunimi tulee
     * avaimen osaksi.
     *
     * @param viite
     * @return
     */
    protected String generoiAvain(Viite viite) {
        Attribuutti sukuNimi = viite.getAttribuutti(AttrTyyppi.author.name());
        if (sukuNimi == null) {
            sukuNimi = viite.getAttribuutti(AttrTyyppi.editor.name());
        }

        Attribuutti vuosi = viite.getAttribuutti(AttrTyyppi.year.name());

        String avain = luoAvain(sukuNimi, vuosi);
        return avain + tunniste(avain);
    }

    protected String luoAvain(Attribuutti sukuNimi, Attribuutti year) {
        String yhdiste = "";

        if (sukuNimi != null) {
            yhdiste = sukuNimi.getArvo();
        }

        if (year != null) {
            yhdiste += year.getArvo();
        }
        return yhdiste;
    }

    /**
     * Luo avaimelle tunnisteen. Tunniste on tyhjä jos avain on yksiselitteinen.
     *
     * Jos samanalkuisia viitteitä on olemassa jo tai avain on tyhjä,
     * palautetaan tunniste, joka lisätään avaimen perään.
     *
     * @param avain
     * @return
     */
    protected String tunniste(String avain) {
        char tunniste = 'a';
        char samanAlkuisia;

        if (avain.isEmpty()) {
            samanAlkuisia = (char) yhdenCharinAvaimet();
        } else {
            samanAlkuisia = (char) sanallaAlkavatAvaimet(avain);
            if (samanAlkuisia == 0) {
                return "";
            }

            samanAlkuisia--;    // vähennetään yksi joka on "alkuperäinen" ilman tunnistetta
        }

        return ((char) (tunniste + samanAlkuisia)) + "";
    }

    protected int yhdenCharinAvaimet() {
        return viitteet.values().stream().filter(v -> v.getBibtexAvain().length() == 1).toArray().length;
    }

    protected int sanallaAlkavatAvaimet(String sana) {
        return viitteet.values().stream().filter(v -> v.getBibtexAvain().matches(sana + "(.*)")).toArray().length;
    }

    /**
     * Poistaa viitteen parametrina saadun viitteen ohjelmasta.
     *
     * @param viite Poistettava viite.
     */
    public void poistaViite(Viite viite) {
        viitteet.remove(viite.getBibtexAvain());
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
                .sorted((a, b) -> bibtexJarjestys(a, b))
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
     * Palauttaa kaikki ohjelman sisältämät viitteet jotka totettavat hakuehdon.
     *
     * @param attribuutti Attribuutti jonka arvolla haku toteutetaan.
     * @param arvo Arvo joka attribuutilla halutaan olevan.
     * @return Collectoin, joka sisältää kaikki ohjelmaan syötetyt hakuehdon
     * toteuttavat viitteet.
     */
    public Collection<Viite> haeViitteet(String attribuutti, String arvo) {
        Predicate<Viite> testi;
        if (attribuutti.equals(AttrTyyppi.bibtexavain.name())) {
            testi = viite -> viite.getBibtexAvain().contains(arvo);
        } else if (attribuutti.equals("tyyppi")) {
            testi = viite -> viite.getTyyppi().name().equals(arvo);
        } else {
            testi = viite -> viite.getAttribuutit().containsKey(attribuutti)
                    && viite.getAttribuutti(attribuutti).getArvo().toLowerCase().contains(arvo.toLowerCase());
        }
        return viitteet.values().stream()
                .filter(testi)
                .collect(Collectors.toList());
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
                .map(s -> s.listaus(false))
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
                .filter((v) -> v.getAttribuutit().containsKey(CROSSREF) && v.getAttribuutti(CROSSREF).getArvo().equals(viite.getBibtexAvain()))
                .collect(Collectors.toSet());
    }

    /**
     * Päivittää Viitteen attribuutin arvon.
     *
     * @param viite Viite jonka attribuutin arvo päivitetään.
     * @param attribuutti Attribuutti jonka arvo päivitetään.
     * @param uusiArvo Attribuutin uusi arvo.
     */
    public void paivitaArvo(Viite viite, String attribuutti, String uusiArvo) {
        if (attribuutti.equals(BIBTEXAVAIN)) {
            //TODO tämä säädön voisi siirtää viitekäsittelijän metodiksi. paivitaNimi(Viite viite, String nimi).
            viittaavatViitteet(viite).stream().forEach(s -> s.setAttribuutti(AttrTyyppi.crossref.name(), uusiArvo));
            poistaViite(viite);
            viite.setBibtexAvain(uusiArvo);
            lisaaViite(viite);
        } //                else if(attribuutti.equals(CROSSREF)){
        //                //TODO Tähän voi määrittää toiminnan kun/jos crossref muutetaan. Muista päivittää myös validaattorin vastaava toiminta.
        //            } 
        else {
            viite.setAttribuutti(attribuutti, uusiArvo);
        }
    }
}
