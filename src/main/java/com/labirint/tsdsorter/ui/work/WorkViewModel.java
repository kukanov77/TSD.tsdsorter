package com.labirint.tsdsorter.ui.work;

import android.text.Spannable;
import android.text.SpannableString;

import androidx.databinding.ObservableField;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import ru.labirint.core.entities.Barcode;
import ru.labirint.core.ui.base.BaseViewModel;
import ru.labirint.core.util.messages.tsdmsg.MsgHelper;
import ru.labirint.core.util.messages.tsdmsg.MsgScanUseCase;
import ru.labirint.core.util.messages.tsdmsg.MsgViewModel;
import com.labirint.tsdsorter.R;
import com.labirint.tsdsorter.data.QueryHelper;
import com.labirint.tsdsorter.interactors.scankeys.ScanUseCase;
import com.labirint.tsdsorter.interactors.scankeys.Scankeys;
import com.labirint.tsdsorter.interactors.RunnableUseCase;
import com.labirint.tsdsorter.entities.values.ValuesRepository;

import static com.labirint.tsdsorter.interactors.scankeys.Scankeys.BAGE;
import static com.labirint.tsdsorter.interactors.scankeys.Scankeys.CMD_ARRANGE;
import static com.labirint.tsdsorter.interactors.scankeys.Scankeys.CMD_GET;
import static com.labirint.tsdsorter.interactors.scankeys.Scankeys.CMD_GET_ONE;
import static com.labirint.tsdsorter.interactors.scankeys.Scankeys.EAN13;
import static com.labirint.tsdsorter.interactors.scankeys.Scankeys.FINISH;
import static com.labirint.tsdsorter.interactors.scankeys.Scankeys.PLACE;


public class WorkViewModel extends BaseViewModel implements MsgViewModel {

    public QueryHelper queryHelper;
    public ValuesRepository valuesRepository;
    public ScanUseCase scanUseCase;
    RunnableUseCase run;
    public MsgHelper msg;


    public Barcode barcode;

    public ObservableField<Spannable> text = new ObservableField<Spannable>(new SpannableString("Сканируй бейдж"));
    public ObservableField<Spannable> personText = new ObservableField<Spannable>(new SpannableString(""));
    public ObservableField<Boolean> isAlert = new ObservableField<Boolean>(false);
    public ObservableField<Integer> backResource = new ObservableField<Integer>(R.drawable.bage);
    MutableLiveData<Integer> beep = new MutableLiveData<Integer>();
    public ObservableField<Boolean> isConnected = new ObservableField<Boolean>(true);
    public MutableLiveData<String> title = new MutableLiveData<String>();
    private MutableLiveData<Boolean> onSay = new MutableLiveData<Boolean>();

    public WorkViewModel(QueryHelper queryHelper, ValuesRepository valuesRepository) {
        this.queryHelper = queryHelper;
        this.valuesRepository = valuesRepository;
        valuesRepository.getPersonText().subscribe(txt -> personText.set(new SpannableString(txt)));
        this.scanUseCase = new ScanUseCase(new Scankeys());
        msg = new MsgHelper(this);
        msg.getOnSay().subscribe(b -> onSay.setValue(b));

        run = new RunnableUseCase(this);

        scanUseCase.getAlert().subscribe(txt -> msg.alert(txt));

        title.setValue("Авторизация");

        scanUseCase.put(run.getPerson, BAGE);

        // расстановка
        scanUseCase.put(run.cmdArrange, BAGE, CMD_ARRANGE);
//        scanUseCase.put(run.runArrangeBox, BAGE, CMD_ARRANGE, EAN13);
//        scanUseCase.put(run.runArrangeBoxPlace, BAGE, CMD_ARRANGE, EAN13, PLACE);
//        scanUseCase.put(run.cmdFinish, BAGE, CMD_ARRANGE, EAN13, FINISH);
//        scanUseCase.put(run.cmdFinish, BAGE, CMD_ARRANGE, FINISH);
        // снятие
        scanUseCase.put(run.cmdGet, BAGE, CMD_GET);
        // снять один
        scanUseCase.put(run.cmdGetOne, BAGE, CMD_GET_ONE);
//        scanUseCase.put(run.runGetOnePlace, BAGE, CMD_GET_ONE, PLACE);
//        //
        scanUseCase.put(run.cmdFinish, BAGE, CMD_GET, FINISH);
        scanUseCase.put(run.cmdFinish, BAGE, CMD_GET_ONE, FINISH);
//        //
        scanUseCase.put(run.cmdBageFinish, BAGE, FINISH);

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

    public MutableLiveData<Integer> getBeep() {
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

    }


    // -----------------------------------------------------------------------------------------

}
