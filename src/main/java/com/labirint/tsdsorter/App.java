package com.labirint.tsdsorter;

import com.labirint.tsdsorter.data.AppDatabase;
import com.labirint.tsdsorter.data.QueryHelper;
import com.labirint.tsdsorter.entities.values.ValuesRepository;

public class App extends ru.labirint.core.App  {

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

    // ------------------------------------------------------------------------------------------
}
