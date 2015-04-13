package hammentyneethaxaajat.viiteapuri.viite;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
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

    @Test
    public void haeViitePalauttaaHalutunViitteen() {
        lisaaViite();
        Viite viite = new Viite();
        viite.setNimi("viite 2");
        kasittelija.lisaaViite(viite);

        assertEquals(viite, kasittelija.haeViite("viite 2"));
    }

    private void lisaaViite() {
        Viite viite = new Viite();
        viite.setNimi("viite 1");
        kasittelija.lisaaViite(viite);
    }

    @Test
    public void viitteetListauksenaPalauttaaViitteetSelkokielisenaListana() {
        Viite viite = new Viite();
        viite.setNimi("viite1");
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
}
