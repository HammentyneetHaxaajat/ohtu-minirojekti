import hammentyneethaxaajat.viiteapuri.*
import hammentyneethaxaajat.viiteapuri.UI.*
import hammentyneethaxaajat.viiteapuri.validaattori.*
import hammentyneethaxaajat.viiteapuri.viite.*
import hammentyneethaxaajat.viiteapuri.IO.*

String[] perustilanne = ["uusi", "bViite", "book", "", "juri", "juritus", "juri pub", "testaamisen Iloa", "1945", "", "", "", "", "", "", "", "lopeta"]

def init(String... komennot) {
	viiteKasittelija = new ViiteKasittelija()
	validaattori = new Validaattori(viiteKasittelija)
	io = new StubIO(komennot)
	app = new Tekstikayttoliittyma(viiteKasittelija, validaattori, io)
}

description "Käyttäjä voi luoda uuden book-viitteen"

scenario "käyttäjä kykenee lisäämään uuden book-viitteen", {
	
	given 'käyttäjä antaa komennon uusi', {
		init(perustilanne)
	}

	when 'kaikki kentät lisätään oikein', {
		app.run()
	}

	then 'uusi viite lisätään järjestelmään', {
		io.getTulosteet().shouldHave("Viite lisätty onnistuneesti!")
	}

}

scenario "jos annetaan vahingossa virheelliset syötteet kenttiin, antaa ohjelma palautetta", {
	
	given 'käyttäjä antaa vääriä syötteitä', {
		init("aasi", "uusi", "bViite", "böök", "book", "", "juri", "juritus", "juri pub", "testaamisen Iloa", "1945", "", "", "", "", "", "", "", "lopeta")
	}
	
	when 'käyttäjä antaa väärän komennon ja viitetiedon', {
		app.run()
	}
	
	then 'käyttäjälle välittyy tieto väärästä komennosta', {
		io.getTulosteet().shouldHave("Tuntematon komento.")
	}
	and
	then 'käyttäjälle välittyy tieto väärästä viitteestä', {
		io.getTulosteet().shouldHave("Tuntematon viitteen tyyppi. Tuetut tyypit: book.")
	}
}

scenario "käyttäjä tietää mitkä kentät ovat pakollisia", {
	
	given 'käyttäjä syöttää komennon uusi', {
		init(perustilanne)
	}

	when 'kaikki kentät lisätään oikein', {
		app.run()
	}

	then 'käyttäjä saa tiedon pakollisista kentistä', {
		io.getTulosteet().shouldHave("Tähdellä(*) merkityt kentät ovat pakollisia.")
	}
}

//Ei vielä testaa kaikkia pakollisa kenttiä
scenario "käyttäjän tulee täyttää pakolliset kentät", {

	given 'käyttäjä syöttää komennon uusi', {
		init("uusi", "bViite", "book", "", "juri", "", "juritus", "juri pub", "testaamisen Iloa", "1945", "", "", "", "", "", "", "", "lopeta")
	}
	
	when 'pakollinen kenttä jätetään tyhjäksi', {
		app.run()
	}
	
	then 'Käyttäjälle ilmoitetaan, ettei kenttää voi jättää tyhjäksi', {
		io.getTulosteet().shouldHave("Kentän arvo ei saa olla tyhjä!")
	}
}

scenario "käyttäjän ei ole pakko täyttää valinnaisia kenttiä", {

	given 'käyttäjä syöttää komennon uusi', {
		init(perustilanne)
	}

	when 'kaikki valinnaiset kentät jätetään tyhjiksi', {
		app.run()
	}

	then 'uusi viite lisätään järjestelmään', {
		io.getTulosteet().shouldHave("Viite lisätty onnistuneesti!")
	}
}