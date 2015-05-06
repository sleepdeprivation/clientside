package groovycarnage.com.hermes.utility;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by cburke on 4/14/15.
 *
 * I'm sick of writing out these URL's only to change them
 * I've made a utility for generating the urls from data
 *
 */
public class URLUtil {

    public static final String BASEURL = "http://scary4cat.com:8003/";
    //public static final String BASEURL = "http://serenity-valley.ddns.net:8003/";

    public static final String getPostsByRange(LatLng[] rect) {
        return
                BASEURL + "getPostsByRange?" +
                        "latMin=" + rect[1].latitude +
                        "&lonMin=" + rect[1].longitude +
                        "&latMax=" + rect[0].latitude +
                        "&lonMax=" + rect[0].longitude;
    }


    public static final String getRepliesTo(Integer messageID) {
        return BASEURL + "getRepliesTo?parentID=" + messageID.toString();
    }

    public static final String submitOP(){
        return BASEURL + "submit/newop";
    }
    public static final String submitReply(){
        return BASEURL + "submit/newreply";
    }
}
