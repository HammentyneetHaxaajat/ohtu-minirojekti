import hammentyneethaxaajat.viiteapuri.UI.*
import hammentyneethaxaajat.viiteapuri.validaattori.*
import hammentyneethaxaajat.viiteapuri.viite.*
import hammentyneethaxaajat.viiteapuri.IO.*
import hammentyneethaxaajat.viiteapuri.resurssit.*
import static org.mockito.Mockito.*

def perusviite = [nimi : "bViite", tyyppi : "book", author : "juri", editor : "juritus", publisher : "juri pub", title : "testaamisen iloa", year : "1995"]

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

def perustilanne() {
    when(io.lueRivi(contains(Tulosteet.SYOTA_KOMENTO))).thenReturn("uusi", "muokkaa", "listaa", "lopeta")
    when(io.lueRivi(contains(Tulosteet.VIITE))).thenReturn("bViite")
    when(io.lueRivi(contains(Tulosteet.ATTRIBUUTTI))).thenReturn("editor")
    when(io.lueRivi(contains("editor"))).thenReturn("Jeesus")
}

def verifyContains(String vaatimus) {
    verify(io).tulosta(contains(vaatimus))
}


def init(String... komennot) {
    viiteKasittelija = new ViiteKasittelija()
    validaattori = new Validaattori(viiteKasittelija)
    io = new StubIO(komennot)
    app = new Tekstikayttoliittyma(viiteKasittelija, validaattori, io)
}

description "Käyttäjä voi muokata viitteitä"

scenario "käyttäjän on nähtävä mahdolliset muokattavat viitteet", {

    given 'käyttäjä lisää viitteet ohjelmaan', {
        init()
        lisaaViite(perusviite)
    }
    
    when 'käyttäjä syötää muokkaa komennon', {
        perustilanne()
        app.run()
    }

    then 'viite on muokattu ohjelmasta', {
        verifyContains("Muokattavissa olevat viitteet: bViite")
    }
}

//Tämä tilanne on suoraan perustilanteessa
scenario "käyttäjä voi valita muokattavan kentän ilman että käydään jokainen kenttä läpi", {

    given 'käyttäjä lisää viitteet ohjelmaan', {
        init()
        lisaaViite(perusviite)
    }

    when 'käyttäjä syöttää muokkaa komennon', {
        perustilanne()
        app.run()
    }
    then 'viite on muokattu onnistuneesti', {
        verifyContains("Kentän arvo päivitetty onnistuneesti.")
    }
}

scenario "käyttäjä näkee mahdolliset kentät", {

    given 'käyttäjä lisää viitteet ohjelmaan', {
        init()
        lisaaViite(perusviite)
    }
    when 'käyttäjä syöttää muokkaa komennon', {
        perustilanne()
        app.run()
    }
    then 'viite on muokattu onnistuneesti', {
        verify(io, atLeastOnce()).lueRivi(contains(Tulosteet.ATTRIBUUTTI))
    }
}

scenario "viitteen muokkaus näkyy tulosteessa", {

    given 'käyttäjä lisää viitteet ohjelmaan', {
        init()
        lisaaViite(perusviite)
    }
    when 'käyttäjä syöttää muokkaa komennon', {
        perustilanne()
        app.run()
    }
    then 'viite on muokattu onnistuneesti', {
        verify(io, atLeastOnce()).tulosta(contains("editor: Jeesus"))
    }
}