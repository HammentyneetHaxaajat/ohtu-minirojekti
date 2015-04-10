package hammentyneethaxaajat.viiteapuri.validaattori;

import hammentyneethaxaajat.viiteapuri.viite.Viite;
import hammentyneethaxaajat.viiteapuri.viite.ViiteKasittelija;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;
import resurssit.Tulosteet;

public class ValidaattoriTest {
    Validaattori validaattori;
    Validaattori mockDaattori;
    String aapinen = "aapinen";
    
    @Before
    public void setUp() {
        Viite viite = new Viite();
        viite.setNimi(aapinen);
        ViiteKasittelija kasittelija = new ViiteKasittelija();
        kasittelija.lisaaViite(viite);
        
        validaattori = new Validaattori(kasittelija);
        mockDaattori = mock(Validaattori.class);
    }
    
//    @Test
//    public void validoiParametrinaTyyppiValidoiTyypin() {
//        mockDaattori.validoi("tyyppi", "book");
//        verify(mockDaattori, times(1)).validoiViiteTyyppi(eq("book"));
//    }
//    
//    @Test
//    public void validoiParametrinaNimiValidoiNimen() {
//        mockDaattori.validoi("nimi", "abc");
//        verify(mockDaattori, times(1)).validoiNimi(eq("abc"));
//    }
//    
//    @Test
//    public void validoiParametrinaCrossrefValidoiRistiviitteen() {
//        mockDaattori.validoi("crossref", "abc");
//        verify(mockDaattori, times(1)).validoiRistiviite(eq("abc"));
//    }
//    
//    @Test
//    public void validoiParametrinaJotainMuutaValidoiAttribuutin() {
//        mockDaattori.validoi("author", "abc");
//        verify(mockDaattori, times(1)).validoiAttribuutti(eq("author"), eq("abc"));
//    }

    @Test
    public void viiteTyypinValidointiOnnistuuTunnetuillaViiteTyypeilla() {
        validaattori.validoiViiteTyyppi("book");
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void viiteTyypinValidointiHeittaaExceptioninJosTuntematonViiteTyyppi() {
        validaattori.validoiViiteTyyppi("kirja");
    }
    
    @Test
    public void viiteTyypinValidoinninHeittamaExceptionSisaltaaJarkevanIlmoituksen() {
        try {
            validaattori.validoiViiteTyyppi("kirja");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains(Tulosteet.TUNTEMATON_VIITETYYPPI));
        }
    }
    
    @Test
    public void nimenValidointiOnnistuuJosSellaistaEiOleMuillaViitteillaJaSyntaksiOK() {
        validaattori.validoiNimi("asdas");
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void nimenValidointiEiOnnistuJosToisellaViitteellaOnSamaNimi() {
        validaattori.validoiNimi(aapinen);
    }
    
//    @Test (expected = IllegalArgumentException.class)
//    public void nimenValidointiEiOnnistuJosNimiEiNoudataSilleMaarattyaSyntaksia() {
//        validaattori.validoiNimi("12.*/sdfklj");
//    }
    
    @Test
    public void attribuutinvalidointiOnnistuuJosAttribuuttiOnMaarattyaMuotoa() {
        validaattori.validoiAttribuutti("key", "123");
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void attribuutinvalidointiEiOnnistuJosAttribuuttiEiOleMaarattyaMuotoa() {
        validaattori.validoiAttribuutti("key", "123a");
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void attribuutinvalidointiEiOnnistuJosValidoitavanNimellaOlevaaAttribuuttiaEiOlemassa() {
        validaattori.validoiAttribuutti("attr", "abc");
    }
    
    @Test
    public void ristiViitteenValidointiOnnistuuJosHaettavaViiteOlemassa() {
        validaattori.validoiRistiviite(aapinen);
    }
    
    @Test
    public void ristiViitteenValidointiOnnistuuJosHaettavaTyhjaString() {
        validaattori.validoiRistiviite("");
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void ristiViitteenValidointiEiOnnistuJosHaettavaaViiteEiOlemassaJaHaettavaEpaTyhja() {
        validaattori.validoiRistiviite("kirja");
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void heitaExceptionHeittaaIllegalArgumentExceptionin() {
        validaattori.heitaException(null);
    }
}
