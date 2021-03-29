package ru.labirint.sorterim.entities.values;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import ru.labirint.core.entities.Barcode;

@Entity(tableName = "sorterim_values")
public class Values {
    @PrimaryKey
    private int id = 0;
    private int idPerson = 0;
    private String name = "";
    private int idPlace = 0;
    private String Place = "";
    private int idBox = 0;
    private int idSales = 0;
    private int stretch = 0;


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

    public void setIdSales(int idSales) {
        this.idSales = idSales;
    }

    public void setStretch(int stretch) {
        this.stretch = stretch;
    }

    public int getIdSales() {
        return idSales;
    }

    public int getStretch() {
        return stretch;
    }

    // ----------------------------------------------------------------------------------------

}
