package hammentyneethaxaajat.viiteapuri.viite;

/**
 * Atribuutin tyyppi, joka määrittää kyseiselle atribuutille hyväksyttävän
 * arvon.
 */

public enum AttrTyyppi {

    author("(\\p{L}|\\s|\\{Punct})*"), title("(\\p{L}|\\d|\\s|\\p{Punct})*"), 
    editor("(\\p{L}|\\d|\\s|\\p{Punct})*"), publisher("(\\p{L}|\\d|\\s|\\p{Punct})*"), 
    year("[\\d]{4}"), volume("(\\p{L}|\\d|\\s|\\p{Punct})*"), 
    number("(\\p{L}|\\d|\\s|\\p{Punct})*"), 
    series("(\\p{L}|\\d|\\s|\\p{Punct})*"), address("(\\p{L}|\\d|\\s|\\p{Punct})*"), 
    edition("(\\p{L}|\\d|\\s|\\p{Punct})*"), month("(\\p{L})*"), 
    note("(\\p{L}|\\d|\\s|\\p{Punct})*"), key("(\\d)*"), crossref("");

    private String muoto;

    private AttrTyyppi(String syntaksi) {
        if (syntaksi.equals("")) {
            this.muoto = ".*";
            return;
        }
        
        this.muoto = syntaksi;
    }

    /**
     * Palauttaa String muotoisen olion, joka on tyyppiä vastaavia arvoja
     * kuvaava säännöllinen lause.
     *
     * @return Tyyppiä vastaava säännöllinen lause stringinä
     */
    
    public String getMuoto() {
        return this.muoto;
    }
}
