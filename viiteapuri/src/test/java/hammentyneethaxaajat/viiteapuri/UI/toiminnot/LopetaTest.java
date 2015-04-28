
package hammentyneethaxaajat.viiteapuri.UI.toiminnot;

import hammentyneethaxaajat.viiteapuri.IO.IO;
import hammentyneethaxaajat.viiteapuri.UI.Tekstikayttoliittyma;
import hammentyneethaxaajat.viiteapuri.resurssit.Tulosteet;
import hammentyneethaxaajat.viiteapuri.validaattori.Validoija;
import hammentyneethaxaajat.viiteapuri.viite.ViiteKasittelija;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class LopetaTest {
    
    @Mock
    private ViiteKasittelija viiteKasittelija;
    @Mock
    private Validoija validaattori;
    @Mock
    private IO io;
    @Mock
    private Tekstikayttoliittyma ui;
    
    private Lopeta toiminto;
    
    public LopetaTest() {
    }
    
    @Before
    public void setUp() {
        toiminto = new Lopeta(io, viiteKasittelija, validaattori, ui);
    }

    @Test
    public void testSuorita() {
        toiminto.suorita();
        verify(ui).abort();
    }

    @Test
    public void testToString() {
        assertTrue(toiminto.toString().equals(Tulosteet.KOMENTO_LOPETA));
    }
    
}
