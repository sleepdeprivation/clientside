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

import android.content.SharedPreferences;

public class Settings extends ActionBarActivity{

    public static final String PREFS_NAME = "MyPrefsFile";

    protected void onCreate(Bundle savedInstanceState) {

        Log.d("SETTINGS", "inside settings create");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        Log.d("GPS_STUFF", "inside settings selected");

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void saveSettings(View v){

        Log.d("SETTINGS", "saving settings");

       String setting = ((EditText)findViewById(R.id.setting)).getText().toString(); // gets setting from view

        Log.d("SETTINGS", "Setting: " + setting);

        //saves the setting
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0); // gets settings
        SharedPreferences.Editor editor = settings.edit(); //gets editor
        editor.putString("windowSize", setting); // edits the setting names "windowSize"

        editor.apply(); // commits the change to setting

        // retrieves setting, can be used anywhere (that has import statement).  for testing, can be removed.
        // import atatement: import android.content.SharedPreferences;
        SharedPreferences newSettings = getSharedPreferences(PREFS_NAME, 0); // gets settings
        String result = newSettings.getString("windowSize", "string not found"); // gets string from settings named "windowSize"
        Log.d("SETTINGS", "newSetting: " + result);
    }

}
