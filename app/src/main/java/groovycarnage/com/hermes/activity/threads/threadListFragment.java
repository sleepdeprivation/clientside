package groovycarnage.com.hermes.activity.threads;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;

import groovycarnage.com.hermes.R;
import groovycarnage.com.hermes.activity.SubmitNewOp;
import groovycarnage.com.hermes.activity.main;
import groovycarnage.com.hermes.adapters.headListAdapter;
import groovycarnage.com.hermes.model.Message;
import groovycarnage.com.hermes.utility.VolleyQueue;

/**
 * A list fragment representing a list of threads. This fragment
 * also supports tablet devices by allowing list items to be given an
 * 'activated' state upon selection. This helps indicate which item is
 * currently being viewed in a {@link threadDetailFragment}.
 * <p/>
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class threadListFragment extends ListFragment
{

    /**
     * The serialization (saved instance state) Bundle key representing the
     * activated item position. Only used on tablets.
     */
    private static final String STATE_ACTIVATED_POSITION = "activated_position";

    /**
     * The fragment's current callback object, which is notified of list item
     * clicks.
     */
    private Callbacks mCallbacks = sDummyCallbacks;

    /**
     * The current activated item position. Only used on tablets.
     */
    private int mActivatedPosition = ListView.INVALID_POSITION;

    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callbacks {
        /**
         * Callback for when an item has been selected.
         */
        public void onItemSelected(Message OP);
    }

    /**
     * A dummy implementation of the {@link Callbacks} interface that does
     * nothing. Used only when this fragment is not attached to an activity.
     */
    private static Callbacks sDummyCallbacks = new Callbacks() {
        @Override
        public void onItemSelected(Message OP) {

            Log.d("FRAGMENT", "DUMMY CALLBACK CALLED");
        }
    };

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public threadListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        Log.d("JSON", "FORMING REQUEST");
        Response.Listener<JSONArray> responseListener = new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("JSON", response.toString());
                GsonBuilder gsonBuilder = new GsonBuilder();
                Message[] OPs = gsonBuilder.create().fromJson(response.toString(), Message[].class);
                Log.d("JSON", OPs[0].toString());
                setListAdapter(new headListAdapter(getActivity().getApplicationContext(), R.layout.activity_thread_list, OPs));
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("JSON", "JSON FAIL1");
                Log.d("JSON", error.getMessage());
            }
        };

        //stuck at school for the moment
        String url = "http://serenity-valley.ddns.net:8001/getPostsByRange?latMin=38.336185&lonMin=-122.685516&latMax=38.345812&lonMax=-122.66805";

        JsonArrayRequest request = new JsonArrayRequest(url, responseListener, errorListener);

        Log.d("JSON", "ADDING REQUEST TO QUEUE1");
        VolleyQueue.getRequestQueue(getActivity().getApplicationContext()).add(request);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Restore the previously serialized activated item position.
        if (savedInstanceState != null
                && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
            setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        Log.d("FRAGMENT", "ATTACH");
        Log.d("FRAGMENT", activity.getLocalClassName());

        // Activities containing this fragment must implement its callbacks.
        if (!(activity instanceof Callbacks)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }

        mCallbacks = (Callbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        // Reset the active callbacks interface to the dummy implementation.
        mCallbacks = sDummyCallbacks;
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);
        Message OP = ((Message) getListAdapter().getItem(position));
        OP.parentID = -1;
        mCallbacks.onItemSelected(OP);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mActivatedPosition != ListView.INVALID_POSITION) {
            // Serialize and persist the activated item position.
            outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
        }
    }

    /**
     * Turns on activate-on-click mode. When this mode is on, list items will be
     * given the 'activated' state when touched.
     */
    public void setActivateOnItemClick(boolean activateOnItemClick) {
        // When setting CHOICE_MODE_SINGLE, ListView will automatically
        // give items the 'activated' state when touched.
        getListView().setChoiceMode(activateOnItemClick
                ? ListView.CHOICE_MODE_SINGLE
                : ListView.CHOICE_MODE_NONE);
    }

    private void setActivatedPosition(int position) {
        if (position == ListView.INVALID_POSITION) {
            getListView().setItemChecked(mActivatedPosition, false);
        } else {
            getListView().setItemChecked(position, true);
        }

        mActivatedPosition = position;
    }




    @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.menu_list, menu);
        super.onCreateOptionsMenu(menu, inflater);
        //return true;
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
            Intent i = new Intent(getActivity().getApplicationContext(), main.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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

}
