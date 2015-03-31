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

    // returns the top right and bottom left corners for a square of size "area" centered at "center"
    // return value is stored as an array of LatLng {topRight, bottomLeft}
    // assumes "area" is the area in square miles.
    public static LatLng[] getSquare(double area, LatLng center) {
        area *= 1137.97806; // convert to meters, divided by 2, multiplied by sqrt(2) to get distance from corner to center in meters

        LatLng x = SphericalUtil.computeOffset(center, area, 45); // top right
        LatLng y = SphericalUtil.computeOffset(center, area, 225); // bottom left
        LatLng corners[] = {x, y};
        return (corners);
    }

}
