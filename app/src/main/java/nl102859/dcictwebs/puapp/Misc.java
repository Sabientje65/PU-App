package nl102859.dcictwebs.puapp;

/**
 * Created by Danny on 29-10-2014.
 */
public class Misc {
    public static int countOccurrences(String haystack, char needle){
        int toReturn = 0;

        for(char c : haystack.toCharArray())
            if(c == needle)
                toReturn++;

        return toReturn;
    }
}
