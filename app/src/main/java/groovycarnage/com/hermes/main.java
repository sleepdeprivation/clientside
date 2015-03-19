package groovycarnage.com.hermes;

import android.content.BroadcastReceiver;
import android.location.Location;
import android.location.LocationManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

import org.apache.http.client.HttpClient;


public class main extends ActionBarActivity
                implements  GoogleApiClient.ConnectionCallbacks,             //we have to implement these to use GoogleApiClient
                            GoogleApiClient.OnConnectionFailedListener,
                            LocationListener,
                            OnMapReadyCallback
{

    public GoogleApiClient mGoogleApiClient;
    public LocationRequest mLocationRequest;
    public GoogleMap map = null;
    public Location lastLocation = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map_display);
        mapFragment.getMapAsync(this);
        LocationManager m = (LocationManager) this.getSystemService(this.LOCATION_SERVICE);
        Log.d("GPS_STUFF", "CREATING API CLIENT");
        createAPIClient();
        Log.d("GPS_STUFF", "ATTEMPTING CONNECTION");
        createLocationRequest();
        mGoogleApiClient.connect();
    }


    private void createAPIClient(){
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)           //hook up to our overriden callbacks
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)           //and we'll need this api
                .build();
    }

    private void createLocationRequest(){
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(500);
        mLocationRequest.setFastestInterval(400);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void startLocationUpdates(){
        Log.d("GPS_STUFF","REQUESTING UPDATES");

        PendingResult<Status> answer =
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        answer.setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(Status status) {
                Log.d("GPS_STUFF","RECEIVED STATUS");
                Log.d("GPS_STUFF", "SUCCESS: " + status.isSuccess());
                Log.d("GPS_STUFF", "STATUS: " + status.getStatus());
                Log.d("GPS_STUFF", "STATUS MESSAGE: " + status.getStatusMessage());
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /*
        Just display the users location for now
     */
    @Override
    public void onConnected(Bundle bundle) {
        Log.d("GPS_STUFF","CONNECTED");
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d("GPS_STUFF", "CONNECTION SUSPENDED");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d("GPS_STUFF", "CONNECTION FAILED");
    }





    @Override
    public void onLocationChanged(Location location) {
        Log.d("GPS_STUFF","LOCATION CHANGED");
        if(location != null){
            ((TextView) findViewById(R.id.latDisplay)).setText(Double.toString(location.getLatitude()));
            ((TextView) findViewById(R.id.lonDisplay)).setText(Double.toString(location.getLongitude()));
            if(map != null){
                LatLng me = new LatLng(location.getLatitude(), location.getLongitude());
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(me, 13));
            }
        }
        lastLocation = location;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setMyLocationEnabled(true);
    }
}
