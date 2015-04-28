package hammentyneethaxaajat.viiteapuri.resurssit;

/**
 * Tekstikäyttöliittymän hyödyntämät tulosteet vakioina.
 */
public abstract class Tulosteet {

    public static final String ARVO_EI_SAA_OLLA_TYHJA = "Kentän arvo ei saa olla tyhjä!\n";
    public static final String TUNTEMATON_VIITETYYPPI = "Tuntematon viitteen tyyppi. ";
    public static final String NIMI_VARATTU = "Nimi varattu. Valitse toinen nimi.\n";
    public static final String NIMI_EI_VASTAA_SEN_SYNTAKSIA = "Nimi ei vastaa sille määrätyä syntaksia.\n";
    public static final String KENTTA_PAIVITETTY = "Kentän arvo päivitetty onnistuneesti.\n";
    public static final String VIRHE_VIITETTA_EI_OLE = " nimistä viitettä ei löydetty.\n";
    public static final String TIEDOSTONLUONTI_EI_ONNISTUNUT = "Bibtex tiedoston kirjoitus epäonnistui\n";
    public static final String TIEDOSTONLUONTI_ONNISTUI = "Tiedosto luotiin ohjelman juureen nimellä: ";
    public static final String TIEDOSTONTUONTI_ONNISTUI = "Tiedosto tuotiin onnistuneesti.\n";
    public static final String VIRHE_TIEDOSTONIMI = "Tiedoston nimi saa sisältää vain kirjaimia ja numeroita.\n";
    public static final String VIRHE_TIEDOSTOPOLKU = "Tiedoston on oltava olemassa ja tiedostopolun lopussa on oltava kauttamerkki.\n";
    public static final String VIRHE_OLEMATONTIEDOSTO = "Annettu polku ei ole kelvollinen.\n";
    public static final String KYSY_UUSIARVO = "Uusi arvo attribuutille ";
    
    public static final String VIRHE_TIEDOSTOFORMAATTI = "Luettava tiedosto ei vastaa tuettua .bib formaattia\n";
    public static final String VIESTI_TUODUT_VIITTEET = "Tiedostosta Luetut viitteet:\n";
    public static final String VIRHE_VIITE_HYLATTIIN = "Epäkelpoja viitteitä hylättiin: ";
    public static final String VIRHE_EI_OLE_ATTRIBUUTTI = "Annettu syöte ei ole kelvollinen kentän nimi.\n";
    public static final String VIRHE_EIVIITTEITA = "Ohjelmassa ei ole yhtäkään viitettä.\n";
    public static final String VIITE = "anna viite";
    public static final String ATTRIBUUTTI = "Anna attribuutti";
    public static final String KYSY_TIEDOSTO_NIMI = "Anna nimi tiedostolle";
    public static final String KYSY_TIEDOSTO_POLKU = "Anna tiedoston polku";
    public static final String TYYPPI = "tyyppi";
    public static final String BIBTEXAVAIN = "bibtexavain";
    public static final String CROSSREF = "crossref";
    public static final String KYSY_HAKUSANA = "Anna attribuutti jolla haetaan";

    public static final String KOMENTO_UUSI = "uusi";
    public static final String KOMENTO_MUOKKAA = "muokkaa";
    public static final String KOMENTO_POISTA = "poista";
    public static final String KOMENTO_LISTAA = "listaa";
    public static final String KOMENTO_LUO_BIBTEX = "luo bibtex";
    public static final String KOMENTO_TUO_BIBTEX = "tuo bibtex";
    public static final String KOMENTO_HAE_VIITTEITA = "Hae viitteitä";
    public static final String KOMENTO_LOPETA = "lopeta";

    public static final String KOMENTO_UUSI_NUMERO = "1";
    public static final String KOMENTO_MUOKKAA_NUMERO = "2";
    public static final String KOMENTO_POISTA_NUMERO = "3";
    public static final String KOMENTO_LISTAA_NUMERO = "4";
    public static final String KOMENTO_HAE_VIITTEITA_NUMERO = "5";
    public static final String KOMENTO_LUO_BIBTEX_NUMERO = "6";
    public static final String KOMENTO_TUO_BIBTEX_NUMERO = "7";
    public static final String KOMENTO_LOPETA_NUMERO = "8";

    public static final String OHJE_UUSI = KOMENTO_UUSI_NUMERO + ". " + KOMENTO_UUSI;
    public static final String OHJE_MUOKKAA = KOMENTO_MUOKKAA_NUMERO + ". " + KOMENTO_MUOKKAA;
    public static final String OHJE_POISTA = KOMENTO_POISTA_NUMERO + ". " + KOMENTO_POISTA;
    public static final String OHJE_LISTAA = KOMENTO_LISTAA_NUMERO + ". " + KOMENTO_LISTAA;
    public static final String OHJE_LUO_BIBTEX = KOMENTO_LUO_BIBTEX_NUMERO + ". " + KOMENTO_LUO_BIBTEX;
    public static final String OHJE_TUO_BIBTEX = KOMENTO_TUO_BIBTEX_NUMERO + ". " + KOMENTO_TUO_BIBTEX;
    public static final String OHJE_HAE_VIITTEITA = KOMENTO_HAE_VIITTEITA_NUMERO + ". " + KOMENTO_HAE_VIITTEITA;
    public static final String OHJE_LOPETA = KOMENTO_LOPETA_NUMERO + ". " + KOMENTO_LOPETA;

    public static final String SYOTA_KOMENTO = "Syötä komento";
    public static final String TUNTEMATON_KOMENTO = "Tuntematon komento. ";

    public static final String UUDEN_VIITTEEN_LUONTI = "Luodaan uusi viite.\nTähdellä(*) merkityt kentät ovat pakollisia.\n";
    public static final String TUETUT_KOMENNOT = "Tuetut komennot:\n" + OHJE_UUSI + "\n" + OHJE_MUOKKAA + "\n" + OHJE_POISTA + "\n" + OHJE_LISTAA + "\n" + OHJE_HAE_VIITTEITA + "\n" + OHJE_LUO_BIBTEX + "\n" + OHJE_TUO_BIBTEX + "\n" + OHJE_LOPETA + "\n";
    public static final String OHJEET = "\nVoit kirjoittaa komennon numeron tai nimen.\n";
    public static final String VIESTI_HEIHEI = "Ohjelma suljetaan.\n";
    public static final String KYSY_VARMISTUS = "Varmista toiminto kirjoittamalla ";
    public static final String VARMISTA_POISTO = "poista";
    public static final String POISTETTAVA_VIITE = "poistettava viite";
    public static final String POISTETTAVISSA_OLEVAT_VIITTEET = "Poistettavissa olevat viitteet: ";
    public static final String VIITE_LISATTY_ONNISTUNEESTI = "Viite lisätty onnistuneesti!\n";
    public static final String VIITTEEN_TIEDOT = "\nViitteen tiedot:\n";
    public static final String MUOKATTAVISSA_OLEVAT_VIITTEET = "Muokattavissa olevat viitteet: ";

    public static final String VIIVA = "--------------------";
}
