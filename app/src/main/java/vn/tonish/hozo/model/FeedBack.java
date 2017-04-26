package vn.tonish.hozo.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by huyquynh on 4/20/17.
 */

public class FeedBack implements Parcelable {

    int id;
    String content;
    String name;
    String time;
    double rate;

    protected FeedBack(Parcel in) {
        id = in.readInt();
        content = in.readString();
        name = in.readString();
        time = in.readString();
        rate = in.readDouble();
    }

    public static final Creator<FeedBack> CREATOR = new Creator<FeedBack>() {
        @Override
        public FeedBack createFromParcel(Parcel in) {
            return new FeedBack(in);
        }

        @Override
        public FeedBack[] newArray(int size) {
            return new FeedBack[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(content);
        parcel.writeString(name);
        parcel.writeString(time);
        parcel.writeDouble(rate);
    }
}
