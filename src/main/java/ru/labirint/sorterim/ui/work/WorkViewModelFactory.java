package ru.labirint.sorterim.ui.work;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import ru.labirint.core.helper.msg.MsgHelper;
import ru.labirint.sorterim.data.QueryHelper;
import ru.labirint.sorterim.entities.values.ValuesRepository;
import ru.labirint.sorterim.interactors.scanchains.ScanChainUse;


public class WorkViewModelFactory implements ViewModelProvider.Factory {


    QueryHelper queryHelper;
    ValuesRepository valuesRepository;
    MsgHelper msg;
    ScanChainUse scanChainUse;

    public WorkViewModelFactory(QueryHelper queryHelper, ValuesRepository valuesRepository, MsgHelper msg, ScanChainUse scanChainUse) {
        this.queryHelper = queryHelper;
        this.valuesRepository = valuesRepository;
        this.msg = msg;
        this.scanChainUse = scanChainUse;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new WorkViewModel(valuesRepository, msg, scanChainUse);
    }
}
