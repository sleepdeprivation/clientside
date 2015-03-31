package groovycarnage.com.hermes.activity.threads;

import android.app.ActionBar;
//import android.app.ListFragment; why do i need to do this ???!
import android.support.v4.app.ListFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;

import groovycarnage.com.hermes.R;
import groovycarnage.com.hermes.adapters.headListAdapter;
import groovycarnage.com.hermes.adapters.replyListAdapter;
import groovycarnage.com.hermes.model.Message;
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

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public threadDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        OP = getArguments().getParcelable(threadListActivity.OP_ID);

        setHasOptionsMenu(true);

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



        JsonArrayRequest request = new JsonArrayRequest("http://www.scary4cat.com:8003/getRepliesTo?messageID=" + Integer.toString(OP.messageID),
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


}
