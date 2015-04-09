package hammentyneethaxaajat.viiteapuri.viite;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

public class ViiteTest {
    Viite viite;
    
    @Before
    public void setUp() {
        viite = new Viite();
    }
    
    @Test
    public void setAttribuuttiLuoViitteelleUudenAttribuutinJosSellaistaEiOle() {
        viite.setAttribuutti("number", "1243");
        assertEquals(1, viite.getAttribuutit().size());
    }
    
    @Test
    public void setAttribuuttiMuokkaaViitteenAttribuuttiaJosSellainenViitteellaOn() {
        viite = new Viite();
        viite.setAttribuutti("number", "1243");
        assertEquals(1, viite.getAttribuutit().size());
        
        viite.setAttribuutti("number", "abc");
        assertEquals(1, viite.getAttribuutit().size());
        assertTrue(viite.getAttribuutti("number").toString().contains("abc"));
    }
    
    
}
