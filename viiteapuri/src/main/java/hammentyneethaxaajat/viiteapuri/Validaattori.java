package hammentyneethaxaajat.viiteapuri;

import java.util.Arrays;
import java.util.function.Predicate;

/**
 * Luokka, joka validoi syötteitä
 */
public class Validaattori {

    public boolean validoiViiteTyyppi(String tyyppi) {
        return Arrays.stream(ViiteTyyppi.values()).map(s -> s.name()).anyMatch(s -> s.equals(tyyppi));
    }

    public boolean validoi(String syöte, Predicate<String>... testit) {
        return Arrays.stream(testit).allMatch(s -> s.test(syöte));
    }
}
