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
    public void ViitetyypinenAsettaminenJaHakeminenToimii() {
        viite = new Viite();
        viite.setTyyppi(ViiteTyyppi.book);
        
        assertEquals(ViiteTyyppi.book, viite.getTyyppi());
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
    
    @Test
    public void listausListaaViitteetSelkokielisessaMuodossa() {
        viite = new Viite();
        viite.setNimi("Viite #1");
        viite.setTyyppi(ViiteTyyppi.book);
        viite.setAttribuutti("year", "2345");
        viite.setAttribuutti("month", "tammikuu");
        viite.setAttribuutti("author", "erkki");
        
        assertTrue(viite.listaus().contains("nimi: Viite #1"));
        assertTrue(viite.listaus().contains("tyyppi: book"));
        assertTrue(viite.listaus().contains("author: erkki"));
        assertTrue(viite.listaus().contains("month: tammikuu"));
        assertTrue(viite.listaus().contains("year: 2345"));
    }
}