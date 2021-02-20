package com.labirint.tsdsorter.ui.base;


import ru.labirint.core.entities.Barcode;
import com.labirint.tsdsorter.App;
import com.labirint.tsdsorter.data.QueryHelper;
import com.labirint.tsdsorter.ui.root.RootActivity;

public abstract class BaseFragment extends ru.labirint.core.ui.base.BaseFragment {
    public abstract void onScan(Barcode barcode);
    public QueryHelper sql(){
        return ((RootActivity)getActivity()).getQueryHelper();
    }
    public void scanOn(){((RootActivity)getActivity()).scanOn();}
    public void alert(String txt){
        ((RootActivity)getActivity()).onError(new Throwable(txt));
    }
    public App app(){return (App)getActivity().getApplicationContext();}
}
