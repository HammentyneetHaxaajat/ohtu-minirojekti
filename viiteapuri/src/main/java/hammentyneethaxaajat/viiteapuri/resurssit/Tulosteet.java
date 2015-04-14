package hammentyneethaxaajat.viiteapuri.resurssit;

/**
 * Tekstikäyttöliittymän hyödyntämät tulosteet vakioina.
 */

public abstract class Tulosteet {
    public static final String ARVO_EI_SAA_OLLA_TYHJA = "Kentän arvo ei saa olla tyhjä!\n";
    public static final String TUNTEMATON_VIITETYYPPI = "Tuntematon viitteen tyyppi. ";
    public static final String NIMI_VARATTU = "Nimi varattu. Valitse toinen nimi.\n";
    public static final String NIMI_EI_VASTAA_SEN_SYNTAKSIA = "Nimi ei vastaa sille määrätyä syntaksia.\n";
    public static final String RISTIVIITETTA_EI_OLE = " nimistä viitettä ei löydetty. Et voi viitata olemattomiin viitteisiin\n";
    public static final String TIEDOSTONLUONTI_EI_ONNISTUNUT = "Bibtex tiedoston kirjoitus epäonnistui";
    public static final String TIEDOSTONLUONTI_ONNISTUI = "Tiedosto luotiin polkuun: ";
    
    public static final String VIRHE_EIVIITTEITA = "Ohjelmassa ei ole yhtäkään viitettä.\n";
    public static final String VIITE = "viite";
    public static final String ATTIBUTTIKYSELY = "Muokattava attribuutti";
    public static final String KYSY_TIEDOSTO_NIMI = "Anna nimi tiestolle";
    public static final String KYSY_TIEDOSTO_POLKU = "Anna kohdekansio";
    public static final String TYYPPI = "tyyppi";
    public static final String NIMI = "nimi";
    public static final String CROSSREF = "crossref";
    
    public static final String UUSI = "uusi";
    public static final String MUOKKAA = "muokkaa";
    public static final String POISTA = "poista";
    public static final String LISTAA = "listaa";
    public static final String BIBTEX = "bibtex";
    public static final String LOPETA = "lopeta";
    
    public static final String SYOTA_KOMENTO = "Syötä komento";
    public static final String TUNTEMATON_KOMENTO = "Tuntematon komento. ";
    
    public static final String UUDEN_VIITTEEN_LUONTI = "Luodaan uusi viite.\nTähdellä(*) merkityt kentät ovat pakollisia.\n";
    public static final String TUETUT_KOMENNOT = "Tuetut komennot: " + UUSI + ", " + MUOKKAA + ", " + POISTA + ", " + LISTAA + ", " + BIBTEX + ", "  + LOPETA + ".\n";
    public static final String VIESTI_HEIHEI = "Ohjelma suljetaan.\n";
    public static final String KYSY_VARMISTUS = "Varmista viitteen poistaminen kirjoittamalla \"poista\"";
    public static final String VIITE_LISATTY_ONNISTUNEESTI = "Viite lisätty onnistuneesti!\n";
    
    
}
