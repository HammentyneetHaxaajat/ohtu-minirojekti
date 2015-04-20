import hammentyneethaxaajat.viiteapuri.UI.*
import hammentyneethaxaajat.viiteapuri.validaattori.*
import hammentyneethaxaajat.viiteapuri.viite.*
import hammentyneethaxaajat.viiteapuri.IO.*
import hammentyneethaxaajat.viiteapuri.resurssit.*
import static org.mockito.Mockito.*

String[] xcxzcxzccxzczcxzc = [
"uusi", "bViite1", "book", "", "juri", "juritus", "juri pub", "testaamisen iloa1", "1946", "", "", "", "", "", "", "",
"uusi", "bViite2", "book", "", "matti", "matitus", "matti pub", "testaamisen iloa2", "1947", "", "", "", "", "", "", "",
"uusi", "bViite3", "book", "", "markus", "markusedit", "markustamo", "testaamisen iloa3", "1948", "", "", "", "", "", "", "",
"uusi", "bViite4", "book", "", "mika", "mikatus", "mika pub", "testaamisen iloa4", "1949", "", "", "", "", "", "", "", "poista", "bViite3", "poista", "listaa", "lopeta"]

def perusviitteet = [
nimi : ["bViite1","bViite2","bViite3","bViite4"], 
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
    when(io.lueRivi(contains(Tulosteet.SYOTA_KOMENTO))).thenReturn("uusi", "uusi", "uusi", "uusi", "poista", "lopeta")
    when(io.lueRivi(contains(Tulosteet.VIITE))).thenReturn("bViite1")
    when(io.lueRivi(contains(Tulosteet.VARMISTA_POISTO))).thenReturn("poista")
}

def init(String... komennot) {
    viiteKasittelija = new ViiteKasittelija()
    validaattori = new Validaattori(viiteKasittelija)
    io = new StubIO(komennot)
    app = new Tekstikayttoliittyma(viiteKasittelija, validaattori, io)
}

description "Käyttäjä voi poistaa viitteen"

scenario "käyttäjän on nähtävä mahdolliset poistettavat viitteet", {

    given 'käyttäjä lisää viitteet ohjelmaan', {
        init()
        lisaaViite(perusviitteet)
    }
    
    when 'käyttäjä syötää poista komennon', {
        perustilanne()
        app.run()
    }
    
    then 'viite on poistettu ohjelmasta', {
        verifyContains("Poistettavissa olevat viitteet: bViite1, bViite2, bViite3, bViite4")
    }
}

scenario "käyttäjä voi poistaa viitteen", {

    given 'käyttäjä lisää viitteet ohjelmaan', {
        init()
        lisaaViite(perusviitteet)
    }
    
    when 'käyttäjä poistaa viitteen ohjelmasta', {
        when(io.lueRivi(contains(Tulosteet.SYOTA_KOMENTO))).thenReturn("uusi", "uusi", "uusi", "uusi", "poista", "poista", "lopeta")
        when(io.lueRivi(contains(Tulosteet.VIITE))).thenReturn("bViite1", "bViite1", "bViite2")
        when(io.lueRivi(contains(Tulosteet.VARMISTA_POISTO))).thenReturn("poista")
        app.run()
    }

    //purkkaa
    then 'viite on poistettu ohjelmasta', {
        verifyContains("bViite1 nimistä viitettä ei löydetty.")
    }
}

scenario "ohjelma tulostaa poistettavan viitteen tiedot", {
    given 'käyttäjä listaa viiteet ohjelmaan', {
        init()
        lisaaViite(perusviitteet)        
    }
    
    when 'käyttäjä poistaa viiteen ohjelmasta', {
        perustilanne()
        app.run()
    }
    
    then 'viitteen tiedot listataan käyttäjälle', {
        verifyContains("Viitteen tiedot:")
    }
}

scenario "ohjelman on varmistettava käyttäjältä viitteen poistaminen", {
    given 'käyttäjä listaa viitteet ohjelmaan', {
        init()
        lisaaViite(perusviitteet)
    }
    
    when 'käyttäjä syöttää poista komennon ja valitsee poistettavan viitteen', {
        perustilanne()
        app.run()
    }
    
    then 'käyttäjää pyydetään varmistamaan poistaminen', {
        verify(io).lueRivi(contains(Tulosteet.KYSY_VARMISTUS))
    }
}