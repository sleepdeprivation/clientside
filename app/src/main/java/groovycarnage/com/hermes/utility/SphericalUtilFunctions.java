package groovycarnage.com.hermes.utility;

/**
 * Created by Domenic on 3/30/2015.
 */

import com.google.maps.android.SphericalUtil;
import com.google.android.gms.maps.model.LatLng;

public class SphericalUtilFunctions {

    // returns the distance between two LatLng points in miles
    public static double getDistance(LatLng a, LatLng b) {
        double distance = SphericalUtil.computeDistanceBetween(a, b);
        distance *= 0.000621371; //converts from meters to miles
        return distance;
    }

    // returns the top right and bottom left corners for a rectangle with a certain height and width centered at "center"
    // return value is stored as an array of LatLng {topRight, bottomLeft}
    // assumes "height" and "width" are the height and width of the rectangle in miles
    public static LatLng[] getRect(double height, double width, LatLng center) {

        height *= 804.672; // convert to meters, divided by 2, to get distance from corner to side in meters
        width *= 804.672; // convert to meters, divided by 2, to get distance from corner to side in meters

        LatLng x1 = SphericalUtil.computeOffset(center, height, 0); // top
        LatLng x2 = SphericalUtil.computeOffset(center, height, 180); // bottom
        LatLng y1 = SphericalUtil.computeOffset(center, width, 270); // left
        LatLng y2 = SphericalUtil.computeOffset(center, width, 90); // right

        LatLng x = new LatLng(x1.latitude, y2.longitude); // top right
        LatLng y = new LatLng(x2.latitude, y1.longitude); // bottom left

        LatLng corners[] = {x, y};
        return (corners);
    }

}
