package ru.labirint.sorterim.ui.work;

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

import ru.labirint.sorterim.R;
import ru.labirint.sorterim.data.QueryHelper;
import ru.labirint.sorterim.interactors.scankeys.ScanUseCase;
import ru.labirint.sorterim.interactors.scankeys.Scankeys;
import ru.labirint.sorterim.interactors.RunnableUseCase;
import ru.labirint.sorterim.entities.values.ValuesRepository;

public class WorkViewModel extends ru.labirint.core_tsd.ui.work.WorkViewModel  {

    public WorkViewModel(QueryHelper queryHelper, ValuesRepository valuesRepository) {
        super(queryHelper, valuesRepository);
    }

    @Override
    protected void initScanUseCase() {
        this.scanUseCase = new ScanUseCase(new Scankeys(), new RunnableUseCase(this));
    }

}
