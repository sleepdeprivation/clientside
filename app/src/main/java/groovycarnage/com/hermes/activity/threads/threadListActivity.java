package groovycarnage.com.hermes.activity.threads;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;


import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;

import groovycarnage.com.hermes.R;
import groovycarnage.com.hermes.activity.SubmitNewOp;
import groovycarnage.com.hermes.activity.main;
import groovycarnage.com.hermes.adapters.headListAdapter;
import groovycarnage.com.hermes.model.Message;
import groovycarnage.com.hermes.utility.IDStrings;
import groovycarnage.com.hermes.utility.SphericalUtilFunctions;
import groovycarnage.com.hermes.utility.URLUtil;
import groovycarnage.com.hermes.utility.VolleyQueue;

/**
 *
 */
public class threadListActivity extends ActionBarActivity
            implements SwipeRefreshLayout.OnRefreshListener{


    LatLng lastLocation;
    Message[] OPs;
    double width;
    double height;

    ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = null;


        if(savedInstanceState != null){
            b = savedInstanceState;
        }else if(getIntent() != null){
            b = getIntent().getExtras();
        }

        try {

            lastLocation = new LatLng(  b.getDouble(IDStrings.LATID),
                                        b.getDouble(IDStrings.LONGID));
            width = b.getDouble(IDStrings.WIDTHID);
            height = b.getDouble(IDStrings.HEIGHTID);

            setContentView(R.layout.activity_thread_list);

            ((SwipeRefreshLayout)findViewById(R.id.swipe_refresh_list)).setOnRefreshListener(this);
/*
            if (findViewById(R.id.thread_detail_container) != null) {
                // The detail container view will be present only in the
                // large-screen layouts (res/values-large and
                // res/values-sw600dp). If this view is present, then the
                // activity should be in two-pane mode.
                mTwoPane = true;

                // In two-pane mode, list items should be given the
                // 'activated' state when touched.
                f.setActivateOnItemClick(true);

            }

            f.location = lastLocation;

            f.width = width;
            f.height = height;

*/

            listView = (ListView) findViewById(R.id.thread_list);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Log.d("THREAD_LIST", "ITEM SELECTED");

                    Message OP = (Message) parent.getItemAtPosition(position);

                    startDetails(OP);

                }
            });

            try {
                Parcelable[] parcelables = b.getParcelableArray(IDStrings.OPLISTID);
                OPs = new Message[parcelables.length];
                System.arraycopy(parcelables, 0, OPs, 0, parcelables.length);
                //f.OPs = OPs;
                listView.setAdapter(new headListAdapter(getApplicationContext(), R.layout.activity_thread_list, OPs));
            }catch(NullPointerException e){
                requestMessages();
            }

        }catch(NullPointerException e){ //drop out to main
            Intent i = new Intent(getApplicationContext(), main.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        }

    }

    public void startDetails(Message OP){
        Bundle arguments = new Bundle();
        arguments.putParcelable(IDStrings.OP_ID, OP);
        OP.parentID = -1;

        Intent detailIntent = new Intent(this, threadDetailActivity.class);
        detailIntent.putExtras(arguments);
        startActivity(detailIntent);
    }



    public void requestMessages() {

        Log.d("BUNDLE width", Double.toString(width));
        Log.d("BUNDLE height", Double.toString(height));

        Log.d("JSON", "FORMING REQUEST");
        Response.Listener<JSONArray> responseListener = new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("JSON", response.toString());
                GsonBuilder gsonBuilder = new GsonBuilder();
                OPs = gsonBuilder.create().fromJson(response.toString(), Message[].class);
                listView.setAdapter(new headListAdapter(getApplicationContext(), R.layout.activity_thread_list, OPs));
                ((SwipeRefreshLayout)findViewById(R.id.swipe_refresh_list)).setRefreshing(false);
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("JSON", "JSON FAIL1");
                Log.d("JSON", error.getMessage());
            }
        };

        LatLng[] rect = SphericalUtilFunctions.getRect(height, width, lastLocation);

        String url = URLUtil.getPostsByRange(rect);

        Log.d("JSON", "Requesting resource from " + url);

        JsonArrayRequest request = new JsonArrayRequest(url, responseListener, errorListener);

        Log.d("JSON", "ADDING REQUEST TO QUEUE12");
        VolleyQueue.getRequestQueue(getApplicationContext()).add(request);

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        if(OPs != null) {
            savedInstanceState.putParcelableArray(IDStrings.OPLISTID, OPs);
        }
        if(lastLocation != null) {
            savedInstanceState.putDouble(IDStrings.LATID, lastLocation.latitude);
            savedInstanceState.putDouble(IDStrings.LONGID, lastLocation.longitude);
        }

        savedInstanceState.putDouble(IDStrings.HEIGHTID, height);
        savedInstanceState.putDouble(IDStrings.WIDTHID, width);

        super.onSaveInstanceState(savedInstanceState);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_list, menu);
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
        }else if(id == R.id.action_show_map){
            Intent i = new Intent(getApplicationContext(), main.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);

        }else if(id == R.id.action_new_OP){
            Intent i = new Intent(getApplicationContext(), SubmitNewOp.class);
            i.putExtra("LATITUDE", lastLocation.latitude);
            i.putExtra("LONGITUDE", lastLocation.longitude);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {
        Log.d("SWIPEREFRESH", "REFRESHING! : )");
        requestMessages();
    }
}
