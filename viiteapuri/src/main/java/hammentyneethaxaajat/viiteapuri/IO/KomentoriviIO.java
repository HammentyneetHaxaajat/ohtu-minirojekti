package hammentyneethaxaajat.viiteapuri.IO;

import java.util.Scanner;

/**
 * Komentoriviä käyttävä IO-implememntaatio
 */

public class KomentoriviIO implements IO {

    private Scanner lukija;

    /**
     * Luo uuden komentoriviIOn
     */
    
    public KomentoriviIO() {
        lukija = new Scanner(System.in);
    }

    /**
     * Palauttaa seuraavan komentoriville syötettävän rivin.
     *
     * @param kehote Ennen käyttäjän syötteen ottamista tulostettava viesti.
     * @return Komentoriville syötetty rivi.
     */
    
    @Override
    public String lueRivi(String kehote) {
        System.out.print(kehote);
        return lukija.nextLine();
    }

    /**
     * Tulostaa parametrina saamansa tekstin ilman rivinvaihtoa.
     *
     * @param teksti Tulostettava teksti.
     */
    
    @Override
    public void tulosta(String teksti) {
        System.out.print(teksti);
    }
}
