package groovycarnage.com.hermes.activity;

import android.content.Intent;
import android.location.Location;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.content.SharedPreferences;

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
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;

import java.util.Observable;
import java.util.Observer;

import groovycarnage.com.hermes.R;
import groovycarnage.com.hermes.activity.threads.threadListActivity;
import groovycarnage.com.hermes.model.Message;
import groovycarnage.com.hermes.utility.GPSListener;
import groovycarnage.com.hermes.utility.IDStrings;
import groovycarnage.com.hermes.utility.SphericalUtilFunctions;
import groovycarnage.com.hermes.utility.URLUtil;
import groovycarnage.com.hermes.utility.VolleyQueue;

public class main extends ActionBarActivity
                        implements
                            OnMapReadyCallback,
                            Observer
{


    public GoogleMap map = null;
    public LatLng lastLocation = null;
    public GPSListener gpsListener;
    public Message[] OPs;
    public double boxHeight = 5;
    public double boxWidth = 3.5;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d("LIFECYCLE", "CREATE");

        if(savedInstanceState != null) {

            boxHeight = savedInstanceState.getDouble(IDStrings.HEIGHTID, boxHeight);
            boxWidth = savedInstanceState.getDouble(IDStrings.WIDTHID, boxWidth);
            try {
                lastLocation = new LatLng(savedInstanceState.getDouble(IDStrings.LATID),
                        savedInstanceState.getDouble(IDStrings.LONGID));
            } catch (NullPointerException e) {
                lastLocation = null;
            }
            try {
                Parcelable[] parcelables = savedInstanceState.getParcelableArray(IDStrings.OPLISTID);
                OPs = new Message[parcelables.length];
                System.arraycopy(parcelables, 0, OPs, 0, parcelables.length);
            } catch (NullPointerException e) {
                Log.d("EXCEPTION GET", "OP was null . . . ");
                OPs = null;
            }
        }


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

//causes exceptions :(
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        if(OPs != null) {
            savedInstanceState.putParcelableArray(IDStrings.OPLISTID, OPs);
        }

        if(lastLocation != null) {
            savedInstanceState.putDouble(IDStrings.LATID, lastLocation.latitude);
            savedInstanceState.putDouble(IDStrings.LONGID, lastLocation.longitude);
        }

        savedInstanceState.putDouble(IDStrings.HEIGHTID, boxHeight);
        savedInstanceState.putDouble(IDStrings.WIDTHID, boxWidth);


        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState){
        boxHeight = savedInstanceState.getDouble(IDStrings.HEIGHTID, boxHeight);
        boxWidth = savedInstanceState.getDouble(IDStrings.WIDTHID, boxWidth);

        try {
            lastLocation = new LatLng(savedInstanceState.getDouble(IDStrings.LATID),
                    savedInstanceState.getDouble(IDStrings.LONGID));
        }catch(NullPointerException e){
            lastLocation = null;
        }
        try {
            Parcelable[] parcelables = savedInstanceState.getParcelableArray(IDStrings.OPLISTID);
            OPs = new Message[parcelables.length];
            System.arraycopy(parcelables, 0, OPs, 0, parcelables.length);
        }catch(NullPointerException e){
            OPs = null;
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onStart() {

        Log.d("LIFECYCLE", "start");
        super.onStart();
    }
    @Override
    protected void onRestart(){

        Log.d("LIFECYCLE", "restart");
        super.onRestart();
    }
    @Override
    protected void onResume(){

        getMap();
        getGPS();

        Log.d("LIFECYCLE", "resume");
        super.onResume();
    }
    @Override
    protected void onStop(){

        Log.d("LIFECYCLE", "stop");
        super.onStop();
    }

    @Override
    protected void onPause(){

        Log.d("LIFECYCLE", "pause");
        super.onPause();
    }

    @Override
    protected void onDestroy(){

        Log.d("LIFECYCLE", "destroy");
        if(gpsListener != null) {
            gpsListener.deleteObserver(this);
            gpsListener.stopLocationUpdates();
        }
        super.onDestroy();
    }



    public void tryUI(){
        if(lastLocation != null){
            if(OPs != null){
                if(map != null){
                    formatMap(lastLocation);
                    makeMarkers();
                }
            }
        }
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
            Intent i = new Intent(getApplicationContext(), Settings.class);
            startActivity(i);

        }else if(id == R.id.action_show_list) {
            Intent i = new Intent(getApplicationContext(), threadListActivity.class);
            /*we might need these to view the list*/
            i.putExtra(IDStrings.LATID, lastLocation.latitude);
            i.putExtra(IDStrings.LONGID, lastLocation.longitude);
            i.putExtra(IDStrings.WIDTHID, boxWidth);
            i.putExtra(IDStrings.HEIGHTID, boxHeight);
            i.putExtra(IDStrings.OPLISTID, OPs);
            startActivity(i);
        }else if(id == R.id.action_new_OP) {
            if(lastLocation != null) {
                Intent i = new Intent(getApplicationContext(), SubmitNewOp.class);
                /*we might need these to post a post*/
                i.putExtra(IDStrings.LATID, lastLocation.latitude);
                i.putExtra(IDStrings.LONGID, lastLocation.longitude);
                startActivity(i);
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setMyLocationEnabled(true);
        lastLocation = new LatLng(38.340, -122.676);
        requestOps();
    }

    public void receiveLocation(Location location){
        Log.d("GPS_STUFF", "LOCATION CHANGED");
        if (location != null) {
            lastLocation = new LatLng(location.getLatitude(), location.getLongitude());
            gpsListener.stopLocationUpdates();
            requestOps();
        }
    }


    public void getGPS(){
        gpsListener = GPSListener.getInstance(this.getApplicationContext());
        gpsListener.addObserver(this);  //we will get notified of location updates now
        gpsListener.connect();
    }

    public void getMap(){
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map_display);

        mapFragment.getMapAsync(this);
    }


    boolean opsRequested = false;

    public void requestOps(){

        synchronized (this) {

            if(opsRequested)   return;
            else        opsRequested = true;

            Log.d("JSON", "FORMING REQUEST");
            Response.Listener<JSONArray> responseListener = new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    Log.d("JSON", response.toString());
                    GsonBuilder gsonBuilder = new GsonBuilder();
                    OPs = gsonBuilder.create().fromJson(response.toString(), Message[].class);
                    tryUI();
                    opsRequested = false;
                }
            };
            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Log.d("JSON", "JSON FAIL1");
                    Log.d("JSON", error.getMessage());
                }
            };

            LatLng[] rect = SphericalUtilFunctions.getRect(boxHeight, boxWidth, lastLocation);

            String url = URLUtil.getPostsByRange(rect);

            Log.d("JSON", "Main requesting resource from " + url);

            JsonArrayRequest request = new JsonArrayRequest(url, responseListener, errorListener);

            Log.d("JSON", "ADDING REQUEST TO QUEUE1");
            VolleyQueue.getRequestQueue(getApplicationContext()).add(request);
        }

    }


    public void formatMap(LatLng me)
    {
        Log.d("GPS_STUFF", "Formatting map");

        map.clear(); // clears map markers/lines/ect.

        //map.moveCamera(CameraUpdateFactory.newLatLngZoom(me, 13));

        map.addMarker(new MarkerOptions()
                .position(me)
                .title("You"));

        SharedPreferences newSettings = getSharedPreferences("MyPrefsFile", 0); // gets settings
        double result = newSettings.getFloat("windowSizeFloat", 5); // gets double from settings named "windowSize"
        boxHeight = result;
        boxWidth = result * 0.7;

        // Create a LatLngBounds that specifies the bounds of the window.
        LatLng rect[] = SphericalUtilFunctions.getRect(boxHeight, boxWidth, me);
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
