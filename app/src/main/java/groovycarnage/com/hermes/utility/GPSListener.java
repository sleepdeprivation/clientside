package groovycarnage.com.hermes.utility;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
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
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;

import groovycarnage.com.hermes.R;

/**
 * Created by cburke on 3/26/15.
 */
public class GPSListener
        implements
            GoogleApiClient.ConnectionCallbacks,             //we have to implement these to use GoogleApiClient
            GoogleApiClient.OnConnectionFailedListener,
            LocationListener
{

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    public LocationListener parentRelay;
    public Context context;


    public GPSListener(Context ctxt, LocationListener parent){
        this.context = ctxt;
        this.parentRelay = parent;
        createAPIClient();
        createLocationRequest();
    }

    public void connect(){
        mGoogleApiClient.connect();
    }

    private void createAPIClient(){
        Log.d("GPS_STUFF", "CREATING API CLIENT");
        mGoogleApiClient = new GoogleApiClient.Builder(this.context)
                .addConnectionCallbacks(this)           //hook up to our overriden callbacks
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)           //and we'll need this api
                .build();
    }

    private void createLocationRequest(){
        Log.d("GPS_STUFF", "ATTEMPTING CONNECTION");
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(500);
        mLocationRequest.setFastestInterval(400);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }


    private void startLocationUpdates(){
        Log.d("GPS_STUFF", "REQUESTING UPDATES");

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
        parentRelay.onLocationChanged(location);
    }




}
