package nl102859.dcictwebs.puapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Vector;


public class MyActivity extends ActionBarActivity {
    private LinearLayout layout;
    private Button vorige, home, volgende, herladen, frontPage;
    private int paginaNummer;
    private TextView pagina;
    private boolean isFrontPage = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        paginaNummer = 1;
        layout      = (LinearLayout)findViewById(R.id.topicLayout);
        vorige      = (Button)findViewById(R.id.back);
        home        = (Button)findViewById(R.id.homePage);
        volgende    = (Button)findViewById(R.id.next);
        herladen    = (Button)findViewById(R.id.herlaadButton);
        frontPage   = (Button)findViewById(R.id.frontPage);
        pagina      = (TextView)findViewById(R.id.pagina);


        vorige.setOnClickListener(vorigePagina);
        home.setOnClickListener(homePagina);
        volgende.setOnClickListener(volgendePagina);
        herladen.setOnClickListener(herlaadPagina);
        frontPage.setOnClickListener(frontpageListener);

        vorige.setVisibility(View.INVISIBLE);

        communityCreateButtons();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
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

    private void communityCreateButtons(){
        if(isConnected()) {
            final Vector<String> topicLijst = NetworkConnectivity.topics(paginaNummer);

            for (String s : topicLijst) {
                final String topicURL = s;

                final Button buttocks = new Button(MyActivity.this);
                buttocks.setText(Topic.topicName(s).toUpperCase());

                buttocks.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ViewTopic.topicURL = topicURL;
                        startActivity(new Intent(MyActivity.this, ViewTopic.class));
                    }
                });

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        layout.addView(buttocks);
                    }
                });
            }
        }
        else
            notConnected();
    }

    private void frontpageCreateButtons(){
        if(isConnected()){
            final Vector<String> artikelLijst = NetworkConnectivity.frontPage(paginaNummer);

            for(String s : artikelLijst){
                final String artikelURL = s;

                final Button buttocks = new Button(MyActivity.this);
                buttocks.setText(Artikel.articleName(artikelURL));
                buttocks.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ViewArticle.artikelURL = artikelURL;
                        startActivity(new Intent(MyActivity.this, ViewArticle.class));
                    }
                });

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        layout.addView(buttocks);
                    }
                });
            }
        }
        else
            notConnected();
    }

    private void buttonCreateSelector(){
        if(isFrontPage)
            frontpageCreateButtons();
        else
            communityCreateButtons();
    }

    private void newPage(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                layout.removeAllViews();
                if(paginaNummer == 1)
                    vorige.setVisibility(View.INVISIBLE);
                else
                    vorige.setVisibility(View.VISIBLE);

                pagina.setText("Pagina " + paginaNummer);
            }
        });
    }

    View.OnClickListener vorigePagina = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            paginaNummer--;
            newPage();
            buttonCreateSelector();
        }
    };
    View.OnClickListener homePagina = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            paginaNummer = 1;
            newPage();
            buttonCreateSelector();
        }
    };
    View.OnClickListener volgendePagina = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            paginaNummer++;
            newPage();
            buttonCreateSelector();
        }
    };
    View.OnClickListener herlaadPagina = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            newPage();
            buttonCreateSelector();
        }
    };

    View.OnClickListener frontpageListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            isFrontPage = !isFrontPage;

            if(isFrontPage)
                frontPage.setText("Community");
            else
                frontPage.setText("Front Page");

            paginaNummer = 1;
            newPage();
            buttonCreateSelector();
        }
    };

    private void notConnected(){
        final TextView geenVerbinding = new TextView(MyActivity.this);
        geenVerbinding.setText("Geen verbinding");

        newPage();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                layout.addView(geenVerbinding);
            }
        });
    }

    public boolean isConnected(){
        ConnectivityManager conMan = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = conMan.getActiveNetworkInfo();

        return info != null;
    }
}