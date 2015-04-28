package hammentyneethaxaajat.viiteapuri.validaattori;

import hammentyneethaxaajat.viiteapuri.viite.Viite;
import hammentyneethaxaajat.viiteapuri.viite.ViiteKasittelija;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;
import static hammentyneethaxaajat.viiteapuri.resurssit.Tulosteet.*;
import hammentyneethaxaajat.viiteapuri.viite.AttrTyyppi;
import hammentyneethaxaajat.viiteapuri.viite.ViiteTyyppi;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.fail;

public class ValidaattoriTest {

    Validaattori validaattori;
    ViiteKasittelija kasittelija;
    Viite kirjaViite;

    @Before
    public void setUp() {
        kasittelija = mock(ViiteKasittelija.class);
        validaattori = new Validaattori(kasittelija);
        kirjaViite = new Viite(ViiteTyyppi.book);
        kirjaViite.setAttribuutti(AttrTyyppi.crossref.name(), "");
    }
    
    @Test
    public void validoiTiedostonNimen() {
        try {
            validaattori.validoi(KYSY_TIEDOSTO_NIMI, "dokumentti");
        } catch (Exception e) {
            fail("Tiedoston nimen validointi epäonnistui");
        }
    }
    
    @Test
    public void validoiTiedostonPolun() {
        try {
            validaattori.validoi(KYSY_TIEDOSTO_POLKU, "bibtex/");
        } catch (Exception e) {
            fail("Tiedoston polun validointi epäonnistui");
        }
    }

    @Test
    public void validoiTyypin() {
        try {
            validaattori.validoi(TYYPPI, "book");
        } catch (Exception e) {
            fail("Tyypin validointi epäonnistui");
        }
    }

    @Test
    public void validoiNimen() {
        try {
            validaattori.validoi(BIBTEXAVAIN, "korvisten rouva");
        } catch (Exception e) {
            fail("Nimen validointi epäonnistui");
        }
    }

    @Test
    public void validoiRistiviite() {
        Viite viite = when(mock(Viite.class).getBibtexAvain()).thenReturn("aapinen").getMock();
        List<Viite> viitteet = new ArrayList<Viite>();
        viitteet.add(viite);

        when(kasittelija.getViitteet()).thenReturn(viitteet);

        try {
            validaattori.validoi("crossref", "aapinen");
        } catch (Exception e) {
            fail("Ristiviitteen validointi epäonnistui");
        }
    }
    
    @Test
    public void validoiAttribuutinTyypin() {
        try {
            validaattori.validoi(kirjaViite, ATTRIBUUTTI, AttrTyyppi.editor.name());
        } catch (Exception e) {
            fail("Attribuutin tyypin validointi epäonnistui.");
        }
    }
    
    @Test
    public void validoiAttribuutinNimen() {
        try {
            validaattori.validoi(kirjaViite, BIBTEXAVAIN, "Iivarin Seikkailut");
        } catch (Exception e) {
            fail("Attribuutin nimen validointi epäonnistui.");
        }
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void validoiAttribuutin() {
        validaattori.validoi(kirjaViite, AttrTyyppi.year.name(), "Iivarin Seikkailut");
    }

    @Test
    public void viiteTyypinValidointiOnnistuuTunnetuillaViiteTyypeilla() {
        validaattori.validoiViiteTyyppi("book");
    }

    @Test(expected = IllegalArgumentException.class)
    public void viiteTyypinValidointiHeittaaExceptioninJosTuntematonViiteTyyppi() {
        validaattori.validoiViiteTyyppi("kirja");
    }

    @Test
    public void viiteTyypinValidoinninHeittamaExceptionSisaltaaJarkevanIlmoituksen() {
        try {
            validaattori.validoiViiteTyyppi("kirja");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains(TUNTEMATON_VIITETYYPPI));
        }
    }

    @Test
    public void nimenValidointiOnnistuuJosSellaistaEiOleMuillaViitteillaJaSyntaksiOK() {
        validaattori.validoiBibtexAvain("asdas");
    }

    @Test(expected = IllegalArgumentException.class)
    public void nimenValidointiEiOnnistuJosToisellaViitteellaOnSamaNimi() {
        Viite viite = when(mock(Viite.class).getBibtexAvain()).thenReturn("aapinen").getMock();
        List<Viite> viitteet = new ArrayList<Viite>();
        viitteet.add(viite);

        when(kasittelija.getViitteet()).thenReturn(viitteet);
        validaattori.validoiBibtexAvain("aapinen");
    }

    @Test
    public void attribuutinvalidointiOnnistuuJosAttribuuttiOnMaarattyaMuotoa() {
        validaattori.validoiAttribuutinArvo("key", "123");
    }

    @Test(expected = IllegalArgumentException.class)
    public void attribuutinvalidointiEiOnnistuJosAttribuuttiEiOleMaarattyaMuotoa() {
        validaattori.validoiAttribuutinArvo("key", "123a");
    }

    @Test(expected = IllegalArgumentException.class)
    public void attribuutinvalidointiEiOnnistuJosValidoitavanNimellaOlevaaAttribuuttiaEiOlemassa() {
        validaattori.validoiAttribuutinArvo("attr", "abc");
    }

    @Test(expected = IllegalArgumentException.class)
    public void nimenOnVastattavaSyntaksia() {
        validaattori.validoi("nimi", "@%///&&##");
    }

    @Test
    public void ristiViitteenValidointiOnnistuuJosHaettavaTyhjaString() {
        validaattori.validoiViite("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void ristiViitteenValidointiEiOnnistuJosHaettavaaViiteEiOlemassaJaHaettavaEpaTyhja() {
        validaattori.validoiViite("kirja");
    }

    @Test(expected = IllegalArgumentException.class)
    public void heitaExceptionHeittaaIllegalArgumentExceptionin() {
        validaattori.heitaException(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void viiteEiSaaOllaTyhja() {
        validaattori.validoi("viite", "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void tuntematonValidoitavaHeittaaExceptionin() {
        validaattori.validoi("kukka", "ohai");
    }
    
    @Test
    public void attribuutinTyypinValidointiOnnistuuJosValidoitavaAttribuuttiViitteellaJaSeEiOleCrossref() {
        validaattori.validoiMuokattavanAttribuutinTyyppi(kirjaViite, AttrTyyppi.publisher.name());
        validaattori.validoiMuokattavanAttribuutinTyyppi(kirjaViite, AttrTyyppi.series.name());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void attribuutinTyypinValidointiEiOnnistuJosValidoitavaAttribuuttiCrossref() {
        validaattori.validoiMuokattavanAttribuutinTyyppi(kirjaViite, AttrTyyppi.crossref.name());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void attribuutinTyypinValidointiEiOnnistuJosViitteellaEiValidoitavaaAttribuuttiTyyppia() {
        validaattori.validoiMuokattavanAttribuutinTyyppi(kirjaViite, AttrTyyppi.edition.name());
    }
    
    @Test
    public void attribuutinValidointiOnnistuuJosTyyppiValinnainenJaArvoValidi() {
        validaattori.validoiViitteenAttribuutti(kirjaViite, AttrTyyppi.address.name(), "");
    }
    
    @Test
    public void attribuutinValidointiOnnistuuJosTyyppiPakollinenJaArvoValidi() {
        validaattori.validoiViitteenAttribuutti(kirjaViite, AttrTyyppi.title.name(), "joku kirja");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void attribuutinValidointiEiOnnistuJosTyyppiPakollinenJaArvoTyhja() {
        validaattori = new Validaattori(new ViiteKasittelija());
        validaattori.validoiViitteenAttribuutti(kirjaViite, AttrTyyppi.title.name(), "");
    }
    
}
