package com.labirint.tsdsorter.ui.work;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.labirint.tsdsorter.data.QueryHelper;
import com.labirint.tsdsorter.entities.values.ValuesRepository;


public class WorkViewModelFactory implements ViewModelProvider.Factory {

    QueryHelper queryHelper;
    ValuesRepository valuesRepository;

    public WorkViewModelFactory(QueryHelper queryHelper, ValuesRepository valuesRepository) {
        this.queryHelper = queryHelper;
        this.valuesRepository = valuesRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new WorkViewModel(queryHelper, valuesRepository);
    }
}
