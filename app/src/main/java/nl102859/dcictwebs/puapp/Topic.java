package nl102859.dcictwebs.puapp;

import java.util.Vector;

/**
 * Created by Danny on 27-10-2014.
 */
public class Topic {
    private String topicTitel, openingPost, originalPoster;

    public Topic(String topicURL){
        Vector<String> topicInfo = NetworkConnectivity.topicInfo(topicURL);

        this.topicTitel     = topicInfo.elementAt(0);
        this.openingPost    = topicInfo.elementAt(1);
        this.originalPoster = topicInfo.elementAt(2);
    }

    public String getTopicTitel(){
        return this.topicTitel;
    }
    public String getOpeningPost(){
        return this.openingPost;
    }
    public String getOriginalPoster(){
        return this.originalPoster;
    }

    public static String topicName(String topicURL){
        if(topicURL.contains("discussions/discussion/"))
            return topicURL.substring(topicURL.indexOf("discussions/discussion/")
                    + "discussions/question/".length() + 1)
                    .replaceAll("/", "").
                            replaceAll("-", " ");
        else
            return topicURL.substring(topicURL.indexOf("discussions/question/")
                    + "discussions/question/".length())
                    .replaceAll("/", "").
                            replaceAll("-", " ");
    }
}
