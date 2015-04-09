package hammentyneethaxaajat.viiteapuri.IO;

import java.util.ArrayList;

/**
 *
 * @author juri
 */
public class StubIO implements IO {

    private String[] rivit;
    private ArrayList<String> tulosteet;
    private int i;

    public StubIO(String... komennot) {
        this.rivit = komennot;
        this.tulosteet = new ArrayList<>();
    }

    @Override
    public void tulosta(String teksti) {
        tulosteet.add(teksti);
    }

    @Override
    public String lueRivi(String kehote) {
        if (i < rivit.length) {
            return rivit[i++];
        }
        return "";
    }

    public ArrayList<String> getTulosteet() {
        return tulosteet;
    }
}
