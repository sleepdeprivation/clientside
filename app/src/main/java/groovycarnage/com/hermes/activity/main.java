package groovycarnage.com.hermes.activity;

import android.content.Intent;
import android.location.Location;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

import java.util.Observable;
import java.util.Observer;

import groovycarnage.com.hermes.R;
import groovycarnage.com.hermes.activity.threads.threadListActivity;
import groovycarnage.com.hermes.utility.DataSender;
import groovycarnage.com.hermes.utility.GPSListener;


public class main extends ActionBarActivity
                        implements
                            OnMapReadyCallback,
                            Observer
{


    public GoogleMap map = null;
    public Location lastLocation = null;
    public GPSListener gpsListener;
    public DataSender dataTransferOut = new DataSender();

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
    }

    public void receiveLocation(Location location){
        Log.d("GPS_STUFF", "LOCATION CHANGED");
        if (location != null) {
            String latString = Double.toString(location.getLatitude());
            String lonString = Double.toString(location.getLongitude());
            if (map != null) {
                LatLng me = new LatLng(location.getLatitude(), location.getLongitude());
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(me, 13));
            }
            //dataTransferOut.execute("{\"Lat\":"+latString+",\"Lon\":"+lonString+"}");
        }
        lastLocation = location;
    }



    @Override
    public void update(Observable observable, Object data) {
        Log.d("GPS_STUFF", "observed");
        if(observable.getClass() == gpsListener.getClass()) {
            receiveLocation((Location) data);
        }
    }
}
