import hammentyneethaxaajat.viiteapuri.UI.*
import hammentyneethaxaajat.viiteapuri.validaattori.*
import hammentyneethaxaajat.viiteapuri.viite.*
import hammentyneethaxaajat.viiteapuri.IO.*
import hammentyneethaxaajat.viiteapuri.resurssit.*
import static org.mockito.Mockito.*

def perusviite = [tyyppi : "book", author : "juri", editor : "juritus", publisher : "juri pub", title : "testaamisen iloa", year : "1995"]

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
    verify(io, atLeastOnce()).tulosta(contains(vaatimus))
}

def perustilanne() {
    when(io.lueRivi(contains(Tulosteet.SYOTA_KOMENTO))).thenReturn(Tulosteet.KOMENTO_UUSI, Tulosteet.KOMENTO_HAE_VIITTEITA, Tulosteet.KOMENTO_LOPETA)
    when(io.lueRivi(contains(Tulosteet.KYSY_HAKUSANA))).thenReturn("author")
    when(io.lueRivi(contains("Haettava arvo"))).thenReturn("juri")
}

description 'Käyttäjä voi rajata viitelistan hakutuloksia'

scenario 'käyttäjä voi hakea viitteitä hakusanojen perusteella', {

    given 'Käyttäjä lisää viitteen ohjelmaan', {
        init()
        lisaaViite(perusviite)
    }

    when 'Käyttäjä hakee jotain tiettya atribuuttia', {
        perustilanne()
        app.run()
    }

    then 'Käyttäjälle tulostetaan viitteet jotka täyttävät hakutuloksen kriteerit', {
        verifyContains("bibtexavain: juri1995")
    }
}

scenario 'käyttäjältä kysytään haettavaa kenttää', {

    given 'description', {
        init()
        lisaaViite(perusviite)
    }

    when 'description', {
        perustilanne()
        app.run()
    }

    then 'description', {
        verify(io).lueRivi(contains(Tulosteet.KYSY_HAKUSANA))
    }
}


scenario 'käyttäjälle annetaan ilmoitus jos yhtäkään hakusanaa täyttävää viitettä ei ole', {
    given 'description', {
        init()
        lisaaViite(perusviite)
    }

    when 'description', {
        perustilanne()
        when(io.lueRivi(contains("Haettava arvo"))).thenReturn("En halua löytää mitään")
        app.run()
    }

    then 'description', {
        verifyContains("Haulla ei löydetty yhtään viitettä")
    }
}

/*Tulee jo testatuksi perustilanteessa

scenario 'käyttäjä asettaa hakusanan kentälle'
scenario 'käyttäjälle palauatetaan lista hakuehdot toteuttavista viitteistä'
*/