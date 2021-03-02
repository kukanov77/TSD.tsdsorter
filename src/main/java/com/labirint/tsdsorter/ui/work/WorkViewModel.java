package com.labirint.tsdsorter.ui.work;

import android.text.Spannable;
import android.text.SpannableString;

import androidx.databinding.ObservableField;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import ru.labirint.core.entities.Barcode;
import ru.labirint.core.ui.base.BaseViewModel;
import ru.labirint.core.util.messages.Beep;
import ru.labirint.core.util.messages.Msg;
import ru.labirint.core.util.messages.tsdmsg.MsgHelper;
import ru.labirint.core.util.messages.tsdmsg.MsgScanUseCase;
import ru.labirint.core.util.messages.tsdmsg.MsgViewModel;
import ru.labirint.core.util.messages.tsdmsg.StringHelper;

import com.labirint.tsdsorter.R;
import com.labirint.tsdsorter.data.QueryHelper;
import com.labirint.tsdsorter.interactors.scankeys.ScanUseCase;
import com.labirint.tsdsorter.interactors.scankeys.Scankeys;
import com.labirint.tsdsorter.interactors.RunnableUseCase;
import com.labirint.tsdsorter.entities.values.ValuesRepository;

public class WorkViewModel extends BaseViewModel implements MsgViewModel {

    public QueryHelper queryHelper;
    public ValuesRepository valuesRepository;
    public ScanUseCase scanUseCase;
    public MsgHelper msg;

    RunnableUseCase run;

    public Barcode barcode;

    public ObservableField<Spannable> text = new ObservableField<Spannable>(new SpannableString("Сканируй бейдж"));
    public ObservableField<Spannable> personText = new ObservableField<Spannable>(new SpannableString(""));
    public ObservableField<Boolean> isAlert = new ObservableField<Boolean>(false);
    public ObservableField<Integer> backResource = new ObservableField<Integer>(R.drawable.bage);
    MutableLiveData<Beep> beep = new MutableLiveData<Beep>();
    public ObservableField<Boolean> isConnected = new ObservableField<Boolean>(true);
    public MutableLiveData<String> title = new MutableLiveData<String>();
    private MutableLiveData<Boolean> onSay = new MutableLiveData<Boolean>();

    public WorkViewModel(QueryHelper queryHelper, ValuesRepository valuesRepository) {
        this.queryHelper = queryHelper;
        this.valuesRepository = valuesRepository;
        valuesRepository.getPersonText().subscribe(txt -> personText.set(new SpannableString(txt)));
        // - initScanUseCase();
        run = new RunnableUseCase(this);
        this.scanUseCase = new ScanUseCase(new Scankeys(), run);
        // -
        msg = new MsgHelper(this);
        msg.getOnSay().subscribe(b -> onSay.setValue(b));

        scanUseCase.getAlert().subscribe(txt -> msg.alert(txt));

        title.setValue("Авторизация");

    }

    // -----------------------------------------------------------------------------------------
    public LiveData<String> getTitle() { return title; }
    public LiveData<Boolean> onSay(){return onSay; }
    // -----------------------------------------------------------------------------------------
    // --- interface MsgViewModel
    // -----------------------------------------------------------------------------------------

    @Override
    public ObservableField<Spannable> getText() {
        return text;
    }
    @Override
    public ObservableField<Boolean> getIsAlert() {
        return isAlert;
    }
    @Override
    public ObservableField<Integer> getBackResource() {
        return backResource;
    }
    @Override
    public MutableLiveData<Beep> getBeep() {
        return beep;
    }
    @Override
    public MsgScanUseCase getScanUseCase() {
        return scanUseCase;
    }

    // -----------------------------------------------------------------------------------------
    // -----------------------------------------------------------------------------------------

    public void onScan(Barcode barcode) {
        setBarcode(barcode);
        //scanPath.postValue(data.getScanPath());
        scanUseCase.run();
    }



    // -----------------------------------------------------------------------------------------

    public void setBarcode(Barcode barcode) {
        this.barcode = barcode;
        scanUseCase.addKey(barcode);
    }

    public void backScanKey() {
        scanUseCase.backScanKey();
    }

    @Override
    public void onDestroyView() {
        beep = new MutableLiveData<Beep>();
    }


    // -----------------------------------------------------------------------------------------

}
