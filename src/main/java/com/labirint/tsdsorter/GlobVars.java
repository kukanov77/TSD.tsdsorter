package com.labirint.tsdsorter;

import com.labirint.dataaccess.BarCode;
import com.labirint.dataaccess.Query;

public class GlobVars extends Query {


    public static final String BARCODE_ARRANGE = "33000010"; //расстановка
    public static final String BARCODE_REMOVE = "33000011"; //снять
    public static final String BARCODE_REMOVE_ONE = "33000012"; //снять один
    public static final String BARCODE_CANCEL = "33000015"; // отмена
    public static final String BARCODE_CLOSE = "33000013"; // закончить работу

    public static final int COMMAND_NO = -1;
    public static final int COMMAND_ARRANGE = 0; //расстановка
    public static final int COMMAND_REMOVE = 1; //снять
    public static final int COMMAND_REMOVE_ONE = 2; //снять один
    public static final int COMMAND_CANCEL = 3; //отмена
    public static final int COMMAND_CLOSE = 4; //

    // --------------------------------------------------------------------------------------

    private int command = -1;
    private int last_command = -1; //предыдущая команда
    private int id_place = 0;
    private Place place;
    private int id_sales = -1;
    private int i_stretch = -1;

    // --------------------------------------------------------------------------------------

    public int getCommand() {
        return command;
    }
    public void setCommand(int command) {
        this.command = command;
    }
    public int getLastCommand() {
        return last_command;
    }
    public void setLastCommand(int last_command) {
        this.last_command = last_command;
    }
    public int getIdPlace() {
        return id_place;
    }
    public void setIdPlace(int id_place) {
        this.id_place = id_place;
    }
    public Place getPlace() {
        return place;
    }
    public void setPlace(Place place) {
        this.place = place;
    }
    public int getIdSales() {
        return id_sales;
    }
    public void setIdSales(int id_sales) {
        this.id_sales = id_sales;
    }
    public int getStretch() {
        return i_stretch;
    }
    public void setStretch(int i_stretch) {
        this.i_stretch = i_stretch;
    }
    // --------------------------------------------------------------------------------------

    public static boolean isCommandClose(BarCode barcode) {
        return barcode.equals(barcode.equals(GlobVars.BARCODE_CLOSE));
    }


    public int setCommand(BarCode barcode) {

        //запоминаем предыдущую команду
        last_command = command;

        //

        if (barcode.getBarcode().equals(BARCODE_ARRANGE))
        {
            command = COMMAND_ARRANGE;
        }
        else if (barcode.getBarcode().equals(BARCODE_REMOVE))
        {
            command = COMMAND_REMOVE;
        }
        else if (barcode.getBarcode().equals(BARCODE_REMOVE_ONE))
        {
            command = COMMAND_REMOVE_ONE;
        }
        else if (barcode.getBarcode().equals(BARCODE_CANCEL))
        {
            command = COMMAND_CANCEL;
        }
        else if (barcode.getBarcode().equals(BARCODE_CLOSE))
        {
            command = COMMAND_CLOSE;
        }
        else
        {
            command = COMMAND_NO;
        }

        return command;

    }

    // --------------------------------------------------------------------------------------

}
