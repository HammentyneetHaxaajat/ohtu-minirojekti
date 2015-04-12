package hammentyneethaxaajat.viiteapuri.viite;

import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

public class ViiteKasittelijaTest {
    ViiteKasittelija kasittelija;
    
    @Before
    public void setUp() {
        kasittelija = new ViiteKasittelija();
    }
    
    @Test
    public void lisaaViiteLisaaViitteenKasittelijanTuntemiinViitteisiin() {
        lisaaViite();
        assertTrue(kasittelija.getViitteet().size() == 1);
    }
    
    @Test (expected = IllegalStateException.class)
    public void lisaaViiteHeittaaExceptioninJosSamanNiminenViiteJoOlemassa() {
        setUp();
        lisaaViite();
        lisaaViite();
    }
    
    private void lisaaViite() {
        Viite viite = new Viite();
        viite.setNimi("viite 1");
        kasittelija.lisaaViite(viite);
    }
    
//    @Test
//    public void viitteetListauksenaPalauttaaViitteetSelkokielisenaListana() {
//        // TODO ellei pelkästään easyB -testillä joka tekee kyllä saman asian
//        
//        assertTrue(false);
//    }
}
