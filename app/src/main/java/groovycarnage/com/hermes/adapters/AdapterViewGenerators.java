package groovycarnage.com.hermes.adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

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

        populate(itemView, message);

        /*
        if(position % 2 == 0){
            itemView.setBackgroundColor(Color.parseColor("#d3d3d3"));
        }else{
            itemView.setBackgroundColor(Color.parseColor("#f6f6f6"));
        }
        */

        return itemView;
    }

    public static void populate(View itemView, Message message){
        TextView content = (TextView) itemView.findViewById(R.id.content);
        TextView latitude = (TextView) itemView.findViewById(R.id.lat);
        TextView longitude = (TextView) itemView.findViewById(R.id.lon);
        TextView timestamp = (TextView) itemView.findViewById(R.id.timestamp);
        TextView username = (TextView) itemView.findViewById(R.id.username);

        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        Date d = f.parse(message.timePosted, new ParsePosition(0));

        timestamp.setText(d.toString());
        username.setText(message.uname);

        content.setText(message.content);

        if(message.lat < 800) {

            double outLat = new BigDecimal(message.lat).setScale(3, RoundingMode.HALF_UP).doubleValue();
            double outLon = new BigDecimal(message.lon).setScale(3, RoundingMode.HALF_UP).doubleValue();

            latitude.setText(Double.toString(outLat));
            longitude.setText(Double.toString(outLon));
        }

        View bar = itemView.findViewById(R.id.infobar);
        bar.setBackgroundColor(generateColor());
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
                    .inflate(R.layout.op_list_item, null);
        }else{
            itemView = convertView;
        }

        populate(itemView, message);
        itemView.setPadding(10, 0, 0, 0);
        /*
        if(position % 2 == 0){
            itemView.setBackgroundColor(Color.parseColor("#d3d3d3"));
        }else{
            itemView.setBackgroundColor(Color.parseColor("#f6f6f6"));
        }
        */

        return itemView;
    }

    public static int generateColor(){
        Random random = new Random();
        int r = random.nextInt(255);
        int g = random.nextInt(255);
        int b = random.nextInt(255);
        return Color.rgb((r+255)/2, (g+255)/2, (b+255/2));
    }



}
