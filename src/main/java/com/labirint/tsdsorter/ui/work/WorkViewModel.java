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

public class WorkViewModel extends ru.labirint.core_tsd.ui.work.WorkViewModel  {

    public WorkViewModel(QueryHelper queryHelper, ValuesRepository valuesRepository) {
        super(queryHelper, valuesRepository);
    }

    @Override
    protected void initScanUseCase() {
        this.scanUseCase = new ScanUseCase(new Scankeys(), new RunnableUseCase(this));
    }

}
