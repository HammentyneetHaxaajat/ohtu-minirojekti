package hammentyneethaxaajat.viiteapuri.UI.toiminnot;

import hammentyneethaxaajat.viiteapuri.IO.IO;
import hammentyneethaxaajat.viiteapuri.resurssit.Tulosteet;
import static hammentyneethaxaajat.viiteapuri.resurssit.Tulosteet.ATTRIBUUTTI;
import static hammentyneethaxaajat.viiteapuri.resurssit.Tulosteet.VIITE;
import hammentyneethaxaajat.viiteapuri.validaattori.Validoija;
import hammentyneethaxaajat.viiteapuri.viite.Viite;
import hammentyneethaxaajat.viiteapuri.viite.ViiteKasittelija;
import java.util.Collection;
import java.util.HashSet;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.contains;
import static org.mockito.Matchers.eq;
import org.mockito.Mock;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PoistaViiteTest {

    public PoistaViiteTest() {
    }

    @Mock
    private ViiteKasittelija viiteKasittelija;
    @Mock
    private Validoija validaattori;
    @Mock
    private IO io;

    private PoistaViite toiminto;

    @Before
    public void setUp() {
        toiminto = new PoistaViite(io, viiteKasittelija, validaattori);
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
        when(io.lueRivi(contains(Tulosteet.KYSY_VARMISTUS))).thenReturn("poista");
        when(viite1.listaus()).thenReturn("");
        when(viiteKasittelija.kopioiTiedotViittaajiin(Matchers.anyObject())).thenReturn(Boolean.TRUE);
        toiminto.suorita();
        verify(viiteKasittelija).poistaViite(eq(viite1));
        verify(viiteKasittelija).kopioiTiedotViittaajiin(eq(viite1));
    }

    @Test
    public void tekeeJotainMuutakinJosViitteita() {
        Viite viite1 = when(mock(Viite.class).getBibtexAvain()).thenReturn("omena").getMock();
        Viite viite2 = when(mock(Viite.class).getBibtexAvain()).thenReturn("sieni").getMock();
        Collection<Viite> viitteet = new HashSet<Viite>();
        viitteet.add(viite2);
        viitteet.add(viite1);
        when(viiteKasittelija.getViitteet()).thenReturn(viitteet);
        when(viiteKasittelija.haeViite(anyString())).thenReturn(viite1);
        when(io.lueRivi(contains(VIITE))).thenReturn("omena");
        when(io.lueRivi(contains(Tulosteet.KYSY_VARMISTUS))).thenReturn("poista");
        when(viite1.listaus()).thenReturn("");
        when(viiteKasittelija.kopioiTiedotViittaajiin(Matchers.anyObject())).thenReturn(Boolean.FALSE);
        toiminto.suorita();
        verify(viiteKasittelija).poistaViite(eq(viite1));
    }

    @Test
    public void EiPoistaJosKayttajaPeruu() {
        Viite viite1 = when(mock(Viite.class).getBibtexAvain()).thenReturn("omena").getMock();
        Viite viite2 = when(mock(Viite.class).getBibtexAvain()).thenReturn("sieni").getMock();
        Collection<Viite> viitteet = new HashSet<Viite>();
        viitteet.add(viite2);
        viitteet.add(viite1);
        when(viiteKasittelija.getViitteet()).thenReturn(viitteet);
        when(viiteKasittelija.haeViite(anyString())).thenReturn(viite1);
        when(io.lueRivi(contains(VIITE))).thenReturn("omena");
        when(io.lueRivi(contains(Tulosteet.KYSY_VARMISTUS))).thenReturn("en poista");
        when(viite1.listaus()).thenReturn("");
        when(viiteKasittelija.kopioiTiedotViittaajiin(Matchers.anyObject())).thenReturn(Boolean.FALSE);
        toiminto.suorita();
        verify(viiteKasittelija, never()).poistaViite(eq(viite1));
        verify(viiteKasittelija, never()).kopioiTiedotViittaajiin(eq(viite1));
        verify(io).tulosta(eq("Viitettä ei poistettu.\n"));
    }

    @Test
    public void testToString() {
        assertTrue(toiminto.toString().equals(Tulosteet.KOMENTO_POISTA));
    }

}
