package hammentyneethaxaajat.viiteapuri.UI.toiminnot;

import hammentyneethaxaajat.viiteapuri.IO.IO;
import hammentyneethaxaajat.viiteapuri.resurssit.Tulosteet;
import hammentyneethaxaajat.viiteapuri.validaattori.Validoija;
import hammentyneethaxaajat.viiteapuri.viite.Viite;
import hammentyneethaxaajat.viiteapuri.viite.ViiteKasittelija;
import java.util.Collection;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ListaaViitteetTest {

    @Mock
    private ViiteKasittelija viiteKasittelija;
    @Mock
    private Validoija validaattori;
    @Mock
    private IO io;

    ListaaViitteet toiminto;

    public ListaaViitteetTest() {
    }

    @Before
    public void setUp() {
        toiminto = new ListaaViitteet(io, viiteKasittelija, validaattori);
    }

    @Test
    public void eiTeeMitaanTyhjana() {
        Collection<Viite> viitteet = when(mock(Collection.class).isEmpty()).thenReturn(Boolean.TRUE).getMock();
        when(viiteKasittelija.getViitteet()).thenReturn(viitteet);
        toiminto.suorita();
        verify(viiteKasittelija).getViitteet();
        verifyNoMoreInteractions(viiteKasittelija);
        verifyZeroInteractions(validaattori);

    }

    public void pyytaaTulostuksenEpatyhjana() {
        Collection<Viite> viitteet = when(mock(Collection.class).isEmpty()).thenReturn(Boolean.FALSE).getMock();
        when(viiteKasittelija.getViitteet()).thenReturn(viitteet);
        toiminto.suorita();
        verify(viiteKasittelija).viitteetListauksena();
    }

    @Test
    public void testToString() {
        assertTrue(toiminto.toString().equals(Tulosteet.KOMENTO_UUSI));
    }

}
