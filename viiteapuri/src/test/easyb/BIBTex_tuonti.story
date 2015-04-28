import hammentyneethaxaajat.viiteapuri.UI.*
import hammentyneethaxaajat.viiteapuri.validaattori.*
import hammentyneethaxaajat.viiteapuri.viite.*
import hammentyneethaxaajat.viiteapuri.IO.*
import hammentyneethaxaajat.viiteapuri.resurssit.*
import static org.mockito.Mockito.*

def init() {
    viiteKasittelija = new ViiteKasittelija()
    validaattori = new Validaattori(viiteKasittelija)
    io = mock(KomentoriviIO.class)
    app = new Tekstikayttoliittyma(viiteKasittelija, validaattori, io)
}

def yleisKomento(String komento) {
    when(io.lueRivi(anyString())).thenReturn(komento)
}

def verifyContains(String vaatimus) {
    verify(io).tulosta(contains(vaatimus))
}

def perustilanne() {
    File file = new File("src/test/easyb/easybTesti.bib");
    when(io.lueRivi(contains(Tulosteet.SYOTA_KOMENTO))).thenReturn(Tulosteet.KOMENTO_TUO_BIBTEX, Tulosteet.KOMENTO_LOPETA)    
    when(io.lueRivi(contains(Tulosteet.KYSY_TIEDOSTO_POLKU))).thenReturn(file.getAbsolutePath())
}

description "Käyttäjä voi lukea BiBTex-tiedoston ohjelmaan"


scenario "Käyttäjä voi tuoda ohjelmaan valmiin BiBTex-tiedoston", {

    given 'Käyttäjä käynnistää ohjelman', {
        init()
    }
    
    when 'Käyttäjä valitsee tuo bibtex komennon ja antaa oikean polun', {
        perustilanne()
        app.run()
    }
    
    then 'BiBTex tiedoston sisältö tuodaan onnistuneesti', {
        verifyContains("Juria")
        verifyContains("Tori, Juveri2015")
        verify(io, atMost(2)).tulosta(contains("Juri2015"))

    }
}
scenario "Tiedosto yritetään tuoda käyttäjän antamasta polusta", {

    given 'Käyttäjä käynnistää ohjelman', {
        init()
    }
    
    when 'Käyttäjä valitsee tuo bibtex komennon', {
        perustilanne()
        app.run()
    }
    
    then 'Ohjelma pyytää käyttäjältä tiedoston polkua', {
        verify(io).lueRivi(contains(Tulosteet.KYSY_TIEDOSTO_POLKU))
    }
}

scenario "Jos tiedosto ei löydy, ilmoitetaan siitä käyttäjälle", {

    given 'Käyttäjä käynnistää ohjelman', {
        init()
    }
    
    when 'Käyttäjä valitsee tuo bibtex komennon ja antaa väärän', {
        perustilanne()
        when(io.lueRivi(contains(Tulosteet.KYSY_TIEDOSTO_POLKU))).thenReturn("Don't tell me what to do!!!")

        app.run()
    }
    
    then 'Käyttäjä saa tiedon virheellisestä polusta', {
        verifyContains(Tulosteet.VIRHE_OLEMATONTIEDOSTO)
    }
}
scenario "Jos tiedosto on epäkelvollinen, ilmoitetaan siitä käyttäjälle", {

    given 'Käyttäjä käynnistää ohjelman', {
        init()
    }
    
    when 'Käyttäjä valitsee tuo bibtex komennon ja antaa epäkelvon bib tiedoston', {
        perustilanne()
        File file = new File("src/test/easyb/epakelpo.bib");
        when(io.lueRivi(contains(Tulosteet.KYSY_TIEDOSTO_POLKU))).thenReturn(file.getAbsolutePath())
        app.run()
    }
    
    then 'Käyttäjä saa tiedon huonosta formaatista', {
        verifyContains(Tulosteet.VIRHE_TIEDOSTOFORMAATTI)
    }
}

scenario "Tiedostossa olevat viitteet ladataan ohjelmaan", {

    given 'Käyttäjä käynnistää ohjelman', {
        init()
    }
    
    when 'Käyttäjä valitsee tuo bibtex komennon ja antaa oikean polun', {
        perustilanne()
        when(io.lueRivi(contains(Tulosteet.SYOTA_KOMENTO))).thenReturn(
            Tulosteet.KOMENTO_TUO_BIBTEX,
            Tulosteet.KOMENTO_LISTAA,
            Tulosteet.KOMENTO_LOPETA) 
        app.run()
    }
    
    then 'Tuodut viitteet näkyvät listaa komennolla', {
        verify(io, atLeast(2)).tulosta(contains("Juri2015"))
        verify(io, atLeast(2)).tulosta(contains("Juria"))
        verify(io, atLeast(2)).tulosta(contains("Tori, Juveri2015"))
    }
}


scenario "BiBTex-tiedostoa tuodessa samannimiset viitteet ohitetaan tiedostossa", {
    
    given 'Käyttäjä käynnistää ohjelman', {
        init()
    }
    
    when 'Käyttäjä valitsee tuo bibtex komennon ja antaa oikean polun', {
        perustilanne()
        when(io.lueRivi(contains(Tulosteet.SYOTA_KOMENTO))).thenReturn(
            Tulosteet.KOMENTO_TUO_BIBTEX,
            Tulosteet.KOMENTO_LISTAA,
            Tulosteet.KOMENTO_LOPETA) 
        app.run()
    }
    
    then 'toinen samanniminen viite ei tulostu listaa komennolla', {
        //Tuonnissa listataan molemmat viitteet vaikka ovatkin saman nimisiä
        //Jos toista viitettä ei tuotu, niin listaa komennolla se näkyy vain kerran
        verify(io, atMost(3)).tulosta(contains("Juri2015"))
    }
}

/*Tulee jo testatuksi edellisiisä testeissä

scenario "Käyttäjälle listataan tiedostosta ladatut viitteet"
scenario "BiBTex-tiedostossa voi olla useita viitteitä"
*/
