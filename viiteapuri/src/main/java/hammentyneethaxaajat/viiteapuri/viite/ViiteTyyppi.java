package hammentyneethaxaajat.viiteapuri.viite;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Viitteen tyyppi, joka määrittää sille pakolliset ja sallitut atribuutit.
 */
public enum ViiteTyyppi {

    //TODO keksi parempi tapa saaada nämä listat seteiksi.
    book(new AttrTyyppi[]{
        AttrTyyppi.author, AttrTyyppi.editor, AttrTyyppi.title, AttrTyyppi.publisher, AttrTyyppi.year
    }, new AttrTyyppi[]{
        AttrTyyppi.volume, AttrTyyppi.number, AttrTyyppi.series, AttrTyyppi.address, AttrTyyppi.month, AttrTyyppi.note, AttrTyyppi.key
    });

    private Set<AttrTyyppi> pakolliset;
    private Set<AttrTyyppi> valinnaiset;

    private ViiteTyyppi(AttrTyyppi[] pakolliset, AttrTyyppi[] valinnaiset) {
        this.pakolliset = Arrays.stream(pakolliset).collect(Collectors.toSet());
        this.valinnaiset = Arrays.stream(valinnaiset).collect(Collectors.toSet());
    }

    /**
     * Palauttaa setin pakollisista Atributtityypeistä
     *
     * @return Set Set, joka sisältää pakollisten attribuuttien tyypit.
     */
    public Set<AttrTyyppi> getPakolliset() {
        return pakolliset;
    }

    /**
     * Palauttaa setin valinnaisista Atributtityypeistä
     *
     * @return Set Set, joka sisältää valinnaisten attribuuttien tyypit.
     */
    public Set<AttrTyyppi> getValinnaiset() {
        return valinnaiset;
    }

}
