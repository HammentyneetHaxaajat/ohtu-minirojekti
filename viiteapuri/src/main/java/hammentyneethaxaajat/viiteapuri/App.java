package hammentyneethaxaajat.viiteapuri;

/**
 *
 * @author Hämmentyneet Haxaajat
 */
public class App {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        ViiteKasittelija vk = new ViiteKasittelija();
        Validaattori v = new Validaattori(vk);
        IO io = new KomentoriviIO();
        new Tekstikayttoliittyma(vk, v, io).run();
        
    }

}
