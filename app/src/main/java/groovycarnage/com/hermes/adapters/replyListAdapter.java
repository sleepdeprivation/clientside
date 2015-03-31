package groovycarnage.com.hermes.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import groovycarnage.com.hermes.R;
import groovycarnage.com.hermes.model.Message;

/**
 * Created by cburke on 3/30/15.
 */
public class replyListAdapter extends ArrayAdapter<Message> {

    Message[] messageList;

    public replyListAdapter(Context context, int resource, Message[] objects) {
        super(context, resource, objects);
        messageList = objects;
    }

    @Override
    public View getView (int position, View convertView, ViewGroup parent){

        if(position == 0) { //the head message goes first
            return AdapterViewGenerators.generateOPView(position, convertView, parent, this);
        }else{
            return AdapterViewGenerators.generateReplyView(position, convertView, parent, this);
        }
    }



}
