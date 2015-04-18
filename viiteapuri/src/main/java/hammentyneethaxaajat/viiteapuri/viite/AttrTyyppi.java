package hammentyneethaxaajat.viiteapuri.viite;

/**
 * Viitteen attribuuttien tyyppejä kuvaava enum. Kullekin attribuuttityypille määritetään
 * hyväksyttävät arvot regexillä
 */
public enum AttrTyyppi {

    //TODO Määrittäkää sanalliset kuvaukset syntaksille jos se on monimutkainen. Silloin sen voi tulostaa "virheellisen syntaksi" ilmoituksen kanssa.
    //TODO Määrittäkää kunnolliset regexit
    author("(\\p{L}|\\s|\\p{Punct})*"), title("(\\p{L}|\\d|\\s|\\p{Punct})*"),
    editor("(\\p{L}|\\d|\\s|\\p{Punct})*"), publisher("(\\p{L}|\\d|\\s|\\p{Punct})*"),
    year("([\\d]{4})?"), volume("(\\p{L}|\\d|\\s|\\p{Punct})*"),
    number("(\\p{L}|\\d|\\s|\\p{Punct})*"),
    series("(\\p{L}|\\d|\\s|\\p{Punct})*"), address("(\\p{L}|\\d|\\s|\\p{Punct})*"),
    edition("(\\p{L}|\\d|\\s|\\p{Punct})*"), month("(\\p{L})*"),
    note("(\\p{L}|\\d|\\s|\\p{Punct})*"), key("(\\d)*"), crossref(""), pages(""), journal("");

    private String muoto;

    /**
     * Alustaa AttrTyyppi enumit. Kukin saa oletuksena ".*" regexin jos parametri ei määritä muuta.
     * @param syntaksi 
     */
    private AttrTyyppi(String syntaksi) {
        if (syntaksi.equals("")) {
            this.muoto = ".*";
        } else {
            this.muoto = syntaksi;
        }

    }

    /**
     * Palauttaa String muotoisen olion, joka on tyyppiä vastaavia arvoja
     * kuvaava säännöllinen lause.
     *
     * @return Tyypin hyväksymiä arvoja vastaava säännöllinen lause stringinä
     */
    
    public String getMuoto() {
        return this.muoto;
    }
}
