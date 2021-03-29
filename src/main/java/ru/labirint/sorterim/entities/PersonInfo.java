package ru.labirint.sorterim.entities;

import ru.labirint.core.data.Column;

public class PersonInfo {
    @Column(name = "Name")
    String name = "";
    @Column(name = "TXT")
    String txt = "";
    @Column(name = "isError")
    Boolean isError = false;

    public boolean isError() {
        return isError;
    }

    public String getTxt() {
        return txt;
    }

    public String getName() {
        return name;
    }
}
