package hammentyneethaxaajat.viiteapuri.UI.toiminnot;

import hammentyneethaxaajat.viiteapuri.IO.IO;
import static hammentyneethaxaajat.viiteapuri.resurssit.Tulosteet.*;
import hammentyneethaxaajat.viiteapuri.validaattori.Validoija;
import hammentyneethaxaajat.viiteapuri.viite.Viite;
import hammentyneethaxaajat.viiteapuri.viite.ViiteKasittelija;
import java.util.Collection;
import java.util.HashSet;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import static org.mockito.Matchers.*;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MuokkaaViitettaTest {

    @Mock
    private ViiteKasittelija viiteKasittelija;
    @Mock
    private Validoija validaattori;
    @Mock
    private IO io;

    @InjectMocks
    MuokkaaViitetta toiminto;

    public MuokkaaViitettaTest() {
    }

    @Before
    public void setUp() {
        toiminto = new MuokkaaViitetta(io, viiteKasittelija, validaattori);
    }

    @Test
    public void eiTeeMitaanJosEiViitteita() {
        Collection<Viite> viitteet = when(mock(Collection.class).isEmpty()).thenReturn(Boolean.TRUE).getMock();
        when(viiteKasittelija.getViitteet()).thenReturn(viitteet);
        toiminto.suorita();
        verify(viiteKasittelija).getViitteet();
        verifyNoMoreInteractions(viiteKasittelija);
        verifyZeroInteractions(validaattori);
    }

    @Test
    public void tekeeJotainJosViitteita() {
        Viite viite1 = when(mock(Viite.class).getBibtexAvain()).thenReturn("omena").getMock();
        Viite viite2 = when(mock(Viite.class).getBibtexAvain()).thenReturn("sieni").getMock();
        Collection<Viite> viitteet = new HashSet<Viite>();
        viitteet.add(viite2);
        viitteet.add(viite1);
        when(viiteKasittelija.getViitteet()).thenReturn(viitteet);
        when(viiteKasittelija.haeViite(anyString())).thenReturn(viite1);
        when(io.lueRivi(contains(VIITE))).thenReturn("omena");
        when(io.lueRivi(contains("a"))).thenReturn("uusi");
        when(io.lueRivi(contains(ATTRIBUUTTI))).thenReturn("a");
        when(viite1.listaus()).thenReturn("");
        toiminto.suorita();
        verify(viiteKasittelija).paivitaArvo(eq(viite1), eq("a"), eq("uusi"));
        verify(viiteKasittelija, atLeastOnce()).getViitteet();
        verify(viite1, atLeastOnce()).listaus();
    }

    @Test
    public void testToString() {
        assertTrue(toiminto.toString().equals(KOMENTO_MUOKKAA));
    }

}
