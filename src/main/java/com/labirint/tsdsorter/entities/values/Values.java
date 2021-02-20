package com.labirint.tsdsorter.entities.values;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import ru.labirint.core.entities.Barcode;

@Entity(tableName = "tbl_values")
public class Values {
    @PrimaryKey
    private int id = 0;
    private int idPerson = 0;
    private String name = "";
    private int idPlace;
    private String Place = "";
    private int idBox;


    // ----------------------------------------------------------------------------------------

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public int getIdPerson() {
        return  idPerson;
    }

    public void setIdPerson(int idPerson) {
        this.idPerson = idPerson;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setIdPlace(Integer idPlace) {
        this.idPlace = idPlace;
    }

    public String getPlace() {
        return Place;
    }

    public void setPlace(String txt) {
        this.Place = txt;
    }

    public int getIdPlace() {
        return idPlace;
    }

    public void setIdBox(int idBox) {
        this.idBox = idBox;
    }

    public int getIdBox() {
        return idBox;
    }

    // ----------------------------------------------------------------------------------------

}
