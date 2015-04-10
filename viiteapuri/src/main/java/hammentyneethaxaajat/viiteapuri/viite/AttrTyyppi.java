package hammentyneethaxaajat.viiteapuri.viite;

/**
 * Viitteen attribuuttien tyyppejä kuvaava enum. Kullekin attribuuttityypille määritetään
 * hyväksyttävät arvot regexillä
 */
public enum AttrTyyppi {

    //TODO Kullekin voisi määrittää myös kuvauksen attribuutin tarkoituksesta ja/tai sanallisen kuvauksen attribuutin hyväksymistä arvoista.
    author("(\\p{L}|\\s|\\p{Punct})*"), title("(\\p{L}|\\d|\\s|\\p{Punct})*"),
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
