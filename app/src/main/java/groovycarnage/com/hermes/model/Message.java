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

<<<<<<< HEAD
    public int messageID;
    public int posterID;
    public int parentID;
    public String content;
    public float lat;
    public float lon;
    public int numUpvotes;
    public int numDownvotes;
=======
    public Integer messageID = null;
    public int posterID;
    public Integer parentID = null;
    public String content;
    public Double lat = null;
    public Double lon = null;
    public int numUpvotes = 0;
    public int numDownvotes = 0;

    public Message(){

    }
>>>>>>> ca57aa86b904643f662eeb4e0bbd74fa83180d17


    protected Message(Parcel in) {
        messageID = in.readInt();
        posterID = in.readInt();
        parentID = in.readInt();
        content = in.readString();
<<<<<<< HEAD
        lat = in.readFloat();
        lon = in.readFloat();
=======
        lat = in.readDouble();
        lon = in.readDouble();
>>>>>>> ca57aa86b904643f662eeb4e0bbd74fa83180d17
        numUpvotes = in.readInt();
        numDownvotes = in.readInt();
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
<<<<<<< HEAD
        dest.writeFloat(lat);
        dest.writeFloat(lon);
=======
        dest.writeDouble(lat);
        dest.writeDouble(lon);
>>>>>>> ca57aa86b904643f662eeb4e0bbd74fa83180d17
        dest.writeInt(numUpvotes);
        dest.writeInt(numDownvotes);
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
