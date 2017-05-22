package vn.tonish.hozo.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by LongBui on 5/20/17.
 */

public class MiniTask implements Parcelable {
    private int id;
    private String title;
    private String address;
    private double lat;
    private double lon;

    public MiniTask() {

    }

    protected MiniTask(Parcel in) {
        id = in.readInt();
        title = in.readString();
        address = in.readString();
        lat = in.readDouble();
        lon = in.readDouble();
    }

    public static final Creator<MiniTask> CREATOR = new Creator<MiniTask>() {
        @Override
        public MiniTask createFromParcel(Parcel in) {
            return new MiniTask(in);
        }

        @Override
        public MiniTask[] newArray(int size) {
            return new MiniTask[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(address);
        dest.writeDouble(lat);
        dest.writeDouble(lon);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }
}
