package hammentyneethaxaajat.viiteapuri.viite;

/**
 *  Olio jota käytetään viitteen kenttien arvojen tallettamiseen.
 */

public class Attribuutti {

    private final AttrTyyppi tyyppi;
    private String arvo;

    public Attribuutti(AttrTyyppi tyyppi, String arvo) {
        this.tyyppi = tyyppi;
        this.arvo = arvo;
    }

    public AttrTyyppi getTyyppi() {
        return tyyppi;
    }

    public String getArvo() {
        return arvo;
    }

    public void setArvo(String arvo) {
        this.arvo = arvo;
    }

    /**
     * Palauttaa bibtex muotoisen tekstiesityksen
     * @return 
     */
    @Override
    public String toString() {
        return tyyppi.name() + " = {" + arvo + "},";
    }
}
