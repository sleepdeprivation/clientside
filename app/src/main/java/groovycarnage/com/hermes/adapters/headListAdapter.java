package groovycarnage.com.hermes.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import groovycarnage.com.hermes.R;
import groovycarnage.com.hermes.model.Message;

/**
 * Created by cburke on 3/29/15.
 */
public class headListAdapter extends ArrayAdapter<Message> {

    Message[] messageList;

    public headListAdapter(Context context, int resource, Message[] objects) {
        super(context, resource, objects);
        messageList = objects;
    }

    @Override
    public View getView (int position, View convertView, ViewGroup parent){
        return AdapterViewGenerators.generateOPView(position, convertView, parent, this);
    }



}
