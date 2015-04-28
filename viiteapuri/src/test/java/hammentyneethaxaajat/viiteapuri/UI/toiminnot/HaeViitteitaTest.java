package hammentyneethaxaajat.viiteapuri.UI.toiminnot;

import hammentyneethaxaajat.viiteapuri.IO.IO;
import hammentyneethaxaajat.viiteapuri.IO.KomentoriviIO;
import static hammentyneethaxaajat.viiteapuri.resurssit.Tulosteet.VIRHE_EIVIITTEITA;
import hammentyneethaxaajat.viiteapuri.validaattori.Validaattori;
import hammentyneethaxaajat.viiteapuri.validaattori.Validoija;
import hammentyneethaxaajat.viiteapuri.viite.Viite;
import hammentyneethaxaajat.viiteapuri.viite.ViiteKasittelija;
import java.util.ArrayList;
import java.util.Collection;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class HaeViitteitaTest {
    private IO io;
    private HaeViitteita hakija;
    
    private void alustaHakija(IO io, ViiteKasittelija kasittelija, Validoija validoija) {
        hakija = new HaeViitteita(io, kasittelija, validoija);
    }
    
    @Before
    public void setUp() {
        io = mock(KomentoriviIO.class);
        alustaHakija(io, mock(ViiteKasittelija.class), mock(Validaattori.class));
    }
    
    @Test
    public void suoritaIlmoittaaJosOhjelmassaEiViitteita() {
        hakija.suorita();
        verify(io, times(1)).tulosta(eq(VIRHE_EIVIITTEITA));
    }
    
    @Test
    public void tulostaHakuTulosPalauttaaTiedonJosHaullaEiLoytynytYhtaanViitetta() {
        Collection<Viite> tyhja = new ArrayList<>();
        
        hakija.tulostaHakuTulos(tyhja);
        verify(io, times(1)).tulosta(eq("Haulla ei löydetty yhtään viitettä.\n"));
    }
    
    @Test
    public void tulostaHakuTulosPalauttaaHaullaLoytyneetViitteet() {
        Collection<Viite> epatyhja = new ArrayList<>();
        
        Viite viite1 = mock(Viite.class);
        when(viite1.listaus(false)).thenReturn("viite1");
        
        Viite viite2 = mock(Viite.class);
        when(viite2.listaus(false)).thenReturn("viite2");
        
        epatyhja.add(viite1);
        epatyhja.add(viite2);
        
        hakija.tulostaHakuTulos(epatyhja);
        verify(io, times(0)).tulosta(eq("Haulla ei löydetty yhtään viitettä.\n"));
        verify(io, times(1)).tulosta(anyString());
    }
}
