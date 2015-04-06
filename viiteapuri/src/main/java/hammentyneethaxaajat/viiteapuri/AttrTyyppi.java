package hammentyneethaxaajat.viiteapuri;

/**
 * Atribuutin tyyppi, joka määrittää kyseiselle atribuutille hyväksyttävän
 * arvon.
 */
public enum AttrTyyppi {

    //TODO Määritä kullekin näistä Regex syntaksi.
    author(""), title(""), editor(""), publisher(""), year("[0-9]{4}"), volume(""),
    number(""), series(""), address(""), edition(""), month(""), note(""), key("");

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
     * @return Tyyppiä vastaava säännöllinen lause stringinä
     */
    public String getMuoto() {
        return this.muoto;
    }

}
