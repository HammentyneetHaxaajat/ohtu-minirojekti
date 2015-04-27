package hammentyneethaxaajat.viiteapuri.UI.toiminnot;

import hammentyneethaxaajat.viiteapuri.IO.BibtexIO;
import hammentyneethaxaajat.viiteapuri.IO.IO;
import hammentyneethaxaajat.viiteapuri.validaattori.Validoija;
import hammentyneethaxaajat.viiteapuri.viite.ViiteKasittelija;
import java.util.HashMap;
import java.util.Map;

public class Toimintotehdas {
    private Map<String, Toiminto> toiminnot;
    private Toiminto tuntematon;
    
    public Toimintotehdas(IO io, ViiteKasittelija kasittelija, Validoija validoija, BibtexIO tiedostokasittelija) {
        toiminnot = new HashMap<>();
        //TODO put toiminnot
        toiminnot.put("1", new UusiViite(io, kasittelija, validoija));
        toiminnot.put("2", new MuokkaaViitetta(io, kasittelija, validoija));
        toiminnot.put("3", new PoistaViite(io, kasittelija, validoija));
        toiminnot.put("4", new ListaaViitteet(io, kasittelija, validoija));
        toiminnot.put("5", new luoBibtex(io, kasittelija, validoija, tiedostokasittelija));
        toiminnot.put("6", new Lopeta(io, kasittelija, validoija));
        tuntematon = new TuntematonToiminto(io, kasittelija, validoija);
    }
    
    public void suorita(String numero) {
        if (toiminnot.containsKey(numero)) {
            toiminnot.get(numero).suorita();
        } else {
            tuntematon.suorita();
        }
    }
}
