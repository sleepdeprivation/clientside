package groovycarnage.com.hermes.activity;

/**
 * Created by Domenic on 4/19/2015.
 */

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import groovycarnage.com.hermes.R;
import groovycarnage.com.hermes.adapters.headListAdapter;
import groovycarnage.com.hermes.model.Message;
import groovycarnage.com.hermes.utility.URLUtil;
import groovycarnage.com.hermes.utility.VolleyQueue;


public class Settings extends ActionBarActivity{

    protected void onCreate(Bundle savedInstanceState) {
        //Log.d("GPS_STUFF", "inside settings");

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        //Log.d("GPS_STUFF", "inside settings");
        return true;
    }


}
