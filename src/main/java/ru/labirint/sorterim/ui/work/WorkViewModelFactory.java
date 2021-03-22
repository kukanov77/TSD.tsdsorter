package ru.labirint.sorterim.ui.work;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import ru.labirint.sorterim.data.QueryHelper;
import ru.labirint.sorterim.entities.values.ValuesRepository;


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
