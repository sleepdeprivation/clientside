package groovycarnage.com.hermes;

import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
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
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import groovycarnage.com.hermes.model.Message;
import groovycarnage.com.hermes.utility.DataSender;
import groovycarnage.com.hermes.utility.GPSListener;


public class main extends ActionBarActivity
                        implements
                            OnMapReadyCallback,
                            LocationListener
{


    public GoogleMap map = null;
    public Location lastLocation = null;
    public GPSListener gpsListener;
    public DataSender dataTransferOut = new DataSender();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gpsListener = new GPSListener(this.getBaseContext(), this);
        gpsListener.connect();  //we will get notified of location updates now

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map_display);

        mapFragment.getMapAsync(this);

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



    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setMyLocationEnabled(true);
    }

    /*
        We will be notified by our GPSListener
     */
    @Override
    public void onLocationChanged(Location location) {
        Log.d("GPS_STUFF","LOCATION CHANGED");
        if(location != null){
            String latString = Double.toString(location.getLatitude());
            String lonString = Double.toString(location.getLatitude());
            ((TextView) findViewById(R.id.latDisplay)).setText(latString);
            ((TextView) findViewById(R.id.lonDisplay)).setText(lonString);
            if(map != null){
                LatLng me = new LatLng(location.getLatitude(), location.getLongitude());
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(me, 13));
            }
            dataTransferOut.execute("{\"Lat\":"+latString+",\"Lon\":"+lonString+"}");
        }
        lastLocation = location;
    }
}
