package hammentyneethaxaajat.viiteapuri.viite;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    }),
    inproceedings(new AttrTyyppi[] {
        AttrTyyppi.author, AttrTyyppi.title, AttrTyyppi.booktitle, AttrTyyppi.year
    }, new AttrTyyppi[] { AttrTyyppi.editor, AttrTyyppi.volume, AttrTyyppi.series, AttrTyyppi.pages, AttrTyyppi.address, AttrTyyppi.month,
                          AttrTyyppi.organization, AttrTyyppi.publisher, AttrTyyppi.note, AttrTyyppi.key        
    }),
    misc(new AttrTyyppi[] {},
        new AttrTyyppi[] { AttrTyyppi.author, AttrTyyppi.title, AttrTyyppi.howpublished, AttrTyyppi.month, AttrTyyppi.note, AttrTyyppi.key
    }),
    booklet(new AttrTyyppi[] { AttrTyyppi.title
    },  new AttrTyyppi[] { AttrTyyppi.author, AttrTyyppi.howpublished, AttrTyyppi.address, AttrTyyppi.month, AttrTyyppi.year, AttrTyyppi.note,
        AttrTyyppi.key
    }),
    mastersthesis( new AttrTyyppi[] { AttrTyyppi.author, AttrTyyppi.title, AttrTyyppi.school, AttrTyyppi.year
    },  new AttrTyyppi[] { AttrTyyppi.type, AttrTyyppi.address, AttrTyyppi.month, AttrTyyppi.note, AttrTyyppi.key
    }),
    phdthesis( new AttrTyyppi[] { AttrTyyppi.author, AttrTyyppi.title, AttrTyyppi.school, AttrTyyppi.year
    },  new AttrTyyppi[] { AttrTyyppi.type, AttrTyyppi.address, AttrTyyppi.month, AttrTyyppi.note, AttrTyyppi.key
    }),
    techreport( new AttrTyyppi[] { AttrTyyppi.author, AttrTyyppi.title, AttrTyyppi.institution, AttrTyyppi.year
    }, new AttrTyyppi[] { AttrTyyppi.type, AttrTyyppi.number, AttrTyyppi.address, AttrTyyppi.month, AttrTyyppi.note, AttrTyyppi.key
    }),
    proceedings( new AttrTyyppi[] { AttrTyyppi.title, AttrTyyppi.year
    }, new AttrTyyppi[] { AttrTyyppi.editor, AttrTyyppi.volume, AttrTyyppi.series, AttrTyyppi.address, AttrTyyppi.month, AttrTyyppi.publisher,
        AttrTyyppi.organization, AttrTyyppi.note, AttrTyyppi.key
    }),
    inbook( new AttrTyyppi[] { AttrTyyppi.author, AttrTyyppi.title, AttrTyyppi.pages, AttrTyyppi.publisher, AttrTyyppi.year
    }, new AttrTyyppi[] { AttrTyyppi.volume, AttrTyyppi.series, AttrTyyppi.type, AttrTyyppi.address, AttrTyyppi.edition, AttrTyyppi.month,
        AttrTyyppi.note, AttrTyyppi.key
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
     * Palauttaa kaikki viitteeseen liittyvät AttrTyypit.
     * @return Set joka sisältää kaikki ViiteTyypille sallitut AttrTyypit.
     */
    public Set<AttrTyyppi> getKaikki(){
        return Stream.concat(Arrays.stream(pakolliset), Arrays.stream(valinnaiset)).collect(Collectors.toSet());
    }
}
