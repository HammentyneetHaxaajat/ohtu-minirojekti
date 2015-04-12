package hammentyneethaxaajat.viiteapuri.IO;

import java.util.ArrayList;

/**
 * Stub luokka IO:lle, jota käytetään testaamiseen.
 * StubIO ottaa parametrina komennot, jotka normaalisti näppäillään
 * itse ohjelman suorituksen aikana. Ohjelman tulosteet tallennetaan
 * testien ajaksi listaan.
 *
 */

public class StubIO implements IO {

    private String[] rivit;
    private ArrayList<String> tulosteet;
    private int i;
    
    /**
     * Luo uuden StubIO:n
     * 
     * @param komennot Ohjelman suorituksen aikana käsiteltävät komennot.
     */

    public StubIO(String... komennot) {
        this.rivit = komennot;
        this.tulosteet = new ArrayList<>();
    }

    /**
     * Ottaa talteen tulostettavaksi tarkoitetun tekstin.
     * 
     * @param teksti tulostettava teksti
     */
    
    @Override
    public void tulosta(String teksti) {
        tulosteet.add(teksti);
    }

    /**
     * Ottaa syötteen pyyntyä varten kirjoitetun kehotteen talteen
     * ja palauttaa seuraavan rivit-taulukkoon talletetuista syötteistä
     * 
     * @param kehote Ohje siitä mitä ohjelman käyttäjän tulisi syöttää
     * @return seuraava syöte rivit-taulukosta
     */
    
    @Override
    public String lueRivi(String kehote) {
        tulosteet.add(kehote);
        if (i < rivit.length) {
            return rivit[i++];
        }
        return "";
    }

    /**
     * Palauttaa talteen otetut tulosteet.
     * 
     * @return lista tulosteista
     */
    
    public ArrayList<String> getTulosteet() {
        return tulosteet;
    }
}
