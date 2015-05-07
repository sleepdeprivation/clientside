package groovycarnage.com.hermes.activity;

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


public class SubmitNewOp extends ActionBarActivity {

    double lat;
    double lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lat = getIntent().getExtras().getDouble("LATITUDE");
        lon = getIntent().getExtras().getDouble("LONGITUDE");
        setContentView(R.layout.activity_submit_new_op);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_submit_new_op, menu);
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

    public void sendOP(View v){
        String content = ((EditText)findViewById(R.id.content)).getText().toString();
        Log.d("OP_SUBMISSION", content);
        Log.d("OP_SUBMISSION", Double.toString(lat));
        Log.d("OP_SUBMISSION", Double.toString(lon));
        Message m = new Message();
        m.content = content;
        m.posterID = 1; //stdout
        m.lat = lat;
        m.lon = lon;

        try {


            m.messageID = null;
            m.parentID = null;
            m.timePosted = null;

            JSONObject submission = new JSONObject(new GsonBuilder().create().toJson(m));

            Log.d("OP_SUBMISSION", submission.toString());
            Log.d("JSON", "FORMING REQUEST");
            Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d("OP_SUBMISSION", "success?");
                }
            };
            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Log.d("OP_SUBMISSION", "JSON FAIL!");
                    //Log.d("OP_SUBMISSION", error.getMessage( ));
                }
            };

            //stuck at school for the moment
            String url = URLUtil.submitOP();

            JsonObjectRequest request = new JsonObjectRequest(url, submission, responseListener, errorListener);

            Log.d("JSON", "ADDING REQUEST TO QUEUE1");
            VolleyQueue.getRequestQueue(getApplicationContext()).add(request);

        } catch (JSONException e) {
            e.printStackTrace();
        };

        Intent i = new Intent(getApplicationContext(), main.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);

    }

}
