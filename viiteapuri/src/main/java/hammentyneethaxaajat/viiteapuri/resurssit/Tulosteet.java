package hammentyneethaxaajat.viiteapuri.resurssit;

/**
 * Tekstikäyttöliittymän hyödyntämät tulosteet vakioina.
 */
public abstract class Tulosteet {

    public static final String ARVO_EI_SAA_OLLA_TYHJA = "Kentän arvo ei saa olla tyhjä!\n";
    public static final String TUNTEMATON_VIITETYYPPI = "Tuntematon viitteen tyyppi. ";
    public static final String NIMI_VARATTU = "Nimi varattu. Valitse toinen nimi.\n";
    public static final String NIMI_EI_VASTAA_SEN_SYNTAKSIA = "Nimi ei vastaa sille määrätyä syntaksia.\n";
    public static final String VIRHE_VIITETTA_EI_OLE = " nimistä viitettä ei löydetty.\n";
    public static final String TIEDOSTONLUONTI_EI_ONNISTUNUT = "Bibtex tiedoston kirjoitus epäonnistui\n";
    public static final String TIEDOSTONLUONTI_ONNISTUI = "Tiedosto luotiin ohjelman juureen nimellä: ";
    public static final String VIRHE_TIEDOSTONIMI = "Tiedoston nimi saa sisältää vain kirjaimia ja numeroita\n";
    public static final String VIRHE_TIEDOSTOPOLKU = "Tiedoston on oltava olemassa ja tiedostopolun lopussa on oltava kauttamerkki\n";

    public static final String VIRHE_EIVIITTEITA = "Ohjelmassa ei ole yhtäkään viitettä.\n";
    public static final String VIITE = "anna viite";
    public static final String ATTRIBUUTTI = "attribuutti";
    public static final String KYSY_TIEDOSTO_NIMI = "Anna nimi tiedostolle";
    public static final String KYSY_TIEDOSTO_POLKU = "Anna kohdekansio";
    public static final String TYYPPI = "tyyppi";
    public static final String BIBTEXAVAIN = "bibtexavain";
    public static final String CROSSREF = "crossref";

    public static final String KOMENTO_UUSI = "1. uusi\n";
    public static final String KOMENTO_MUOKKAA = "2. muokkaa\n";
    public static final String KOMENTO_POISTA = "3. poista\n";
    public static final String KOMENTO_LISTAA = "4. listaa\n";
    public static final String KOMENTO_BIBTEX = "5. bibtex\n";
    public static final String KOMENTO_TUO_BIBTEX = "6. tuo bibtex\n";
    public static final String KOMENTO_LOPETA = "7.lopeta\n";    

    public static final String SYOTA_KOMENTO = "Syötä komento";
    public static final String TUNTEMATON_KOMENTO = "Tuntematon komento. ";

    public static final String UUDEN_VIITTEEN_LUONTI = "Luodaan uusi viite.\nTähdellä(*) merkityt kentät ovat pakollisia.\n";
    public static final String TUETUT_KOMENNOT = "Tuetut komennot:\n" + KOMENTO_UUSI + KOMENTO_MUOKKAA + KOMENTO_POISTA + KOMENTO_LISTAA + KOMENTO_BIBTEX + KOMENTO_TUO_BIBTEX + KOMENTO_LOPETA;
    public static final String VIESTI_HEIHEI = "Ohjelma suljetaan.\n";
    public static final String KYSY_VARMISTUS = "Varmista toiminto kirjoittamalla ";
    public static final String VARMISTA_POISTO = "poista";
    public static final String POISTETTAVA_VIITE = "poistettava viite";
    public static final String VIITE_LISATTY_ONNISTUNEESTI = "Viite lisätty onnistuneesti!\n";
    public static final String TIEDOSTONLUKU_EI_ONNISTUNUT = "Tiedoston luku ei onnistunut\n";

}
