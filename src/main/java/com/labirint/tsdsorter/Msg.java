package com.labirint.tsdsorter;

import android.view.ViewGroup;
import android.widget.TextView;

import com.labirint.dataaccess.BarCode;

public class Msg extends com.labirint.dataaccess.Msg {

    public Msg(TextView textView, ViewGroup viewGroup, boolean is_change_lparam_by_resources) {
        super(textView, viewGroup, is_change_lparam_by_resources);
    }


    public Msg(TextView textView, ViewGroup viewGroup) {
        super(textView, viewGroup);
    }

    // -------------------------------------------------------------------------------------

    @Override
    protected void setPrefixSays() {
        prefixSays.put(BarCode.Prefix.BADGE, new MsgString(" Сканируй пропуск ", R.drawable.bage));
        prefixSays.put(BarCode.Prefix.Command, new MsgString(" Сканируй команду ", R.drawable.command));
        prefixSays.put(BarCode.Prefix.Stretch, new MsgString(" Сканируй стрейч ",  R.drawable.box));
        prefixSays.put(BarCode.Prefix.PLACE, new MsgString(" Сканируй адрес ", R.drawable.place));
        //prefixSays.put(BarCode.Prefix.StretchOrPlace, new MsgString(" Сканируй стрейч \\или\\адрес снятия ", 0)); // R.drawable.place));
        //prefixSays.put(BarCode.Prefix.Invoice, new MsgString(" Сканируй лист \\накладной", 0)); // R.drawable.place));

    }

    @Override
    public boolean NotExpect(BarCode barcode) {
        boolean b =  !((barcode.getPrefix() == expect_barcode)
                || barcode.getPrefix() == BarCode.Prefix.BADGE
                || (barcode.getPrefix() == BarCode.Prefix.Command && GlobVars.isCommandClose(barcode) && expect_barcode != BarCode.Prefix.BADGE)
                || (barcode.equals(GlobVars.BARCODE_CANCEL) && expect_barcode != BarCode.Prefix.BADGE && expect_barcode != BarCode.Prefix.Command)
        );

        if (b) {Alert();}

        return b;
    }

}
