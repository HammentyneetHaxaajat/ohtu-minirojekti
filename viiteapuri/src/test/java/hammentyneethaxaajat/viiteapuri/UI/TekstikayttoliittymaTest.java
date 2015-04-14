package hammentyneethaxaajat.viiteapuri.UI;
    /* <---- Poista tuo ni testit tulee takas.


import hammentyneethaxaajat.viiteapuri.IO.IO;
import hammentyneethaxaajat.viiteapuri.validaattori.Validoija;
import hammentyneethaxaajat.viiteapuri.viite.ViiteKasittelija;
import static hammentyneethaxaajat.viiteapuri.resurssit.Tulosteet.*;
import java.util.Collection;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Rule;
import org.junit.rules.Timeout;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TekstikayttoliittymaTest {

    @Mock
    private ViiteKasittelija viiteKasittelija;
    @Mock
    private Validoija validaattori;
    @Mock
    private IO io;

    @InjectMocks
    private Tekstikayttoliittyma ui;

    @Rule
    public Timeout globalTimeout = Timeout.seconds(5); //TImeout etteivät testit jää ikuiseen looppiin kysymään jotakin syötettä

    public TekstikayttoliittymaTest() {
    }

    @Before
    public void setUp() {
        ui = new Tekstikayttoliittyma(viiteKasittelija, validaattori, io);
    }

     @Test
     public void ohjelmaSammuuLopetuskaskylla() {
     when(io.lueRivi(contains(SYOTA_KOMENTO))).thenReturn(LOPETA);
     ui.run();
     verify(io).tulosta(contains(VIESTI_HEIHEI));
     }

     @Test
     public void ohjelmaSelviaaJaKertooVirheellisestaKomennosta() {
     when(io.lueRivi(contains(SYOTA_KOMENTO))).thenReturn("wat").thenReturn(LOPETA);
     ui.run();
     verify(io).tulosta(contains(TUNTEMATON_KOMENTO));
     }

     @Test
     public void listaaKomentoEiTulostaListaustaJosEiViitteita() {
     when(io.lueRivi(contains(SYOTA_KOMENTO))).thenReturn(LISTAA).thenReturn(LOPETA);
     when(viiteKasittelija.viitteetListauksena()).thenReturn("Hieno Listaus");
     ui.run();
     verify(viiteKasittelija, never()).viitteetListauksena();
     verify(io, never()).tulosta(contains("Hieno Listaus"));
     }
    
     @Test
     public void listaaKomentoTulostaaListauksenJosOhjelmassaViitteita() {
     when(io.lueRivi(contains(SYOTA_KOMENTO))).thenReturn(LISTAA).thenReturn(LOPETA);
     when(viiteKasittelija.viitteetListauksena()).thenReturn("Hieno Listaus");
     Collection kek = when(mock(Collection.class).isEmpty()).thenReturn(Boolean.FALSE).getMock();
     when(viiteKasittelija.getViitteet()).thenReturn(kek);
     ui.run();
     verify(viiteKasittelija).viitteetListauksena();
     verify(io).tulosta(contains("Hieno Listaus"));
     }

     @Test
     public void uudenViitteenTekeminenToimiiJotenkinJaArvotValidoidaanTarvitaessa() {
     when(io.lueRivi(anyString())).thenReturn("Jotain");
     when(io.lueRivi(contains(TYYPPI))).thenReturn("book");
     when(io.lueRivi(contains(CROSSREF))).thenReturn("");
     when(io.lueRivi(contains(SYOTA_KOMENTO))).thenReturn(UUSI).thenReturn(LOPETA);
     ui.run();
     verify(validaattori, atLeastOnce()).validoi(anyString(), eq("Jotain"));
     verify(validaattori).validoi(eq(TYYPPI), eq("book"));
     verify(validaattori, never()).validoi(eq(CROSSREF), anyString());
     verify(viiteKasittelija).lisaaViite(any());
     }
    
     @Test
     public void uudenViitteenTekeminenIniseeVaarillaSyotteilla() {
     when(io.lueRivi(anyString())).thenReturn("Jotain");
     when(io.lueRivi(contains("author"))).thenReturn("", "Jotain");
     when(io.lueRivi(contains(TYYPPI))).thenReturn("book");
     when(io.lueRivi(contains(CROSSREF))).thenReturn("Vaara", "");
     when(io.lueRivi(contains(SYOTA_KOMENTO))).thenReturn(UUSI).thenReturn(LOPETA);
     doThrow(new IllegalArgumentException("HAHAHA")).when(validaattori).validoi(CROSSREF, "Vaara");
     ui.run();
     verify(validaattori, atLeastOnce()).validoi(eq(CROSSREF), eq("Vaara"));
     verify(io).tulosta(eq(ARVO_EI_SAA_OLLA_TYHJA));
     verify(io).tulosta(eq("HAHAHA"));
     }
     }
*/