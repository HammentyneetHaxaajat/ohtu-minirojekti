import hammentyneethaxaajat.viiteapuri.UI.*
import hammentyneethaxaajat.viiteapuri.validaattori.*
import hammentyneethaxaajat.viiteapuri.viite.*
import hammentyneethaxaajat.viiteapuri.IO.*
import hammentyneethaxaajat.viiteapuri.resurssit.*
import java.io.File;
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

def verifyContains(String vaatimus) {
    verify(io).tulosta(contains(vaatimus))
}

def perustilanne() {
    when(io.lueRivi(contains(Tulosteet.SYOTA_KOMENTO))).thenReturn(Tulosteet.KOMENTO_UUSI, Tulosteet.KOMENTO_LUO_BIBTEX, Tulosteet.KOMENTO_LOPETA)
    when(io.lueRivi(contains(Tulosteet.KYSY_TIEDOSTO_NIMI))).thenReturn("testi")
}


description "Käyttäjä voi luoda viitteistä BiBTex-muotoisen tiedoston"

scenario "käyttäjä voi luoda .bib muotoisen tiedoston viitteineen", {
    given 'käyttäjä lisää viitteet ohjelmaan', {
        init()
        lisaaViite(perusviite)
    }
    
    when 'käyttäjä luo bibtex tiedoston', {
        perustilanne()
        app.run()
    }
    
    then 'bibtidosto on olemassa', {
        File file = new File("testi.bib")
        file.exists().shouldBe true
        file.delete()
    }
}
scenario "tiedoston sisältö vastaa BiBTex:in syntaksia", {
    given 'käyttäjä lisää viitteet ohjelmaan', {
        init()
        lisaaViite(perusviite)
    }
    
    when 'käyttäjä luo bibtex tiedoston', {
        perustilanne()
        app.run()
    }
    
    then 'tiedoston syntaksit ovat kunnossa', {
        File file = new File("testi.bib")
        String fileContents = file.getText('UTF-8')
        fileContents.matches("(.*(\\n)+)?(@.+\\{.+\\n(.*\\s=\\s\\{.*\\},\\n)*\\}(\\n)*)*").shouldBe true
        file.delete()
    }
}

// tätä ei oiken voi testata easyB:llä
//scenario ".bib-tiedosto toimii LaTeX:lla"