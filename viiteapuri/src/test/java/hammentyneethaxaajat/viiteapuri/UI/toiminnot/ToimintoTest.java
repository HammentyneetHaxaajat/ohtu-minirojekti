package hammentyneethaxaajat.viiteapuri.UI.toiminnot;

import hammentyneethaxaajat.viiteapuri.IO.IO;
import hammentyneethaxaajat.viiteapuri.validaattori.Validoija;
import hammentyneethaxaajat.viiteapuri.viite.AttrTyyppi;
import hammentyneethaxaajat.viiteapuri.viite.Viite;
import hammentyneethaxaajat.viiteapuri.viite.ViiteKasittelija;
import hammentyneethaxaajat.viiteapuri.viite.ViiteTyyppi;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collection;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import static org.mockito.Matchers.*;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ToimintoTest {

    @Mock
    ViiteKasittelija viiteKasittelija;
    @Mock
    Validoija validaattori;
    @Mock
    IO io;

    ToimintoImpl toiminto;

    public ToimintoTest() {
    }

    @Before
    public void setUp() {
        toiminto = new ToimintoImpl(io, viiteKasittelija, validaattori);
    }

    @Test
    public void testaaKysele() {
        toiminto.kysele("test");
        verify(io).lueRivi(contains("test"));
    }

    @Test
    public void testaaVirheenTulostus() {
        toiminto.tulostaVirheIlmoitus(new InvalidParameterException("kek"));
        verify(io).tulosta(contains("kek"));
    }

    @Test
    public void testOhjelmassaViitteita() {
        Collection<Viite> viitteet = when(mock(Collection.class).isEmpty()).thenReturn(Boolean.TRUE, Boolean.FALSE).getMock();
        when(viiteKasittelija.getViitteet()).thenReturn(viitteet);
        assertFalse(toiminto.ohjelmassaViitteita());
        assertTrue(toiminto.ohjelmassaViitteita());
    }

    @Test
    public void testHaeViite() {
        when(io.lueRivi(anyString())).thenReturn("ahaha");
        toiminto.haeViiteKayttajanSyotteenPerusteella();
        verify(viiteKasittelija).haeViite(eq("ahaha"));
    }

    @Test
    public void testListaa() {
        Collection<Viite> viitteet = new ArrayList<>();
        Viite viite = when(mock(Viite.class).getBibtexAvain()).thenReturn("").getMock();
        viitteet.add(viite);
        viitteet.add(viite);
        viitteet.add(viite);
        when(viiteKasittelija.getViitteet()).thenReturn(viitteet);
        toiminto.listaaViitteet("meh");
        verify(viite, times(3)).getBibtexAvain();
        verify(io).tulosta(contains("meh"));
    }

    @Test
    public void testHankivalidi() {
        doThrow(new IllegalArgumentException("ehhehe")).doNothing().when(validaattori).validoi(anyString(), anyString());
        when(io.lueRivi(anyString())).thenReturn("meh");
        toiminto.hankiValidiSyote("", false);
        verify(validaattori, times(2)).validoi(anyString(), anyString());
        verify(io).tulosta(contains("ehhehe"));
        toiminto.hankiValidiSyote("", true);
        verify(validaattori, times(3)).validoi(anyString(), anyString());
    }
    
    @Test
    public void testHankivalidiViite() {
        doThrow(new IllegalArgumentException("ehhehe")).doNothing().when(validaattori).validoi(anyObject(),anyString(), anyString());
        when(io.lueRivi(anyString())).thenReturn("meh");
        toiminto.hankiValidiSyote(null, "", true);
        verify(validaattori, times(2)).validoi(anyObject() ,anyString(), anyString());
        verify(io).tulosta(contains("ehhehe"));
        toiminto.hankiValidiSyote(null, "", false);
        verify(validaattori, times(3)).validoi(anyObject() ,anyString(), anyString());
    }
    
    @Test
    public void hankiJaAseta(){
        Viite viite = mock(Viite.class);
        Collection<AttrTyyppi> attribuutit = ViiteTyyppi.book.getPakolliset();
        when(io.lueRivi(anyString())).thenReturn("haha");
        toiminto.hankiJaAsetaAttribuuttienArvot(attribuutit, viite);
        verify(viite, times(attribuutit.size())).setAttribuutti(anyString(), eq("haha"));
    }

    public class ToimintoImpl extends Toiminto {

        public ToimintoImpl(IO io, ViiteKasittelija viitekasittelija, Validoija validaattori) {
            super(io, viitekasittelija, validaattori);
        }

        public void suorita() {
        }

        public String toString() {
            return "";
        }
    }

}
