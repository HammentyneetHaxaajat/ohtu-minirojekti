package hammentyneethaxaajat.viiteapuri.UI.toiminnot;

import hammentyneethaxaajat.viiteapuri.IO.BibtexIO;
import hammentyneethaxaajat.viiteapuri.IO.IO;
import hammentyneethaxaajat.viiteapuri.resurssit.Tulosteet;
import static hammentyneethaxaajat.viiteapuri.resurssit.Tulosteet.KOMENTO_LUO_BIBTEX;
import hammentyneethaxaajat.viiteapuri.validaattori.Validoija;
import hammentyneethaxaajat.viiteapuri.viite.Viite;
import hammentyneethaxaajat.viiteapuri.viite.ViiteKasittelija;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import javax.imageio.IIOException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import static org.mockito.Matchers.*;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class luoBibtexTest {

    @Mock
    ViiteKasittelija viiteKasittelija;
    @Mock
    Validoija validaattori;
    @Mock
    IO io;
    @Mock
    BibtexIO bio;

    Collection<Viite> viitteet;
    luoBibtex toiminto;

    public luoBibtexTest() {
    }

    @Before
    public void setUp() {
        viitteet = spy(new ArrayList<>());
        when(viiteKasittelija.getViitteet()).thenReturn(viitteet);
        toiminto = new luoBibtex(io, viiteKasittelija, validaattori, bio);
    }

    @Test
    public void tulostusTyhjallaNimella() throws IOException {
        when(io.lueRivi(anyString())).thenReturn("");
        when(viitteet.isEmpty()).thenReturn(Boolean.FALSE);
        when(viiteKasittelija.viitteetBibtexina()).thenReturn("hehe");
        toiminto.suorita();
        verify(bio).kirjoitaBibtex(eq("hehe"), eq("viitteet"));
        verify(io, never()).tulosta(eq("Huom! Luotu tiedosto ei sisällä yhtään viitettä.\n"));
    }
    
    @Test
    public void tulostusEpaTyhjallaNimella() throws IOException {
        when(io.lueRivi(anyString())).thenReturn("meh");
        when(viitteet.isEmpty()).thenReturn(Boolean.TRUE);
        when(viiteKasittelija.viitteetBibtexina()).thenReturn("hehe");
        toiminto.suorita();
        verify(bio).kirjoitaBibtex(eq("hehe"), eq("meh"));
        verify(io).tulosta(eq("Huom! Luotu tiedosto ei sisällä yhtään viitettä.\n"));
    }
    
      @Test
    public void kertooVirheen() throws IOException {
        when(io.lueRivi(anyString())).thenReturn("meh");
        when(viitteet.isEmpty()).thenReturn(Boolean.FALSE);
        when(viiteKasittelija.viitteetBibtexina()).thenReturn("hehe");
          doThrow(new IIOException("meh")).when(bio).kirjoitaBibtex(anyString(), anyString());
        toiminto.suorita();
        verify(io).tulosta(eq(Tulosteet.TIEDOSTONLUONTI_EI_ONNISTUNUT));
    }

    @Test
    public void testToString() {
        assertTrue(toiminto.toString().equals(KOMENTO_LUO_BIBTEX));
    }

}
