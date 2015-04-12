package hammentyneethaxaajat.viiteapuri.viite;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Viitteen tyyppiä kuvaava luokka, joka määrittää Viitteelle pakolliset ja sallitut atribuutit.
 */

public enum ViiteTyyppi {

    //TODO keksi parempi tapa saaada nämä listat seteiksi.
    book(new AttrTyyppi[] {
        AttrTyyppi.author, AttrTyyppi.editor, AttrTyyppi.title, AttrTyyppi.publisher, AttrTyyppi.year
    }, new AttrTyyppi[] {
        AttrTyyppi.volume, AttrTyyppi.number, AttrTyyppi.series, AttrTyyppi.address, AttrTyyppi.month, AttrTyyppi.note, AttrTyyppi.key
    });

    private Set<AttrTyyppi> pakolliset;
    private Set<AttrTyyppi> valinnaiset;

    /**
     * Privaatti konstruktori, joka luo viitetyypin (enum) joka sisältää
     * listan pakollisia ja valinnaisia attribuutteja (AttrTyyppi).
     * @param pakolliset Pakollisten attribuuttien taulukko.
     * @param valinnaiset Valinnaisten attribuuttien taulukko.
     */
    
    private ViiteTyyppi(AttrTyyppi[] pakolliset, AttrTyyppi[] valinnaiset) {
        this.pakolliset = attribuutit(pakolliset);
        this.valinnaiset = attribuutit(valinnaiset);
    }

    /**
     * Palauttaa setin pakollisista Attribuuttityypeistä.
     * @return Set, joka sisältää pakollisten attribuuttien tyypit.
     */
    public Set<AttrTyyppi> getPakolliset() {
        return pakolliset;
    }

    /**
     * Palauttaa setin valinnaisista Attribuuttityypeistä.
     * @return Set, joka sisältää valinnaisten attribuuttien tyypit.
     */
    
    public Set<AttrTyyppi> getValinnaiset() {
        return valinnaiset;
    }

    /**
     * Palauttaa listan attribuutit Set:inä.
     * @param lista Attribuutti-taulukko
     * @return Paluatettava lista Set:inä.
     */
    
    protected Set<AttrTyyppi> attribuutit(AttrTyyppi[] lista) {
        return Arrays.stream(lista).collect(Collectors.toSet());
    }
}
