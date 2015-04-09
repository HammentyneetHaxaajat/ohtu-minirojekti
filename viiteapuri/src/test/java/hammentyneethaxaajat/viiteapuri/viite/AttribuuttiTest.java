package hammentyneethaxaajat.viiteapuri.viite;

import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class AttribuuttiTest {
    
    @Test
    public void toStringPalauttaaHalutunMuotoisenEsityksen() {
        Attribuutti buutti = new Attribuutti(AttrTyyppi.editor, "janne");
        assertTrue(buutti.toString().contains("editor = {janne},"));
    }
}
