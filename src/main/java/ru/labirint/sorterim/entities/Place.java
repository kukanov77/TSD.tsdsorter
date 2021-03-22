package ru.labirint.sorterim.entities;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import ru.labirint.core.data.Column;

public class Place {
    @Column(name = "id_Place")
    private Integer id_place;
    @Column(name ="place")
    private String place = "";

    public Place(JSONObject j) throws JSONException {
        id_place = j.getInt("id_Place");
        place = j.getString("place");
    }

    public String getPlace() {
        return place;
    }

    public Integer getIdPlace() {
        return Optional.ofNullable(id_place).orElse(0);
    }
}
