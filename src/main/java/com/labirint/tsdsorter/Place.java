package com.labirint.tsdsorter;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Place implements Parcelable {

    private int id_place;
    private String place = "";

    public Place(JSONObject j) throws JSONException {
        id_place = j.getInt("id_Place");
        place = j.getString("place");
    }

    public Place(ResultSet rs)
    {
        try {
            id_place = rs.getInt("id_Place");
            place = rs.getString("place");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected Place(Parcel in) {
        id_place = in.readInt();
        place = in.readString();
    }

    public static final Creator<Place> CREATOR = new Creator<Place>() {
        @Override
        public Place createFromParcel(Parcel in) {
            return new Place(in);
        }

        @Override
        public Place[] newArray(int size) {
            return new Place[size];
        }
    };

    public int getIdPlace() { return id_place; }
    public String getPlace() { return place; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id_place);
        dest.writeString(place);
    }
}
