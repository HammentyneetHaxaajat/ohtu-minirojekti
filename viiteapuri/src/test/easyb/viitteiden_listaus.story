import hammentyneethaxaajat.viiteapuri.UI.*
import hammentyneethaxaajat.viiteapuri.validaattori.*
import hammentyneethaxaajat.viiteapuri.viite.*
import hammentyneethaxaajat.viiteapuri.IO.*
import hammentyneethaxaajat.viiteapuri.resurssit.*
import static org.mockito.Mockito.*

def perusviitteet = [
bibtexavain : ["bViite1","bViite2","bViite3","bViite4"], 
tyyppi : ["book", "book", "book", "book"],
author : ["juri","matti","markus","mika"],
editor : ["juritus","matitus","markusedit","mikatus"],
publisher : ["juri pub","matti pub","markustamo","mika pub"],
title : ["testaamisen iloa1","testaamisen iloa2","testaamisen iloa3","testaamisen iloa4"],
year : ["1946","1947","1948","1949"]
]

def init() {
    viiteKasittelija = new ViiteKasittelija()
    validaattori = new Validaattori(viiteKasittelija)
    io = mock(KomentoriviIO.class)
    app = new Tekstikayttoliittyma(viiteKasittelija, validaattori, io)
}

def lisaaViite(Map mappi) {
    yleisKomento("")
    for (String kentta : mappi.keySet()) {
        when(io.lueRivi(contains(kentta))).
        thenReturn(mappi.get(kentta).get(0)).
        thenReturn(mappi.get(kentta).get(1)).
        thenReturn(mappi.get(kentta).get(2)).
        thenReturn(mappi.get(kentta).get(3))
    }
}

def yleisKomento(String komento) {
    when(io.lueRivi(anyString())).thenReturn(komento)
}

def verifyContains(String vaatimus) {
    verify(io, atLeastOnce()).tulosta(contains(vaatimus))
}

def verifyDoesNotContain(String vaatimus) {
    verify(io, never()).tulosta(contains(vaatimus))
}

def perustilanne() {
    when(io.lueRivi(contains(Tulosteet.SYOTA_KOMENTO))).thenReturn(
    Tulosteet.KOMENTO_UUSI,
    Tulosteet.KOMENTO_UUSI,
    Tulosteet.KOMENTO_UUSI,
    Tulosteet.KOMENTO_UUSI,
    Tulosteet.KOMENTO_LISTAA,
    Tulosteet.KOMENTO_LOPETA)    
}

//Testit alkaa täältä

description "Käyttäjä pystyy listaamaan ohjelmassa olevat viitteet"

scenario "käyttäjä näkee kaikki session viitteet", {

    given 'käyttäjä on syöttänyt useamman viitteen järjestelmään', {
        init()
        lisaaViite(perusviitteet)
    }

    when 'käyttäjä syöttää listaa komennon', {
        perustilanne()
        app.run()
    }

    then 'ohjelma tulostaa siihen syötetyt viitteet', {
        verify(io, atLeast(4)).tulosta(contains(Tulosteet.VIITE_LISATTY_ONNISTUNEESTI))
        
        verifyContains("bViite1")
        verifyContains("bViite2")
        verifyContains("bViite3")
        verifyContains("bViite4")
    }
}

scenario "viitteet listataan luettavassa muodossa", {

    given 'käyttäjä on syöttänyt useamman viitteen järjestelmään', {
        init()
        lisaaViite(perusviitteet)
    }

    when 'käyttäjä syöttää listaa komennon', {
        perustilanne()
        app.run()
    }

    then 'viitteessä olevat tiedot listataan selkokielellä', {
        verifyContains("bViite1")
        verifyContains("book")
        verifyContains("juri")
        verifyContains("juritus")
        verifyContains("juri pub")
        verifyContains("testaamisen iloa1")
        verifyContains("1946")
    }
}

scenario "viitteistä tulee kaikki tieto listaan", {

    given 'käyttäjä on syöttänyt useamman viitteen järjestelmään', {
        init()
        lisaaViite(perusviitteet)
    }

    when 'käyttäjä syöttää listaa komennon', {
        perustilanne()
        app.run()
    }

    //Katsotaan sisältääkö listaus kaikki tietokentät
    then 'kaikki syötetyt tiedot löytyvät tulosteesta', {
        def val = ViiteTyyppi.book.getPakolliset()
        def pak = ViiteTyyppi.book.getValinnaiset()

        //yhdistetään listat
        def attribuutit = val.plus(pak)

        verifyContains("nimi")
        verifyContains(Tulosteet.TYYPPI)
        verifyContains(Tulosteet.CROSSREF)
        for(AttrTyyppi atribuutti: attribuutit) {
            verifyContains(atribuutti.toString())
        }
    }
}

scenario "edellisten sessioiden viitteitä ei listata", {

    given 'käyttäjä on syöttänyt useamman viitteen järjestelmään', {
        init()
        lisaaViite(perusviitteet)
    }

    when 'käyttäjä sulkee session', {
        perustilanne()
        app.run()
    }

    given 'käyttäjä lisää vain yhden viitteen uudessa sessiossa' , {
        def perusviite = [bibtexavain : "bViite5", tyyppi : "book", author : "juri", editor : "juritus", publisher : "juri pub", title : "testaamisen iloa", year : "1995"]
        init()

        yleisKomento("")
        for (String kentta : perusviite.keySet()) {
            when(io.lueRivi(contains(kentta))).thenReturn(perusviite.get(kentta))
        }
    }

    when 'käyttäjä syöttää listaa komennon', {
        when(io.lueRivi(contains(Tulosteet.SYOTA_KOMENTO))).thenReturn(Tulosteet.KOMENTO_UUSI, Tulosteet.KOMENTO_LISTAA, Tulosteet.KOMENTO_LOPETA)
        app.run()
    }

    then 'vain edellisen session viite löytyy listasta', {
        verifyDoesNotContain("bViite1")
        verifyDoesNotContain("bViite2")
        verifyDoesNotContain("bViite3")
        verifyDoesNotContain("bViite4")
        verifyContains("bViite5")
    }
}