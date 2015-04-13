package hammentyneethaxaajat.viiteapuri.validaattori;

import hammentyneethaxaajat.viiteapuri.viite.Viite;
import hammentyneethaxaajat.viiteapuri.viite.ViiteKasittelija;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;
import hammentyneethaxaajat.viiteapuri.resurssit.Tulosteet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import static org.junit.Assert.fail;
import org.junit.Rule;
import org.junit.internal.runners.statements.Fail;


public class ValidaattoriTest {
    Validaattori validaattori;
    ViiteKasittelija kasittelija;
    
    
    @Before
    public void setUp() {
        kasittelija = mock(ViiteKasittelija.class);
        validaattori = new Validaattori(kasittelija);
        
    }
    
    @Test
    public void validoiTyypin() {
        try {
            validaattori.validoi("tyyppi", "book");
        } catch (Exception e) {
            fail("Tyypin validointi epäonnistui");
        }
    }
    
    @Test
    public void validoiNimen() {
        try {
            validaattori.validoi("nimi", "korvisten rouva");
        } catch (Exception e) {
            fail("Nimen validointi epäonnistui");
        }
    }
    
    @Test
    public void validoiRistiviite() {
        Viite viite = when(mock(Viite.class).getNimi()).thenReturn("aapinen").getMock();
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
    public void validoiAttribuutti() {
        try {
            validaattori.validoi("series", "my little pony");
        } catch (Exception e) {
            fail("Attribuutin validointi epäonnistui");
        }
    }

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
        Viite viite = when(mock(Viite.class).getNimi()).thenReturn("aapinen").getMock();
        List<Viite> viitteet = new ArrayList<Viite>();
        viitteet.add(viite);
        
        when(kasittelija.getViitteet()).thenReturn(viitteet);
        validaattori.validoiNimi("aapinen");
    }
    
    
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
    
    @Test (expected = IllegalArgumentException.class)
    public void nimenOnVastattavaSyntaksia() {
        validaattori.validoi("nimi", "@%///&&##");
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
