import hammentyneethaxaajat.viiteapuri.UI.*
import hammentyneethaxaajat.viiteapuri.validaattori.*
import hammentyneethaxaajat.viiteapuri.viite.*
import hammentyneethaxaajat.viiteapuri.IO.*
import hammentyneethaxaajat.viiteapuri.resurssit.*
import static org.mockito.Mockito.*

def perusviite = [tyyppi : "inproceedings", author : "juri", title : "Testaamisen hulluus", booktitle : "Testaamisen Iloa", year : "1995"]


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

description "Käyttäjä voi luoda uuden inproceedings-viitteen"

scenario "käyttäjä kykenee lisäämään uuden inproceedings-viitteen", {

    given 'käyttäjä antaa komennon uusi', {
        init()
        lisaaViite(perusviite)
    }

    when 'kaikki kentät lisätään oikein', {        
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
        when(io.lueRivi(contains(Tulosteet.TYYPPI))).thenReturn("inprobable", "inproceedings")
        when(io.lueRivi(contains(Tulosteet.SYOTA_KOMENTO))).thenReturn("aasi", Tulosteet.KOMENTO_UUSI, Tulosteet.KOMENTO_LOPETA)
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
        def pakolliset = ViiteTyyppi.inproceedings.getPakolliset()

        for(AttrTyyppi atribuutti : pakolliset) {
            verify(io, atLeastOnce()).lueRivi(contains(atribuutti.toString() + "*:"))
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