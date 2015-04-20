import hammentyneethaxaajat.viiteapuri.UI.*
import hammentyneethaxaajat.viiteapuri.validaattori.*
import hammentyneethaxaajat.viiteapuri.viite.*
import hammentyneethaxaajat.viiteapuri.IO.*
import hammentyneethaxaajat.viiteapuri.resurssit.*
import static org.mockito.Mockito.*


def perusviite = [nimi : "aViite", tyyppi : "article", author : "juri", journal : "Testaamisen Iloa", title : "Ilo Testata", volume : "3", year : "1995"]


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
    when(io.lueRivi(contains(Tulosteet.SYOTA_KOMENTO))).thenReturn("uusi", "lopeta")
}

def verifyContains(String vaatimus) {
    verify(io).tulosta(contains(vaatimus))
}

description "Käyttäjä voi luoda uuden article-viitteen"

scenario "käyttäjä kykenee lisäämään uuden article-viitteen", {

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

    given 'käyttäjä antaa vääriä syötteitä', {
        init()
        lisaaViite(perusviite)
    }
    
    when 'käyttäjä antaa väärän komennon ja viitetiedon', {
        //Korvaa lisaaViite() metodissa asetun perustilanteen
        when(io.lueRivi(contains("tyyppi"))).thenReturn("particle", "article")
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
        def pakolliset = ViiteTyyppi.article.getPakolliset()

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
        when(io.lueRivi(contains("nimi"))).thenReturn("", "aViite")
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

//Toistaiseksi ohjelma ei tee tätä
scenario "lisäyksen jälkeen ohjelma listaa lisätyn viitteen", {
    given 'käyttäjä lisää viitteen ohjelmaan', {
        init()
        lisaaViite(perusviite)
    }

    when 'kaikki tiedot lisätään oiken', {
        perustilanne()
        app.run()
    }

    then 'ohjelma listaa viitteen', {
        verifyContains("Viitteen tiedot:")
    }
}