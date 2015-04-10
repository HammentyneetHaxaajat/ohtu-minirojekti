package hammentyneethaxaajat.viiteapuri.viite;

import java.util.Set;
import java.util.TreeSet;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class ViiteTyyppiTest {
    
    @Test
    public void bookViiteTyypinPakollisetSisaltaaSilleKuuluvatPakollisetAttribuutit() {
        Set<AttrTyyppi> pakolliset = new TreeSet<>();
        
        pakolliset.add(AttrTyyppi.author);
        pakolliset.add(AttrTyyppi.editor);
        pakolliset.add(AttrTyyppi.title);
        pakolliset.add(AttrTyyppi.publisher);
        pakolliset.add(AttrTyyppi.year);
        
        setitSamoja(ViiteTyyppi.book.getPakolliset(), pakolliset);
    }
    
    @Test
    public void bookViiteTyypinValinnaisetSisaltaaSilleKuuluvatValinnaisetAttribuutit() {
        Set<AttrTyyppi> valinnaiset = new TreeSet<>();
        
        valinnaiset.add(AttrTyyppi.volume);
        valinnaiset.add(AttrTyyppi.number);
        valinnaiset.add(AttrTyyppi.series);
        valinnaiset.add(AttrTyyppi.address);
        valinnaiset.add(AttrTyyppi.month);
        valinnaiset.add(AttrTyyppi.note);
        valinnaiset.add(AttrTyyppi.key);
        
        setitSamoja(ViiteTyyppi.book.getValinnaiset(), valinnaiset);
    }

    private void setitSamoja(Set<AttrTyyppi> bookinAttribuutit, Set<AttrTyyppi> verrattavat) {
        assertTrue(bookinAttribuutit.containsAll(verrattavat));
        assertTrue(bookinAttribuutit.size() == verrattavat.size());
    }
}
