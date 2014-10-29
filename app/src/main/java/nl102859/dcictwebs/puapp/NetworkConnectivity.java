package nl102859.dcictwebs.puapp;

import android.text.Html;

import java.io.*;
import java.net.*;
import java.util.Vector;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableFuture;

/**
 * Created by Danny on 25-10-2014.
 */
public class NetworkConnectivity {
    public static Vector<String> topics(final int paginaNummer){
        RunnableFuture<Vector<String>> f;
        f = new FutureTask<Vector<String>>(new Callable<Vector<String>>() {
            @Override
            public Vector<String> call() throws Exception {
                Vector<String> topicList = new Vector<String>();

                try{
                    URL pu = new URL("http://pu.nl/community/discussions/?page=" + paginaNummer);

                    BufferedReader buff = new BufferedReader(
                            new InputStreamReader(pu.openStream()));


                    for(String inStr = ""; inStr != null; inStr = buff.readLine()){
                        if (inStr.contains("class='article discussion'")) {
                            inStr = inStr.substring(inStr.indexOf("class='article discussion'"));
                            topicList.add("http://pu.nl" + inStr.substring(inStr.indexOf("href='") + 6, inStr.indexOf("/'><span class='type'>") + 1));
                        }
                        else if(inStr.contains("class='article question'")){
                            inStr = inStr.substring(inStr.indexOf("class='article question'"));
                            topicList.add("http://pu.nl" + inStr.substring(inStr.indexOf("href='") + 6, inStr.indexOf("/'><span class='type'>") + 1));
                        }
                    }

                    buff.close();
                } catch(MalformedURLException e){
                    System.out.println("Bad URL " + e.getMessage());
                } catch(IOException e){
                    System.out.println("IO Error " + e.getMessage());
                } catch(Exception e){
                    System.out.println("Error, exception " + e.getMessage());
                }

                return topicList;
            }
        });

        Thread t = new Thread(f);

        t.start();

        try{
            t.join();
        }
        catch(Exception e){
            System.out.println("Exc");
        }
        try{
            return f.get();
        }catch(Exception e){

        }

        return new Vector<String>();
    }

    public static Vector<String> topicInfo(final String topicURL){
        RunnableFuture<Vector<String>> f;

        f = new FutureTask<Vector<String>>(new Callable<Vector<String>>() {
            @Override
            public Vector<String> call() throws Exception {
                Vector<String> toReturn = new Vector<String>();
                String topicTitel = "", openingPost = "", originalPoster = "";

                try{
                    topicTitel = Topic.topicName(topicURL);
                    topicTitel = topicTitel.toUpperCase().replaceAll("/", "").replaceAll("-", " ");
                    URL t = new URL(topicURL);
                    BufferedReader topicData = new BufferedReader(new InputStreamReader(t.openStream()));

                    String inStr;
                    boolean isOp = false, isOpeningPost = false;

                    while((inStr = topicData.readLine()) != null){
                        if(inStr.contains("class='information'"))
                            isOpeningPost = true;
                        if(isOpeningPost){
                            openingPost += inStr;
                            isOpeningPost = !inStr.contains("</div></div><aside class='span4'>");
                        }
                        if(isOp)
                            originalPoster = inStr.replaceAll(" ", "");
                        isOp = inStr.contains("<h4 itemprop='author'>");
                    }

                    openingPost = openingPost.substring(openingPost.indexOf("<div class='information'><p>") + "<div class='information'><p>".length());
                    openingPost = openingPost.substring(0, openingPost.indexOf("</div></div>"));
                    openingPost = Html.fromHtml(openingPost).toString();

                }catch(MalformedURLException mue){

                }catch(IOException ioe){

                }catch(Exception e){
                    System.out.println("Error: " + e.getMessage());
                }

                toReturn.add(topicTitel);
                toReturn.add(openingPost);
                toReturn.add(originalPoster);

                return toReturn;
            }
        });

        Thread t = new Thread(f);

        t.start();

        try {
            t.join();
        }catch(InterruptedException ie){

        }

        try{
            return f.get();
        }catch(Exception e){

        }

        return new Vector<String>();
    }

    public static Vector<String> frontPage(final int pagina){
        RunnableFuture f = new FutureTask(new Callable<Vector<String>>(){
            @Override
            public Vector<String> call(){
                final Vector<String> returnValue = new Vector<String>();

                try{
                    URL url = new URL("http://pu.nl/?page=" + pagina);
                    BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

                    String inStr;
                    boolean articleList = false;
                    while((inStr = in.readLine()) != null){
                        if(inStr.contains("filter-results list'")){
                            articleList = true;
                        }
                        if(inStr.contains("paginator")){
                            articleList = false;
                        }
                        if(articleList){
                            if(inStr.contains("href")){
                                String artikel = inStr.substring(inStr.indexOf("href='") + "href='".length());
                                artikel = artikel.substring(0, artikel.indexOf("'"));
                                returnValue.add("http://www.pu.nl" + artikel);
                            }
                        }
                    }
                }catch(Exception e){
                    System.out.println("Error: " + e.getMessage());
                }

                return returnValue;
            }
        });

        Thread t = new Thread(f);
        t.start();

        try{
            t.join();
        }catch(Exception e){

        }

        try{
            return (Vector<String>)f.get();
        }catch(Exception e){

        }

        return new Vector<String>();
    }

    public static Vector<String> frontpageInfo(final String artikelURL){
        FutureTask<Vector<String>> f = new FutureTask<Vector<String>>(new Callable<Vector<String>>() {
            @Override
            public Vector<String> call() throws Exception {
                String titel = Artikel.articleName(artikelURL), auteur = "", inhoud = "";
                boolean isVideo = artikelURL.substring(0, "http://wwww.pu.nl/media".length()).contains("media");

                Vector<String> toReturn = new Vector<String>();
                toReturn.add(titel);

                URL artikel = new URL(artikelURL);
                BufferedReader in = new BufferedReader(new InputStreamReader(artikel.openStream()));

                String inStr;
                if(!isVideo) {
                    boolean isAuthor = false, isInhoud = false;

                    while ((inStr = in.readLine()) != null) {
                        if(isAuthor)
                            auteur = inStr.replaceAll(" ", "");
                        isAuthor = inStr.contains("class='author'");

                        if(inStr.contains("class='information'"))
                            isInhoud = true;
                        if(isInhoud) {
                            inhoud += inStr;
                        }
                        if(inStr.contains("class='goto row'"))
                            isInhoud = false;
                    }

                    inhoud = inhoud.substring(inhoud.indexOf("class=\"intro\">") + "class=\"intro\">".length() + 1);
                    inhoud = inhoud.substring(0, inhoud.indexOf("</div>")).toUpperCase() +
                            inhoud.substring(inhoud.indexOf("</div>") + "</div>".length());
                    inhoud = inhoud.substring(0, inhoud.indexOf("</span></div><div class='goto row'>") +
                            "</span></div><div class='goto row'>".length());

                    inhoud = Html.fromHtml(inhoud).toString();


                    toReturn.add(auteur);
                    toReturn.add(inhoud);

                    return  toReturn;
                }

                boolean isInhoud = false;
                while ((inStr = in.readLine()) != null) {
                    if(inStr.contains("<iframe")){
                        inhoud += "<a href=\"" +
                                inStr.substring(inStr.indexOf("src=\"") + "src=\"".length(),
                                inStr.indexOf("?"))
                                + "\">Youtube Video</a>";
                    }
                    if (inStr.contains("class='information'"))
                        isInhoud = true;
                    if (isInhoud)
                        inhoud += inStr;
                    if (inStr.contains("class='box'"))
                        isInhoud = false;
                }

                inhoud = inhoud.substring(0, inhoud.indexOf("<div class='box'>"));
                inhoud = Html.fromHtml(inhoud).toString();

                toReturn.add(inhoud);

                return toReturn;
            }
        });

        Thread t = new Thread(f);
        t.start();

        try{
            t.join();
        }catch(Exception e){

        }

        try{
            return f.get();
        }catch(Exception e){

        }

        return new Vector<String>();
    }
}
