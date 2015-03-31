package groovycarnage.com.hermes.utility;

import android.content.Context;
import android.util.Log;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;

import org.json.JSONArray;

import groovycarnage.com.hermes.model.Message;

/**
 * Created by cburke on 3/29/15.
 */
public class VolleyQueue {
    private static RequestQueue instance;

    public static void generateRequestQueue(Context context){
        Log.d("JSON", "CONSTRUCTING QUEUE");
        Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024);
        Network network = new BasicNetwork(new HurlStack());
        instance= new RequestQueue(cache, network);
        instance.start();
    }

    public static RequestQueue getRequestQueue(Context context){
        if(instance == null){
           generateRequestQueue(context);
        }
        return instance;
    }

}
