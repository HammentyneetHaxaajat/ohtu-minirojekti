package hammentyneethaxaajat.viiteapuri.viite;

import java.util.Collection;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ViiteKasittelijaTest {

    ViiteKasittelija kasittelija;
    Viite viite;
    Attribuutti attr, attr2;
    
    @Before
    public void setUp() {
        kasittelija = new ViiteKasittelija();
        viite = mock(Viite.class);
        attr = mock(Attribuutti.class);
        attr2 = mock(Attribuutti.class);
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
        viite = lisaaViite("");
        assertFalse(viite.getBibtexAvain().isEmpty());
    }
    
    @Test
    public void generoiAvainEiAuthoriaEiEditoriaEiYearia() {
        assertEquals("a", kasittelija.generoiAvain(viite));
        lisaaViite("");
        assertEquals("b", kasittelija.generoiAvain(viite));
        lisaaViite("");
        assertEquals("c", kasittelija.generoiAvain(viite));
    }
    
    @Test
    public void generoiAvainEiAuthoriaEiEditoriaYear() {
        when(attr.getArvo()).thenReturn("1234");
        when(viite.getAttribuutti(AttrTyyppi.year.name())).thenReturn(attr);
        
        testaaGenerointi(viite, "1234");
    }
    
    @Test
    public void generoiAvainEiAuthoriaEditorEiYearia() {
        when(attr.getArvo()).thenReturn("markku");
        when(viite.getAttribuutti(AttrTyyppi.editor.name())).thenReturn(attr);

        testaaGenerointi(viite, "markku");
    }
    
    @Test
    public void generoiAvainEiAuthoriaEditorYear() {
        when(attr.getArvo()).thenReturn("markku");
        when(attr2.getArvo()).thenReturn("2007");
        when(viite.getAttribuutti(AttrTyyppi.editor.name())).thenReturn(attr);
        when(viite.getAttribuutti(AttrTyyppi.year.name())).thenReturn(attr2);
        
        testaaGenerointi(viite, "markku2007");
    }
    
    @Test
    public void generoiAvainAuthorEditorEiYearia() {
        when(attr.getArvo()).thenReturn("erkki");
        when(attr2.getArvo()).thenReturn("jorma");
        when(viite.getAttribuutti(AttrTyyppi.author.name())).thenReturn(attr);
        when(viite.getAttribuutti(AttrTyyppi.editor.name())).thenReturn(attr2);
        
        testaaGenerointi(viite, "erkki");
    }
    
    @Test
    public void generoiAvainAuthorEiEditoriaEiYearia() {
        when(attr.getArvo()).thenReturn("erkki");
        when(viite.getAttribuutti(AttrTyyppi.author.name())).thenReturn(attr);
        
        testaaGenerointi(viite, "erkki");
    }
    
    @Test
    public void generoiAvainAuthorYear() {
        when(attr.getArvo()).thenReturn("erkki");
        when(attr2.getArvo()).thenReturn("5666");
        when(viite.getAttribuutti(AttrTyyppi.author.name())).thenReturn(attr);
        when(viite.getAttribuutti(AttrTyyppi.year.name())).thenReturn(attr2);
        
        testaaGenerointi(viite, "erkki5666");
    }
    
    private Viite lisaaViite(String avain) {
        viite = new Viite(ViiteTyyppi.book);
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
        
        viite = lisaaViite("viite 2");
        assertEquals(viite, kasittelija.haeViite("viite 2"));
    }

    @Test
    public void viitteetListauksenaKutsuuListausMetodiaViitteille() {
        Viite viite2 = mock(Viite.class);
        when(viite.getBibtexAvain()).thenReturn("viite");
        when(viite2.getBibtexAvain()).thenReturn("abc");
       
        kasittelija.lisaaViite(viite);
        kasittelija.lisaaViite(viite2);
        
        kasittelija.viitteetListauksena();
        
        verify(viite, times(1)).listaus();
        verify(viite2, times(1)).listaus();
        
//        when(viite.getBibtexAvain()).thenReturn("viite1");
//        when(viite.getTyyppi()).thenReturn(ViiteTyyppi.book);
//        
//        attr = when(mock(Attribuutti.class).getArvo()).thenReturn("Juuso").getMock();
//        when(viite.getAttribuutti("author")).thenReturn(attr);
//        
//        attr2 = when(mock(Attribuutti.class).getArvo()).thenReturn("Testaamisen iloa").getMock();
//        when(viite.getAttribuutti("title")).thenReturn(attr2);
//        
//        Attribuutti attr3 = when(mock(Attribuutti.class).getArvo()).thenReturn("Mitä ihmettä kustantamo").getMock();
//        when(viite.getAttribuutti("publisher")).thenReturn(attr3);
//        
//        kasittelija.lisaaViite(viite);
//
//        String listaus = kasittelija.viitteetListauksena();
//        System.out.println(listaus);
//
//        assertTrue(listaus.contains("nimi: viite1"));
//        assertTrue(listaus.contains("tyyppi: book"));
//        assertTrue(listaus.contains("author: Juuso"));
//        assertTrue(listaus.contains("publisher: Mitä ihmettä kustantamo"));
//        assertTrue(listaus.contains("title: Testaamisen iloa"));
    }
    
    @Test
    public void poistaViitePoistaaViitteenViiteKasittelijasta() {
        when(viite.getBibtexAvain()).thenReturn("v");
        
        kasittelija.lisaaViite(viite);
        kasittelija.poistaViite(viite);
        
        assertTrue(kasittelija.getViitteet().isEmpty());
    }
    
    @Test
    public void bibTexStringSisältääViitteenArvot() {
        viite = lisaaViite("viite1");
        viite.setAttribuutti("author", "Juuso");
        viite.setAttribuutti("title", "Testaamisen iloa");
        viite.setAttribuutti("publisher", "Mita ihmetta kustantamo");
        
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
