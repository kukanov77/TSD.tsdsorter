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

        prefixSays.Add(new MsgString(" Сканируй пропуск ", R.drawable.bage, BarCode.Prefix.BADGE));
        prefixSays.Add(new MsgString(" Сканируй команду ", R.drawable.command, BarCode.Prefix.COMMAND));
        prefixSays.Add(new MsgString(" Сканируй стрейч ",  R.drawable.box, BarCode.Prefix.STRETCH));
        prefixSays.Add(new MsgString(" Сканируй адрес ", R.drawable.place, BarCode.Prefix.PLACE));

    }

    @Override
    public boolean NotExpect(BarCode barcode) {
        boolean b =  !(barcode.inPrefixes(expect_barcode)
                || barcode.getPrefix() == BarCode.Prefix.BADGE
                || (barcode.getPrefix() == BarCode.Prefix.COMMAND && GlobVars.isCommandClose(barcode) && expect_barcode[0] != BarCode.Prefix.BADGE)
                || (barcode.equals(GlobVars.BARCODE_CANCEL) && expect_barcode[0] != BarCode.Prefix.BADGE && expect_barcode[0] != BarCode.Prefix.COMMAND)
        );

        if (b) {Alert();}

        return b;
    }

}
