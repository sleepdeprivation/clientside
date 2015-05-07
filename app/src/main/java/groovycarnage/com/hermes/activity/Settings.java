package groovycarnage.com.hermes.activity;

/**
 * Created by Domenic on 4/19/2015.
 */


import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;

import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import groovycarnage.com.hermes.R;

import android.content.SharedPreferences;

public class Settings extends ActionBarActivity{

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

       float setting = Float.parseFloat(((EditText)findViewById(R.id.setting)).getText().toString()); // gets setting from view

        Log.d("SETTINGS", "Setting: " + setting);

        //saves the setting
        SharedPreferences settings = getSharedPreferences("MyPrefsFile", 0); // gets settings
        SharedPreferences.Editor editor = settings.edit(); //gets editor
        editor.putFloat("windowSizeFloat", setting); // edits the setting names "windowSize"
        editor.apply(); // commits the change to setting

        // retrieves setting, can be used anywhere (that has import statement).
        // import statement: import android.content.SharedPreferences;
        /*
        SharedPreferences newSettings = getSharedPreferences("MyPrefsFile", 0); // gets settings
        double result = newSettings.getFloat("windowSizeFloat", 5); // gets double from settings named "windowSize"
        */
        finish();
    }

}
