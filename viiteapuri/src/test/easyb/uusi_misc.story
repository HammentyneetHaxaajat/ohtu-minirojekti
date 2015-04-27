import hammentyneethaxaajat.viiteapuri.UI.*
import hammentyneethaxaajat.viiteapuri.validaattori.*
import hammentyneethaxaajat.viiteapuri.viite.*
import hammentyneethaxaajat.viiteapuri.IO.*
import hammentyneethaxaajat.viiteapuri.resurssit.*
import static org.mockito.Mockito.*

def perusviite = [tyyppi : "misc"]


//Setup metodeja
def init() {
    viiteKasittelija = new ViiteKasittelija()
    validaattori = new Validaattori(viiteKasittelija)
    io = mock(KomentoriviIO.class)
    app = new Tekstikayttoliittyma(viiteKasittelija, validaattori, io)
}

def lisaaViite(Map mappi) {
    yleisKomento("")
    for (String kentta : mappi.keySet()) {
        when(io.lueRivi(contains(kentta))).thenReturn(mappi.get(kentta))
    }
}

def yleisKomento(String komento) {
    when(io.lueRivi(anyString())).thenReturn(komento)
}

def verifyContains(String vaatimus) {
    verify(io).tulosta(contains(vaatimus))
}

def perustilanne() {
    when(io.lueRivi(contains(Tulosteet.SYOTA_KOMENTO))).thenReturn(Tulosteet.KOMENTO_UUSI, Tulosteet.KOMENTO_LOPETA)
}

//Testit alkaa täältä

description "Käyttäjä voi luoda uuden misc-viitteen"

scenario "käyttäjä kykenee lisäämään uuden misc-viitteen", {

    given 'käyttäjä antaa komennon uusi', {
        init()
    }

    when 'kaikki kentät lisätään oikein', {
        lisaaViite(perusviite)
        perustilanne()
        app.run()
    }

    then 'uusi viite lisätään järjestelmään', {
        verifyContains(Tulosteet.VIITE_LISATTY_ONNISTUNEESTI)
    }

}

//Normaalisti tässä kohtaa pakollisten kenttien testaus. Miscissä ei ole pakollisia kenttiä

scenario "käyttäjän ei ole pakko täyttää valinnaisia kenttiä", {

    given 'käyttäjä syöttää komennon uusi', {
        init()
        lisaaViite(perusviite)
    }

    when 'kaikki valinnaiset kentät jätetään tyhjiksi', {
        perustilanne()
        app.run()
    }

    then 'uusi viite lisätään järjestelmään', {
        verifyContains(Tulosteet.VIITE_LISATTY_ONNISTUNEESTI)
    }
}

