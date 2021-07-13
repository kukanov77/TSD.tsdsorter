package ru.labirint.sorterim;

import androidx.lifecycle.ViewModelProvider;

import ru.labirint.sorterim.data.AppDatabase;
import ru.labirint.sorterim.data.QueryHelper;
import ru.labirint.sorterim.entities.values.ValuesRepository;
import ru.labirint.sorterim.interactors.ScanActions;
import ru.labirint.sorterim.interactors.scanchains.ScanUse;
import ru.labirint.sorterim.interactors.scanchains.ScanEnum;
import ru.labirint.sorterim.ui.work.WorkViewModelFactory;

public class App extends ru.labirint.core_tsd.App  {

    //QueryHelper queryHelper;
    ValuesRepository valuesRepository;


    // ------------------------------------------------------------------------------------------

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public void initScanChain() {

    }

    @Override
    protected void initQueryHelper() {
        valuesRepository = new ValuesRepository(getAppDatabase().getValuesDao());
        queryHelper = new QueryHelper(queryRepository, valuesRepository);
        scanChain = new ScanUse(new ScanEnum(), new ScanActions((QueryHelper) queryHelper, valuesRepository, msg));
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
        return "(1723) Сортер ИМ";
    }

    public ValuesRepository getValuesRepository() {
        return valuesRepository;
    }

    @Override
    public String getApkName() {
        return "SorterIM.apk";
    }

    @Override
    public ViewModelProvider.Factory getWorkModelFactory() {
        return  new WorkViewModelFactory(getQueryHelper(), getValuesRepository(), msg, (ScanUse) scanChain);
    }

    // ------------------------------------------------------------------------------------------
}
