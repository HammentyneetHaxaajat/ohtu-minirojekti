
package hammentyneethaxaajat.viiteapuri.validaattori;

import hammentyneethaxaajat.viiteapuri.viite.Viite;

/**
 * Rajapinta validointitoiminnoille
 */
public interface Validoija {

    void validoi(String nimi, String arvo);
    void validoi(Viite viite, String attr, String arvo);
    void validoiEttaEpaTyhja(String syote);
    
}
