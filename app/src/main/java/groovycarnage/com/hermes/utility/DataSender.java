package groovycarnage.com.hermes.utility;

import android.os.AsyncTask;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by cburke on 3/26/15.
 */

    /*
        Demonstration of how to send a block of json out to space
     */

public class DataSender extends AsyncTask<String, Void, Void> {

    /*
        Progaurd (I guess that's what it's for - huh) doesn't like this
        to be executed on the UI thread so we wrap it in an async task.
        It's true that we will be receiving a response (actually more
        than one within the execution of this block) from the server...
     */
    @Override
    protected Void doInBackground(String... params) {

        String JSONstring = params[0];

        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpPost post = new HttpPost("http://45.56.93.26:8003/asdf");

        try {
            //throws UnsupportedEncodingException
            StringEntity message = new StringEntity(JSONstring);

            //set the appropriate header
            post.setHeader("Content-type", "application/json");
            post.setEntity(message);

        } catch (UnsupportedEncodingException e) {  //IDE recommended we catch this
            e.printStackTrace();
        }

        ResponseHandler responseHandler = new BasicResponseHandler();

        try {
            httpClient.execute(post, responseHandler);  //send it!

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

}
