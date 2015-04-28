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
    when(io.lueRivi(contains(Tulosteet.SYOTA_KOMENTO))).thenReturn("1", "2", "3", "4", "5", "6", "7", "8")
    when(io.lueRivi(contains(Tulosteet.VIITE))).thenReturn("juri1995")
    when(io.lueRivi(contains(Tulosteet.ATTRIBUUTTI))).thenReturn("volume")
    when(io.lueRivi(contains(Tulosteet.KYSY_TIEDOSTO_NIMI))).thenReturn("testi")
}

description "numeroidut käskyt"

scenario 'Käyttäjä näkee komentoja vastaavat numerot', {

    given 'käyttäjä käynnistää ohjelman', {
        init()
    }
    
    when 'käyttäjä ei tee muuta', {        
        yleisKomento(Tulosteet.KOMENTO_LOPETA)
        app.run()
    }
    
    then 'ohjelma tulostaa kaikki mahdolliset käskyt ja niiden numeroinnin', {
        for (int i = 1; i <= 7; i++) {
            verifyContains(Integer.toString(i))
        }
    }

}

scenario 'Käyttäjä voi valita komennon sitä vastaavalla numerolla', {
        given 'käyttäjä käynnistää ohjelman', {
            init()
            lisaaViite(perusviite)
        }
    
        when 'description', {
            perustilanne()
            app.run()
            File file = new File("testi.bib")
            file.delete()
        }
    
        then 'description', {
            verifyContains(Tulosteet.UUDEN_VIITTEEN_LUONTI)
            verifyContains(Tulosteet.MUOKATTAVISSA_OLEVAT_VIITTEET)
            verifyContains(Tulosteet.POISTETTAVISSA_OLEVAT_VIITTEET)
            verifyContains("Listataan viitteet")
            verifyContains("Ehdon toteuttavat viitteet:")
            verifyContains(Tulosteet.TIEDOSTONLUONTI_ONNISTUI)
            verifyContains(Tulosteet.VIRHE_OLEMATONTIEDOSTO)
            verifyContains(Tulosteet.VIESTI_HEIHEI)
        }
}