package nl102859.dcictwebs.puapp;

import java.util.Vector;

/**
 * Created by Danny on 29-10-2014.
 */
public class Artikel {
    private String artikelTitel, artikelAuteur, artikelInhoud;

    public Artikel(String artikelURL){
        Vector<String> info = NetworkConnectivity.frontpageInfo(artikelURL);

        this.artikelTitel = info.elementAt(0);
        if(info.size() == 3) {
            this.artikelAuteur = info.elementAt(1);
            this.artikelInhoud = info.elementAt(2);
        }
        else
            this.artikelInhoud = info.elementAt(1);
    }

    public String getArtikelTitel(){
        return  this.artikelTitel;
    }

    public String getArtikelAuteur(){
        return this.artikelAuteur;
    }

    public String getArtikelInhoud(){
        return  this.artikelInhoud;
    }


    public static String articleName(String articleURL){
        articleURL = articleURL.substring("http://www.pu.nl/".length());
        for(int i = Misc.countOccurrences(articleURL, '/'); i > 1; i--){
            articleURL = articleURL.substring(articleURL.indexOf('/') + 1);
        }

        return articleURL.replaceAll("/", "").replaceAll("-", " ").toUpperCase();
    }
}