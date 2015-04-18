package hammentyneethaxaajat.viiteapuri.viite;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.lang3.ArrayUtils;

/**
 * Viitteen tyyppiä kuvaava luokka, joka määrittää Viitteelle pakolliset ja sallitut atribuutit.
 */

public enum ViiteTyyppi {

    book(new AttrTyyppi[] {
        AttrTyyppi.author, AttrTyyppi.editor, AttrTyyppi.title, AttrTyyppi.publisher, AttrTyyppi.year
    }, new AttrTyyppi[] {
        AttrTyyppi.volume, AttrTyyppi.number, AttrTyyppi.series, AttrTyyppi.address, AttrTyyppi.month, AttrTyyppi.note, AttrTyyppi.key
    }),
    article(new AttrTyyppi[] {
        AttrTyyppi.author, AttrTyyppi.title, AttrTyyppi.journal, AttrTyyppi.year, AttrTyyppi.volume
    }, new AttrTyyppi[] {
        AttrTyyppi.number, AttrTyyppi.pages, AttrTyyppi.month, AttrTyyppi.note, AttrTyyppi.key
    });

    private AttrTyyppi[] pakolliset;
    private AttrTyyppi[] valinnaiset;

    /**
     * Privaatti konstruktori, joka luo viitetyypin (enum) joka sisältää
     * listan pakollisia ja valinnaisia attribuutteja (AttrTyyppi).
     * @param pakolliset Pakollisten attribuuttien taulukko.
     * @param valinnaiset Valinnaisten attribuuttien taulukko.
     */
    
    private ViiteTyyppi(AttrTyyppi[] pakolliset, AttrTyyppi[] valinnaiset) {
        this.pakolliset = pakolliset;
        this.valinnaiset = valinnaiset;
    }

    /**
     * Palauttaa setin pakollisista Attribuuttityypeistä.
     * @return Set, joka sisältää pakollisten attribuuttien tyypit.
     */
    public Set<AttrTyyppi> getPakolliset() {
        return Arrays.stream(pakolliset).collect(Collectors.toSet());
    }

    /**
     * Palauttaa setin valinnaisista Attribuuttityypeistä.
     * @return Set, joka sisältää valinnaisten attribuuttien tyypit.
     */
    
    public Set<AttrTyyppi> getValinnaiset() {
        return Arrays.stream(valinnaiset).collect(Collectors.toSet());
    }

    /**
     * Palauttaa arrayn attribuutit Set:inä.
     * @param lista Attribuutti-taulukko
     * @return Paluatettava lista Set:inä.
     */
    
    protected Set<AttrTyyppi> attribuutit(AttrTyyppi[] lista) {
        return Arrays.stream(lista).collect(Collectors.toSet());
    }
    
    /**
     * Palauttaa kaikki viitteeseen liittyvät AttrTyypit.
     * @return Set joka sisältää kaikki ViiteTyypille sallitut AttrTyypit.
     */
    public Set<AttrTyyppi> getKaikki(){
        return Arrays.stream(ArrayUtils.addAll(pakolliset, valinnaiset)).collect(Collectors.toSet());
    }
}
