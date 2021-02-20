package com.labirint.tsdsorter.entities;

import ru.labirint.core.data.Column;

public class TextResponse {
    @Column(name = "TXT")
    String TXT = "";
    @Column(name = "is_error")
    Integer is_error = 0;

    public boolean isError(){
        return is_error != 0;
    }

    public String getTXT() {
        return TXT;
    }
}
