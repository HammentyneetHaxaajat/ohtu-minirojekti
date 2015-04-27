import hammentyneethaxaajat.viiteapuri.UI.*
import hammentyneethaxaajat.viiteapuri.validaattori.*
import hammentyneethaxaajat.viiteapuri.viite.*
import hammentyneethaxaajat.viiteapuri.IO.*
import hammentyneethaxaajat.viiteapuri.resurssit.*
import static org.mockito.Mockito.*

def perusviite = [tyyppi : "book", author : "juri, Puri", editor : "juritus", publisher : "juri pub", title : "testaamisen iloa", year : "1995"]
def omaNimi = [bibtexavain : "ParasNimiOnOmaNimi", tyyppi : "book", author : "juri", editor : "juritus", publisher : "juri pub", title : "testaamisen iloa", year : "1995"]


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
    when(io.lueRivi(contains(Tulosteet.SYOTA_KOMENTO))).thenReturn("uusi", "lopeta")
}

description 'nimen autogenerointi'

scenario 'Käyttäjän ei itse tarvitse syöttää viitteen nimeä', {

    given 'Käyttäjä lisää uuden viitteen järjestelmään', {
        init()
        lisaaViite(perusviite)
    }
    
    when 'Käyttäjä syöttää tiedot ilman bibtexavainta', {
        perustilanne()
        app.run()
    }
    
    then 'Viitteen lisätään onnistuneesti', {
        verifyContains(Tulosteet.VIITE_LISATTY_ONNISTUNEESTI)
    }
}

scenario 'Käyttäjä voi halutessaan lisätä itse nimen, muutoin autogeneroidaan', {

    given 'Käyttäjä lisää uuden viitteen järjestelmään', {
        init()
        lisaaViite(omaNimi)
    }
    
    when 'Käyttäjä syöttää tiedoille oman bibtexavaimen', {
        perustilanne()
        app.run()
    }
    
    then 'Viitteen lisätään onnistuneesti', {
        verifyContains(Tulosteet.VIITE_LISATTY_ONNISTUNEESTI)
        verifyContains("ParasNimiOnOmaNimi")
    }
}

scenario 'Nimestä löytyy ensimmäisen authorin (suku)nimi', {

    given 'Käyttäjä lisää uuden viitteen järjestelmään', {
        init()
        lisaaViite(perusviite)
    }
    
    when 'Käyttäjä syöttää tiedot ilman bibtexavainta', {
        perustilanne()
        app.run()
    }
    
    then 'Viitteen nimi(bibtexavain) sisältää authorin sukunimen', {
        verifyContains("nimi: juri, Puri")
    }

}
scenario 'Nimestä löytyy vuosi', {

    given 'Käyttäjä lisää uuden viitteen järjestelmään', {
        init()
        lisaaViite(perusviite)
    }
    
    when 'Käyttäjä syöttää tiedot ilman bibtexavainta', {
        perustilanne()
        app.run()
    }
    
    then 'Viitteen nimi(bibtexavain) sisältää authorin sukunimen', {
        verifyContains("nimi: juri, Puri1995")
    }
}

scenario 'Nimi on uniikki(ilman bibtexavainta)', {

    given 'Käyttäjä lisää uuden viitteen järjestelmään', {
        init()
        lisaaViite(perusviite)
    }
    
    when 'Käyttäjä syöttää tiedot ilman bibtexavainta', {
        when(io.lueRivi(contains(Tulosteet.SYOTA_KOMENTO))).thenReturn("uusi", "uusi", "lopeta")
        app.run()
    }
    
    then 'Ohjelmaan ei luo kahta viitettä samalla bibtexavaimella', {

        verifyContains("nimi: juri, Puri1995a")
    }
}

scenario 'Nimi on uniikki(omalla bibtexavaimella)', {

    given 'Käyttäjä lisää uuden viitteen järjestelmään', {
        init()
        lisaaViite(omaNimi)
        when(io.lueRivi(contains("bibtexavain"))).thenReturn("avain", "avain", "avain1")
    }
    
    when 'Käyttäjä syöttää tiedot ilman bibtexavainta', {
        when(io.lueRivi(contains(Tulosteet.SYOTA_KOMENTO))).thenReturn("uusi", "uusi", "lopeta")
        app.run()
    }
    
    then 'Ohjelmaan ei luo kahta viitettä samalla bibtexavaimella', {
        verifyContains(Tulosteet.NIMI_VARATTU)
    }
}