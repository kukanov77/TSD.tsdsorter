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

        prefixSays.Add(new MsgString(" Сканируй пропуск ", R.drawable.bage, Prefix.BADGE));
        prefixSays.Add(new MsgString(" Сканируй команду ", R.drawable.command, Prefix.COMMAND));
        prefixSays.Add(new MsgString(" Сканируй стрейч ",  R.drawable.box, Prefix.STRETCH));
        prefixSays.Add(new MsgString(" Сканируй адрес ", R.drawable.place, Prefix.PLACE));

    }

    @Override
    public boolean NotExpect(BarCode barcode) {
        boolean b =  !(barcode.inPrefixes(expect_barcode)
                || barcode.getPrefix() == Prefix.BADGE
                || (barcode.getPrefix() == Prefix.COMMAND && GlobVars.isCommandClose(barcode) && expect_barcode[0] != Prefix.BADGE)
                || (barcode.equals(GlobVars.BARCODE_CANCEL) && expect_barcode[0] != Prefix.BADGE && expect_barcode[0] != Prefix.COMMAND)
        );

        if (b) {Alert();}

        return b;
    }

}
