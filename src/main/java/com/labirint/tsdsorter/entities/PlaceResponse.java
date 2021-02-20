package com.labirint.tsdsorter.entities;

import ru.labirint.core.data.Column;

public class PlaceResponse {
    @Column(name = "ID_Box")
    Integer idBox = 0;
    @Column(name = "ID_Place")
    Integer idPlace = 0;
    @Column(name = "TXT")
    String TXT = "";


    public boolean isError(){
        if (idPlace == null) {
            if (TXT == null) TXT = "Ошибка ящика";
            return true;
        }
        return idPlace < 1;
    }

    public Integer getIdPlace() {
        if (idPlace == null) return 0;
        return idPlace;
    }

    public String getTXT() {
        if(TXT == null) return "";
        return TXT;
    }

    public int getIdBox() {
        if (idBox == null) return 0;
        return idBox;
    }
}
