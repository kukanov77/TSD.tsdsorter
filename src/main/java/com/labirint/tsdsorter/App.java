package com.labirint.tsdsorter;

import androidx.lifecycle.ViewModelProvider;

import com.labirint.tsdsorter.data.AppDatabase;
import com.labirint.tsdsorter.data.QueryHelper;
import com.labirint.tsdsorter.entities.values.ValuesRepository;
import com.labirint.tsdsorter.ui.work.WorkViewModelFactory;

public class App extends ru.labirint.core_tsd.App  {

    //QueryHelper queryHelper;
    ValuesRepository valuesRepository;


    // ------------------------------------------------------------------------------------------

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    protected void initQueryHelper() {
        valuesRepository = new ValuesRepository(getAppDatabase().getValuesDao());
        queryHelper = new QueryHelper(queryRepository, valuesRepository);
    }

    @Override
    protected void initAppDatabase() {
        appDatabase = AppDatabase.getInstance(this);
    }

    public AppDatabase getAppDatabase(){return (AppDatabase) appDatabase;}


    // ------------------------------------------------------------------------------------------

    public QueryHelper getQueryHelper() {
        return (QueryHelper) queryHelper;
    }

    @Override
    public String getAppName() {
        return "Сортер ИМ";
    }

    public ValuesRepository getValuesRepository() {
        return valuesRepository;
    }

    @Override
    public String getApkName() {
        return "TSDSorter.apk";
    }

    @Override
    public ViewModelProvider.Factory getWorkModelFactory() {
        return  new WorkViewModelFactory(getQueryHelper(), getValuesRepository());
    }

    // ------------------------------------------------------------------------------------------
}
