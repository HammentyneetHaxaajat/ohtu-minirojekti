package hammentyneethaxaajat.viiteapuri;

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
     * Tulostaa parametrina saadun Stringin komentoriville.
     *
     * @param teksti Tulostettava teksti.
     */
    @Override
    public void tulosta(String teksti) {
        System.out.print(teksti);
    }

    /**
     * Palauttaa seuraavan komentoriville syötettävän rivin.
     *
     * @return Komentoriville syötetty rivi.
     */
    @Override
    public String seuraavaRivi() {
        return lukija.nextLine();
    }

    @Override
    public void tulostaRivi(String teksti) {
        System.out.println(teksti);
    }
}
