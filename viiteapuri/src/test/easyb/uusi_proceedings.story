import hammentyneethaxaajat.viiteapuri.UI.*
import hammentyneethaxaajat.viiteapuri.validaattori.*
import hammentyneethaxaajat.viiteapuri.viite.*
import hammentyneethaxaajat.viiteapuri.IO.*
import hammentyneethaxaajat.viiteapuri.resurssit.*
import static org.mockito.Mockito.*

def perusviite = [nimi : "prohViite", tyyppi : "proceedings", title : "testaamisen iloa", year : "1995"]


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

//Testit alkaa täältä

description "Käyttäjä voi luoda uuden proceedings-viitteen"

scenario "käyttäjä kykenee lisäämään uuden proceedings-viitteen", {

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

scenario "käyttäjä tietää mitkä kentät ovat pakollisia", {

    given 'käyttäjä syöttää komennon uusi', {
        init()
        lisaaViite(perusviite)
    }

    when 'kaikki kentät lisätään oikein', {
        perustilanne()
        app.run()
    }

    then 'käyttäjä saa tiedon pakollisista kentistä', {
        verifyContains(Tulosteet.UUDEN_VIITTEEN_LUONTI)
        def pakolliset = ViiteTyyppi.proceedings.getPakolliset()

        for(AttrTyyppi atribuutti : pakolliset) {
            verify(io).lueRivi(contains(atribuutti.toString() + "*:"))
        }
    }
}

scenario "käyttäjän tulee täyttää pakolliset kentät", {

    given 'käyttäjä lisää uuden viitteen', {
        init()
        lisaaViite(perusviite)
    }
    
    when 'pakollinen kenttä jätetään tyhjäksi', {
        when(io.lueRivi(contains("nimi"))).thenReturn("", "prohViite")
        perustilanne()        
        app.run()
    }
    
    then 'Käyttäjälle ilmoitetaan, ettei kenttää voi jättää tyhjäksi', {
        verifyContains(Tulosteet.ARVO_EI_SAA_OLLA_TYHJA)
    }
}

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

