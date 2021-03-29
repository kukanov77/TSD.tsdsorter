package ru.labirint.sorterim.ui.work;

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
