package groovycarnage.com.hermes.activity;

import android.content.Intent;
import android.location.Location;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;

import java.util.Observable;
import java.util.Observer;

import groovycarnage.com.hermes.R;
import groovycarnage.com.hermes.activity.threads.threadListActivity;
import groovycarnage.com.hermes.adapters.headListAdapter;
import groovycarnage.com.hermes.model.Message;
import groovycarnage.com.hermes.utility.DataSender;
import groovycarnage.com.hermes.utility.GPSListener;
import groovycarnage.com.hermes.utility.SphericalUtilFunctions;
import groovycarnage.com.hermes.utility.VolleyQueue;


public class main extends ActionBarActivity
                        implements
                            OnMapReadyCallback,
                            Observer
{


    public GoogleMap map = null;
    public Location lastLocation = null;
    public GPSListener gpsListener;
    public DataSender dataTransferOut = new DataSender();
    public Message[] OPs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gpsListener = GPSListener.getInstance(this.getApplicationContext());
        gpsListener.addObserver(this);  //we will get notified of location updates now

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map_display);

        mapFragment.getMapAsync(this);



    }

    private void makeMarkers()
    {
        Log.d("GPS_STUFF", "MAKING MESSAGES");
        for (int i = 0; i < OPs.length; i++)
        {
            Log.d("GPS_STUFF", OPs[i].lat + " " + OPs[i].lon);
            map.addMarker(new MarkerOptions()
                    .position(new LatLng(OPs[i].lat, OPs[i].lon))
                    .title(OPs[i].content));
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_map, menu);
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
        }else if(id == R.id.action_show_list) {
            Intent i = new Intent(getApplicationContext(), threadListActivity.class);
            i.putExtra("LATITUDE", lastLocation.getLatitude());
            i.putExtra("LONGITUDE", lastLocation.getLongitude());
            startActivity(i);
        }else if(id == R.id.action_new_OP) {
            if(lastLocation != null) {
                Intent i = new Intent(getApplicationContext(), SubmitNewOp.class);
                i.putExtra("LATITUDE", lastLocation.getLatitude());
                i.putExtra("LONGITUDE", lastLocation.getLongitude());
                startActivity(i);
            }
        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setMyLocationEnabled(true);


        // map.getUiSettings().setZoomGesturesEnabled(false);
    }

    public void requestOps(){
        Log.d("JSON", "FORMING REQUEST");
        Response.Listener<JSONArray> responseListener = new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("JSON", response.toString());
                GsonBuilder gsonBuilder = new GsonBuilder();
                OPs = gsonBuilder.create().fromJson(response.toString(), Message[].class);
                Log.d("JSON", OPs[0].toString());
                makeMarkers();

            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("JSON", "JSON FAIL1");
                Log.d("JSON", error.getMessage());
            }
        };

        //stuck at school for the moment
        //String url = "http://serenity-valley.ddns.net:8001/getPostsByRange?latMin=38.336185&lonMin=-122.685516&latMax=38.345812&lonMax=-122.66805";
        String url = "http://scary4cat.com:8003/getPostsByRange?latMin=38.336185&lonMin=-122.685516&latMax=38.345812&lonMax=-122.66805";



        JsonArrayRequest request = new JsonArrayRequest(url, responseListener, errorListener);

        Log.d("JSON", "ADDING REQUEST TO QUEUE1");
        VolleyQueue.getRequestQueue(getApplicationContext()).add(request);

    }


    public void receiveLocation(Location location){
        Log.d("GPS_STUFF", "LOCATION CHANGED");
        if (location != null) {
            //String latString = Double.toString(location.getLatitude());
            //String lonString = Double.toString(location.getLongitude());
            if (map != null) {
                LatLng me = new LatLng(location.getLatitude(), location.getLongitude());
                formatMap(me);
                requestOps();
                gpsListener.stopLocationUpdates();
            }
            //dataTransferOut.execute("{\"Lat\":"+latString+",\"Lon\":"+lonString+"}");
        }
        lastLocation = location;
    }

    public void formatMap(LatLng me)
    {
        Log.d("GPS_STUFF", "Formatting map");

        map.clear(); // clears map markers/lines/ect.

        //map.moveCamera(CameraUpdateFactory.newLatLngZoom(me, 13));

        map.addMarker(new MarkerOptions()
                .position(me)
                .title("Hello world"));

        // Create a LatLngBounds that specifies the bounds of the window.
        LatLng rect[] = SphericalUtilFunctions.getRect(5, 3.5, me);
        LatLngBounds window = new LatLngBounds(rect[1], rect[0]);

        // Set the camera to the greatest possible zoom level that includes the bounds
        map.moveCamera(CameraUpdateFactory.newLatLngBounds(window, 0));

        // Instantiates a new Polygon object and adds points to define a rectangle
        PolygonOptions rectOptions = new PolygonOptions()
                .add(new LatLng(rect[1].latitude, rect[0].longitude),
                        new LatLng(rect[0].latitude, rect[0].longitude),
                        new LatLng(rect[0].latitude, rect[1].longitude),
                        new LatLng(rect[1].latitude, rect[1].longitude),
                        new LatLng(rect[1].latitude, rect[0].longitude));

        // Get back the mutable Polygon
        Polygon polygon = map.addPolygon(rectOptions);


    }


    @Override
    public void update(Observable observable, Object data) {
        Log.d("GPS_STUFF", "observed");
        if(observable.getClass() == gpsListener.getClass()) {
            receiveLocation((Location) data);
        }
    }
}
