package groovycarnage.com.hermes.model;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by cburke on 3/24/15.
 */
/*

The original class:

public class Message {

    public int messageID;
    public int posterID;
    public int parentID;
    public String content;
    public float lat;
    public float lon;
    public int numUpvotes;
    public int numDownvotes;

}
*/

    //code generated from the above by http://www.parcelabler.com/

    //implementing parcelable allows us to quickly send this object from one activity to another
public class Message implements Parcelable {


    public Integer messageID = -1;
    public int posterID = -1;
    public String uname = "anon";
    public Integer parentID = -1;
    public String content;
    public Double lat = 1000.0;
    public Double lon = 1000.0;
    public int numUpvotes = 0;
    public int numDownvotes = 0;
    public String timePosted = "";

    public Message(){

    }


    protected Message(Parcel in) {
        messageID = in.readInt();
        posterID = in.readInt();
        parentID = in.readInt();
        content = in.readString();
        lat = in.readDouble();
        lon = in.readDouble();
        numUpvotes = in.readInt();
        numDownvotes = in.readInt();
        timePosted = in.readString();
        uname = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(messageID);
        dest.writeInt(posterID);
        dest.writeInt(parentID);
        dest.writeString(content);
        dest.writeDouble(lat);
        dest.writeDouble(lon);
        dest.writeInt(numUpvotes);
        dest.writeInt(numDownvotes);
        dest.writeString(timePosted);
        dest.writeString(uname);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Message> CREATOR = new Parcelable.Creator<Message>() {
        @Override
        public Message createFromParcel(Parcel in) {
            return new Message(in);
        }

        @Override
        public Message[] newArray(int size) {
            return new Message[size];
        }
    };
}
