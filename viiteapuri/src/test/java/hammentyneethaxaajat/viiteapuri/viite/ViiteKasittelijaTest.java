package hammentyneethaxaajat.viiteapuri.viite;

import java.util.Collection;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ViiteKasittelijaTest {

    ViiteKasittelija kasittelija;

    @Before
    public void setUp() {
        kasittelija = new ViiteKasittelija();
    }

    @Test
    public void lisaaViiteLisaaViitteenKasittelijanTuntemiinViitteisiin() {
        lisaaViite("viite");
        assertTrue(kasittelija.getViitteet().size() == 1);
    }
    
    @Test
    public void lisaaViiteLisaaViitteenSilleAnnetullaNimella() {
        lisaaViite("viite");
        assertTrue(kasittelija.getViitteet().stream().anyMatch(v -> v.getBibtexAvain().equals("viite")));
    }
    
    @Test
    public void lisaaViiteGeneroiNimenViitteelleJollaEiNimea() {
        setUp();
        Viite viite = lisaaViite("");
        
        assertFalse(viite.getBibtexAvain().isEmpty());
    }
    
    @Test
    public void generoiAvainEiAuthoriaEiEditoriaEiYearia() {
        setUp();
        Viite viite = new Viite();
        
        assertEquals("a", kasittelija.generoiAvain(viite));
        lisaaViite("");
        assertEquals("b", kasittelija.generoiAvain(viite));
        lisaaViite("");
        assertEquals("c", kasittelija.generoiAvain(viite));
    }
    
    @Test
    public void generoiAvainEiAuthoriaEiEditoriaYear() {
        setUp();
        Viite viite = new Viite();
        viite.setAttribuutti(AttrTyyppi.year.name(), "1234");
        
        testaaGenerointi(viite, "1234");
    }
    
    @Test
    public void generoiAvainEiAuthoriaEditorEiYearia() {
        setUp();
        Viite viite = new Viite();
        viite.setAttribuutti(AttrTyyppi.editor.name(), "markku");
        
        testaaGenerointi(viite, "markku");
    }
    
    @Test
    public void generoiAvainEiAuthoriaEditorYear() {
        setUp();
        Viite viite = new Viite();
        viite.setAttribuutti(AttrTyyppi.editor.name(), "markku");
        viite.setAttribuutti(AttrTyyppi.year.name(), "2007");
        
        testaaGenerointi(viite, "markku2007");
    }
    
    @Test
    public void generoiAvainAuthorEditorEiYearia() {
        setUp();
        
        Viite viite = new Viite();
        viite.setAttribuutti(AttrTyyppi.author.name(), "erkki");
        viite.setAttribuutti(AttrTyyppi.editor.name(), "jorma");
        
        testaaGenerointi(viite, "erkki");
    }
    
    @Test
    public void generoiAvainAuthorEiEditoriaEiYearia() {
        setUp();
        
        Viite viite = new Viite();
        viite.setAttribuutti(AttrTyyppi.author.name(), "erkki");
        
        testaaGenerointi(viite, "erkki");
    }
    
    @Test
    public void generoiAvainAuthorYear() {
        setUp();
        
        Viite viite = new Viite();
        viite.setAttribuutti(AttrTyyppi.author.name(), "erkki");
        viite.setAttribuutti(AttrTyyppi.year.name(), "5666");
        
        testaaGenerointi(viite, "erkki5666");
    }
    
    private Viite lisaaViite(String avain) {
        Viite viite = new Viite();
        viite.setBibtexAvain(avain);
        kasittelija.lisaaViite(viite);
        
        return viite;
    }
    
    private void testaaGenerointi(Viite viite, String avain) {
        assertEquals(avain, kasittelija.generoiAvain(viite));
        lisaaViite(avain);
        
        assertEquals(avain + "a", kasittelija.generoiAvain(viite));
        lisaaViite(avain + "a");
        
        assertEquals(avain + "b", kasittelija.generoiAvain(viite)); 
    }
    
    @Test
    public void haeViitePalauttaaHalutunViitteen() {
        lisaaViite("viite 1");
        
        Viite viite = lisaaViite("viite 2");
        assertEquals(viite, kasittelija.haeViite("viite 2"));
    }

    @Test
    public void viitteetListauksenaPalauttaaViitteetSelkokielisenaListana() {
        Viite viite = new Viite();
        viite.setBibtexAvain("viite1");
        viite.setTyyppi(ViiteTyyppi.book);
        viite.setAttribuutti("author", "Juuso");
        viite.setAttribuutti("title", "Testaamisen iloa");
        viite.setAttribuutti("publisher", "Mitä ihmettä kustantamo");
        kasittelija.lisaaViite(viite);

        String listaus = kasittelija.viitteetListauksena();

        assertTrue(listaus.contains("nimi: viite1"));
        assertTrue(listaus.contains("tyyppi: book"));
        assertTrue(listaus.contains("author: Juuso"));
        assertTrue(listaus.contains("publisher: Mitä ihmettä kustantamo"));
        assertTrue(listaus.contains("title: Testaamisen iloa"));
    }
    
    @Test
    public void poistaViitePoistaaViitteenViiteKasittelijasta() {
        Viite viite = new Viite();
        viite.setBibtexAvain("");
        
        kasittelija.lisaaViite(viite);
        kasittelija.poistaViite(viite);
        
        assertTrue(kasittelija.getViitteet().isEmpty());
    }
    
    @Test
    public void bibTexStringSisältääViitteenArvot() {
        Viite viite = new Viite();
        viite.setBibtexAvain("viite1");
        viite.setTyyppi(ViiteTyyppi.book);
        viite.setAttribuutti("author", "Juuso");
        viite.setAttribuutti("title", "Testaamisen iloa");
        viite.setAttribuutti("publisher", "Mita ihmetta kustantamo");
        kasittelija.lisaaViite(viite);
        
        String bibTexSyote = kasittelija.viitteetBibtexina();
        
        assertTrue(bibTexSyote.contains("viite1"));
        assertTrue(bibTexSyote.contains("Juuso"));
        assertTrue(bibTexSyote.contains("Testaamisen iloa"));
        assertTrue(bibTexSyote.contains("Mita ihmetta kustantamo"));
    }
    
    @Test 
    public void palauttaaPakollisetAttribuutit() {
        Attribuutti tyhja = when(mock(Attribuutti.class).getArvo()).thenReturn("").getMock();
        
        Attribuutti viitattava = tyhja;
        Viite viittaaja = mock(Viite.class);
        
        when(viittaaja.getBibtexAvain()).thenReturn("kirja");
        when(viittaaja.getAttribuutti("crossref")).thenReturn(viitattava);
        when(viittaaja.getTyyppi()).thenReturn(ViiteTyyppi.book);
        kasittelija.lisaaViite(viittaaja);
        
        Collection<AttrTyyppi> pakolliset = kasittelija.pakollisetAttribuutit(viittaaja);
        
        assertTrue(pakolliset.contains(AttrTyyppi.year));
        assertTrue(pakolliset.contains(AttrTyyppi.author));
        assertTrue(pakolliset.contains(AttrTyyppi.publisher));
        assertTrue(pakolliset.contains(AttrTyyppi.editor));
        assertTrue(pakolliset.contains(AttrTyyppi.title));
    }
    
    @Test
    public void attribuuttiEiOlePakollinenJosCrossrefMaaritteleeSen() {
        Viite kohde = mock(Viite.class);
        when(kohde.getBibtexAvain()).thenReturn("isi");
        kasittelija.lisaaViite(kohde);
        Attribuutti year = when(mock(Attribuutti.class).getArvo()).thenReturn("1337").getMock();
        when(kohde.getAttribuutti(anyString())).thenReturn(year);
        
        Viite viittaaja = mock(Viite.class);
        when(viittaaja.getTyyppi()).thenReturn(ViiteTyyppi.book);
        Attribuutti crossref = when(mock(Attribuutti.class).getArvo()).thenReturn("isi").getMock();
        when(viittaaja.getAttribuutti("crossref")).thenReturn(crossref);
        
        Collection<AttrTyyppi> pakolliset = kasittelija.pakollisetAttribuutit(viittaaja);
        
        assertTrue(pakolliset.isEmpty());
    }
}
