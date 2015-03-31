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
 * Created by cburke on 3/31/15.
 */
/*
    This is a utility class used by the 2 adapters.
    One of the adapters uses both of them, and the other, only one.
    I thought it better to just put it all in one place.
 */
public class AdapterViewGenerators {

    /*
        Generate an OP message inflated from an XML and return it
        Use the data backing the Array Adapter
     */
    public static View generateOPView(int position, View convertView, ViewGroup parent, ArrayAdapter<Message> self){
        Message message = self.getItem(position);
        View itemView;
        if(convertView == null){
            itemView = ((LayoutInflater) self.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                    .inflate(R.layout.op_list_item, null);
        }else{
            itemView = convertView;
        }
        TextView content = (TextView) itemView.findViewById(R.id.content);
        TextView poster = (TextView) itemView.findViewById(R.id.poster);
        TextView latitude = (TextView) itemView.findViewById(R.id.lat);
        TextView longitude = (TextView) itemView.findViewById(R.id.lon);

        content.setText(message.content);
        poster.setText("username here");
        latitude.setText(Float.toString(message.lat));
        longitude.setText(Float.toString(message.lon));

        return itemView;
    }

    /*
        Generate a reply Message by inflating an xml and filling it with the data
        backing the ArrayAdapter
     */
    public static View generateReplyView(int position, View convertView, ViewGroup parent, ArrayAdapter<Message> self){
        Message message = self.getItem(position);
        View itemView;
        if(convertView == null){
            itemView = ((LayoutInflater)self.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                    .inflate(R.layout.reply_list_item, null);
        }else{
            itemView = convertView;
        }
        TextView content = (TextView) itemView.findViewById(R.id.content);
        TextView poster = (TextView) itemView.findViewById(R.id.poster);

        content.setText(message.content);
        poster.setText("reply username here");

        return itemView;
    }

}
