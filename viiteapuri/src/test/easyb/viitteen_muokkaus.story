import hammentyneethaxaajat.viiteapuri.UI.*
import hammentyneethaxaajat.viiteapuri.validaattori.*
import hammentyneethaxaajat.viiteapuri.viite.*
import hammentyneethaxaajat.viiteapuri.IO.*
import hammentyneethaxaajat.viiteapuri.resurssit.*




String[] perustilanne = [
"uusi", "bViite1", "book", "", "juri", "juritus", "juri pub", "testaamisen iloa1", "1946", "", "", "", "", "", "", "", "muokkaa", "bViite1", "year", "2000", "lopeta"]


def init(String... komennot) {
    viiteKasittelija = new ViiteKasittelija()
    validaattori = new Validaattori(viiteKasittelija)
    io = new StubIO(komennot)
    app = new Tekstikayttoliittyma(viiteKasittelija, validaattori, io)
}

description "Käyttäjä voi muokata viitteitä"

scenario "käyttäjän on nähtävä mahdolliset muokattavat viitteet", {

        given 'käyttäjä lisää viitteet ohjelmaan', {
            init(perustilanne)
        }
    
        when 'käyttäjä syötää muokkaa komennon', {
            app.run()
        }
        //purkkaa
        then 'viite on pmuokattu ohjelmasta', {
            io.getTulosteet().shouldHave("Muokattavissa olevat viitteet: bViite1")
        }
}

scenario "käyttäjä voi valita muokattavan kentän ilman että käydään jokainen kenttä läpi", {
        given 'käyttäjä lisää viitteet ohjelmaan', {
            init(perustilanne)
        }
        when 'käyttäjä syöttää muokkaa komennon', {
            app.run()
        }
        then 'viite on muokattu onnistuneesti', {
            io.getTulosteet().shouldHave("Kentän arvo päivittynyt onnistuneesti.")
        }

}

scenario "käyttäjä näkee mahdolliset kentät", {
        given 'käyttäjä lisää viitteet ohjelmaan', {
            init(perustilanne)
        }
        when 'käyttäjä syöttää muokkaa komennon', {
            app.run()
        }
        then 'viite on muokattu onnistuneesti', {
            io.getTulosteet().shouldHave("attribuutti:")
        }
}
