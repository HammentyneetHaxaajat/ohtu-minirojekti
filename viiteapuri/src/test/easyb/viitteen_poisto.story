import hammentyneethaxaajat.viiteapuri.UI.*
import hammentyneethaxaajat.viiteapuri.validaattori.*
import hammentyneethaxaajat.viiteapuri.viite.*
import hammentyneethaxaajat.viiteapuri.IO.*
import hammentyneethaxaajat.viiteapuri.resurssit.*

String[] perustilanne = [
"uusi", "bViite1", "book", "", "juri", "juritus", "juri pub", "testaamisen iloa1", "1946", "", "", "", "", "", "", "",
"uusi", "bViite2", "book", "", "matti", "matitus", "matti pub", "testaamisen iloa2", "1947", "", "", "", "", "", "", "",
"uusi", "bViite3", "book", "", "markus", "markusedit", "markustamo", "testaamisen iloa3", "1948", "", "", "", "", "", "", "",
"uusi", "bViite4", "book", "", "mika", "mikatus", "mika pub", "testaamisen iloa4", "1949", "", "", "", "", "", "", "", "poista", "bViite3", "poista", "listaa", "lopeta"]


def init(String... komennot) {
    viiteKasittelija = new ViiteKasittelija()
    validaattori = new Validaattori(viiteKasittelija)
    io = new StubIO(komennot)
    app = new Tekstikayttoliittyma(viiteKasittelija, validaattori, io)
}

description "Käyttäjä voi poistaa viitteen"

scenario "käyttäjän on nähtävä mahdolliset poistettavat viitteet", {

        given 'käyttäjä lisää viitteet ohjelmaan', {
            init(perustilanne)
        }
    
        when 'käyttäjä syötää poista komennon', {
            app.run()
        }
        //purkkaa
        then 'viite on poistettu ohjelmasta', {
            io.getTulosteet().shouldHave("Poistettavissa olevat viitteet: bViite1, bViite2, bViite3, bViite4")
        }
}

scenario "käyttäjä voi poistaa viitteen", {

     given 'käyttäjä lisää viitteet ohjelmaan', {
            init(perustilanne)
        }
    
        when 'käyttäjä poistaa viitteen ohjelmasta', {
            app.run()
        }
        //purkkaa
        then 'viite on poistettu ohjelmasta', {
            def viitteet = io.getTulosteet()
            viitteet = viitteet.get(viitteet.size() - 4)
            viitteet.shouldNotHave('bViite3')
        }
}
scenario "ohjelma tulostaa poistettavan viitteen tiedot"

scenario "ohjelman on varmistettava käyttäjältä viitteen poistaminen", {
    given 'käyttäjä listaa viitteet ohjelmaan', {
        init(perustilanne)
    }
    
    when 'käyttäjä syöttää poista komennon ja valitsee poistettavan viitteen', {
        app.run()
    }
    
    then 'käyttäjää pyydetään varmistamaan poistaminen', {
        io.getTulosteet().shouldHave(Tulosteet.KYSY_VARMISTUS)
    }
}