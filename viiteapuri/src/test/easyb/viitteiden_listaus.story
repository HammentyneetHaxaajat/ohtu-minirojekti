import hammentyneethaxaajat.viiteapuri.*
import hammentyneethaxaajat.viiteapuri.UI.*
import hammentyneethaxaajat.viiteapuri.validaattori.*
import hammentyneethaxaajat.viiteapuri.viite.*
import hammentyneethaxaajat.viiteapuri.IO.*

String[] perustilanne = [
"uusi", "bViite1", "book", "", "juri", "juritus", "juri pub", "testaamisen iloa1", "1946", "", "", "", "", "", "", "",
"uusi", "bViite2", "book", "", "matti", "matitus", "matti pub", "testaamisen iloa2", "1947", "", "", "", "", "", "", "",
"uusi", "bViite3", "book", "", "markus", "markusedit", "markustamo", "testaamisen iloa3", "1948", "", "", "", "", "", "", "",
"uusi", "bViite4", "book", "", "mika", "mikatus", "mika pub", "testaamisen iloa4", "1949", "", "", "", "", "", "", "", "listaa", "lopeta"]

def init(String... komennot) {
	viiteKasittelija = new ViiteKasittelija()
	validaattori = new Validaattori(viiteKasittelija)
	io = new StubIO(komennot)
	app = new Tekstikayttoliittyma(viiteKasittelija, validaattori, io)
}


description "Käyttäjä pystyy listaamaan ohjelmassa olevat viitteet"

scenario "käyttäjä näkee kaikki session viitteet", {

	given 'käyttäjä on syöttänyt useamman viitteen järjestelmään', {
		init(perustilanne)
	}

	when 'käyttäjä syöttää listaa komennon', {
		app.run()
	}

	then 'ohjelma tulostaa siihen syötetyt viitteet', {
		def tulosteet = io.getTulosteet()
		tulosteet.shouldHave("bViite1")
		tulosteet.shouldHave("bViite2")
		tulosteet.shouldHave("bViite3")
		tulosteet.shouldHave("bViite4")
	}
}

//KESKEN
scenario "viitteet listataan luettavassa muodossa", {
	
	given 'käyttäjä on syöttäny useamman viitteen järjestelmään', {
		init(perustilanne)
	}

	when 'käyttäjä syötää listaa komennon', {
		app.run()
	}

	then 'viitteessä olevat tiedot listataan selkokielellä', {
		def tulosteet = io.getTulosteet()
		ensure(tulosteet.last()) {
			contains("bViite1")
			contains("book")
			contains("juri")
			contains("juritus")
			contains("juri pub")
			contains("testaamisen iloa1")
			contains("1946")
		}
	}
}

scenario "viitteistä tulee kaikki tieto listaan", {
	
	given 'käyttäjä on syöttänyt useamman viitteen järjestelmään', {
		init(perustilanne)
	}
	
	when 'käyttäjä syöttää listaa komennon', {
		app.run()
	}
	
	//Katsotaan siältääkö listaus kaikki tietokentät
	then 'kaikki syötetyt tiedot löytyvät tulosteesta', {
		def val = ViiteTyyppi.book.getPakolliset()
        def pak = ViiteTyyppi.book.getValinnaiset()

        //yhdistetään listat
        def attribuutit = val.plus(pak)

        //haetaan Stringi joka sisältää kaikki viitteet tulostettavassa muodossa
        def viiteTiedot = io.getTulosteet().last()

        ensure(viiteTiedot) {
        	contains("nimi")
        	contains("tyyppi")
        	contains("crossref")
        	for(AttrTyyppi atribuutti: attribuutit) {
        		contains(atribuutti.toString())
        	}
        }
	}
}

scenario "edellisten sessioiden viitteitä ei listata", {

	given 'käyttäjä on syöttänyt useamman viitteen järjestelmään', {
		init(perustilanne)
	}
	
	when 'käyttäjä sulkee session', {
		app.run()
	}

	given 'käyttäjä lisää vain yhden viitteen uudessa sessiossa' , {
		init("uusi", "bViite5", "book", "", "mika", "mikatus", "mika pub", "testaamisen iloa4", "1949", "", "", "", "", "", "", "", "listaa", "lopeta")
	}

	when 'käyttäjä syöttää listaa komennon', {
		app.run()
	}
	
	then 'kaikki syötetyt tiedot löytyvät tulosteesta', {
		def tulosteet = io.getTulosteet()
		ensure(tulosteet.last()) {
			doesNotContain("bViite1")
			doesNotContain("bViite2")
			doesNotContain("bViite3")
			doesNotContain("bViite4")
		}
	}
}