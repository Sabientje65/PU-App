package nl102859.dcictwebs.puapp;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;


public class ViewArticle extends ActionBarActivity {
    LinearLayout layout;
    public static String artikelURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_article);

        layout = (LinearLayout)findViewById(R.id.articleStart);

        Artikel artikel = new Artikel(artikelURL);

        this.setTitle(Artikel.articleName(artikelURL));

        final boolean isVideo = artikel.getArtikelAuteur() != "";

        final TextView titel = new TextView(ViewArticle.this);
        final TextView auteur = new TextView(ViewArticle.this);
        final TextView inhoud = new TextView(ViewArticle.this);

        titel.setText("Titel: " + artikel.getArtikelTitel());
        if(!isVideo)
            auteur.setText("Auteur: " + artikel.getArtikelAuteur());
        inhoud.setText("Inhoud: " + artikel.getArtikelInhoud());

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                layout.addView(titel);
                if (!isVideo)
                    layout.addView(auteur);
                layout.addView(inhoud);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.view_article, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
