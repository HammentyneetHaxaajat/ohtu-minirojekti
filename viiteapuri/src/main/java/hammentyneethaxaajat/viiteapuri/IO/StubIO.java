package hammentyneethaxaajat.viiteapuri.IO;

import java.util.ArrayList;

/**
 *
 */

public class StubIO implements IO {

    private String[] rivit;
    private ArrayList<String> tulosteet;
    private int i;

    public StubIO(String... komennot) {
        this.rivit = komennot;
        this.tulosteet = new ArrayList<>();
    }

    /**
     * 
     * @param teksti 
     */
    
    @Override
    public void tulosta(String teksti) {
        tulosteet.add(teksti);
    }

    /**
     * 
     * @param kehote
     * @return 
     */
    
    @Override
    public String lueRivi(String kehote) {
        if (i < rivit.length) {
            return rivit[i++];
        }
        return "";
    }

    /**
     * 
     * @return 
     */
    
    public ArrayList<String> getTulosteet() {
        return tulosteet;
    }
}
