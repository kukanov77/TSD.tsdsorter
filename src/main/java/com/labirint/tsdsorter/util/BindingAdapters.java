package com.labirint.tsdsorter.util;

import android.graphics.Color;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.BindingAdapter;

public class BindingAdapters {
//
    @BindingAdapter("bind:backResource")
    public static void setBackresource(View view, Integer id_resource) {
        view.setBackgroundResource(id_resource);
    }

    @BindingAdapter("bind:textSize")
    public static void setTextSize(TextView view, Boolean isAlert) {
        if (isAlert){
            view.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
        } else {
            view.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
        }
    }

//    @BindingAdapter("bind:backColor")
//    public static void setColor(View view, Boolean is_alert) {
//        if (is_alert) {
//            view.setBackgroundColor(Color.RED);
//        } else {
//            view.setBackgroundColor(Color.WHITE);
//
//        }
//    }


}
