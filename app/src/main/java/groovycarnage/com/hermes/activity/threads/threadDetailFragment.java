package groovycarnage.com.hermes.activity.threads;

//import android.app.ListFragment; why do i need to do this ???!
import android.support.v4.app.ListFragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
        import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
        import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

import groovycarnage.com.hermes.R;
import groovycarnage.com.hermes.adapters.AdapterViewGenerators;
import groovycarnage.com.hermes.adapters.replyListAdapter;
import groovycarnage.com.hermes.model.Message;
import groovycarnage.com.hermes.utility.IDStrings;
import groovycarnage.com.hermes.utility.URLUtil;
import groovycarnage.com.hermes.utility.VolleyQueue;


/**
 * A fragment representing a single thread detail screen.
 * This fragment is either contained in a {@link threadListActivity}
 * in two-pane mode (on tablets) or a {@link threadDetailActivity}
 * on handsets.
 */
public class threadDetailFragment extends ListFragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    private Message OP;
    private Message[] replyList;
    private LatLng lastLocation;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public threadDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        OP = getArguments().getParcelable(IDStrings.OP_ID);

        View itemView = this.getActivity().findViewById(R.id.current_OP);

        AdapterViewGenerators.populate(itemView, OP);

        Log.d("JSON", "FORMING REQUEST");
        Response.Listener<JSONArray> responseListener = new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("JSON", response.toString());
                GsonBuilder gsonBuilder = new GsonBuilder();
                replyList = gsonBuilder.create().fromJson(response.toString(), Message[].class);
                setListAdapter(new replyListAdapter(getActivity().getApplicationContext(), R.layout.activity_thread_list, replyList));


            }
        };



        JsonArrayRequest request = new JsonArrayRequest(URLUtil.getRepliesTo(OP.messageID),
                responseListener,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.d("JSON", "JSON FAIL");
                        Log.d("JSON", error.getMessage());
                    }
                });
        Log.d("JSON", "ADDING REQUEST TO QUEUE");
        VolleyQueue.getRequestQueue(getActivity().getApplicationContext()).add(request);

    }


    /*
        The user wants to reply to this message
     */
    public void submitReply(View view) {

        Log.d("SUBMIT REPLY" , "FRAGMENT");

        Message m = new Message();
        m.content = ((EditText) getActivity().findViewById(R.id.reply_text)).getText().toString();
        m.posterID = 2; //stderr
        m.parentID = OP.messageID;
        m.messageID = null;
        m.lat = null;
        m.lon = null;
        m.timePosted = null;

        try {

            JSONObject submission = new JSONObject(new GsonBuilder().create().toJson(m));

            Log.d("SUBMIT REPLY", submission.toString());
            Log.d("JSON", "FORMING REQUEST");
            Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d("SUBMIT REPLY", "success?");
                }
            };
            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Log.d("SUBMIT REPLY", "JSON FAIL!");
                    Log.d("SUBMIT REPLY", error.getMessage( ));
                }
            };

            //stuck at school for the moment
            String url = URLUtil.submitReply();

            JsonObjectRequest request = new JsonObjectRequest(url, submission, responseListener, errorListener);

            Log.d("JSON", "ADDING REQUEST TO QUEUE1");
            VolleyQueue.getRequestQueue(getActivity().getApplicationContext()).add(request);

        } catch (JSONException e) {
            e.printStackTrace();
        };
    }


}
