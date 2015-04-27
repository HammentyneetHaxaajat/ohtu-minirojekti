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
    verify(io).tulosta(contains(vaatimus))
}

def perustilanne() {
    when(io.lueRivi(contains(Tulosteet.SYOTA_KOMENTO))).thenReturn("uusi", "lopeta")
}

//Testit alkaa täältä

description "Käyttäjä voi luoda uuden book-viitteen"

scenario "käyttäjä kykenee lisäämään uuden book-viitteen", {

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

scenario "jos annetaan vahingossa virheelliset syötteet kenttiin, antaa ohjelma palautetta", {

    given 'käyttäjä antaa syötteitä', {
        init()
        lisaaViite(perusviite)
    }
    
    when 'käyttäjä antaa väärän komennon ja viitetiedon', {
        //Korvaa lisaaViite() metodissa asetun perustilanteen
        when(io.lueRivi(contains("tyyppi"))).thenReturn("böök", "book")
        when(io.lueRivi(contains(Tulosteet.SYOTA_KOMENTO))).thenReturn("aasi", "uusi", "lopeta")
        app.run()
    }
    
    then 'käyttäjälle välittyy tieto väärästä komennosta', {
        verifyContains(Tulosteet.TUNTEMATON_KOMENTO)
        verify(io, atLeastOnce()).tulosta(contains(Tulosteet.TUETUT_KOMENNOT))
    }
    and
    then 'käyttäjälle välittyy tieto väärästä viitteestä', {
        verifyContains(Tulosteet.TUNTEMATON_VIITETYYPPI)
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
        def pakolliset = ViiteTyyppi.book.getPakolliset()

        for(AttrTyyppi atribuutti : pakolliset) {
            verify(io).lueRivi(contains(atribuutti.toString() + "*:"))
        }
    }
}

//Ei vielä testaa kaikkia pakollisa kenttiä
scenario "käyttäjän tulee täyttää pakolliset kentät", {

    given 'käyttäjä lisää uuden viitteen', {
        init()
        lisaaViite(perusviite)
    }
    
    when 'pakollinen kenttä jätetään tyhjäksi', {
        when(io.lueRivi(contains("author"))).thenReturn("", "juri")
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

