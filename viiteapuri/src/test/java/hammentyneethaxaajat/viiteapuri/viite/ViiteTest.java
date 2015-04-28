package hammentyneethaxaajat.viiteapuri.viite;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

public class ViiteTest {
    Viite viite;
    
    @Before
    public void setUp() {
    }
    
    @Test
    public void ViitetyypinenAsettaminenJaHakeminenToimii() {
        viite = new Viite(ViiteTyyppi.book);
        
        assertEquals(ViiteTyyppi.book, viite.getTyyppi());
    }
    
    
    @Test
    public void setAttribuuttiMuokkaaViitteenAttribuuttiaJosSellainenViitteellaOn() {
        viite = new Viite(ViiteTyyppi.book); 
        assertTrue(viite.getAttribuutti("number").getArvo().isEmpty());
        viite.setAttribuutti("number", "abc");
        assertTrue(viite.getAttribuutti("number").getArvo().contains("abc"));
    }
    
    @Test
    public void listausListaaViitteetSelkokielisessaMuodossa() {
        viite = new Viite(ViiteTyyppi.book);
        viite.setBibtexAvain("Viite #1");
        viite.setAttribuutti("year", "2345");
        viite.setAttribuutti("month", "tammikuu");
        viite.setAttribuutti("author", "erkki");
        
        assertTrue(viite.listaus().contains("bibtexavain: Viite #1"));
        assertTrue(viite.listaus().contains("tyyppi: book"));
        assertTrue(viite.listaus().contains("author: erkki"));
        assertTrue(viite.listaus().contains("month: tammikuu"));
        assertTrue(viite.listaus().contains("year: 2345"));
    }
}