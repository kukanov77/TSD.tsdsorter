package ru.labirint.sorterim.ui.work;

import ru.labirint.sorterim.data.QueryHelper;
import ru.labirint.sorterim.interactors.ScanActions;

import ru.labirint.sorterim.interactors.scankeys.ScanChainUse;
import ru.labirint.sorterim.interactors.scankeys.Scankeys;
import ru.labirint.sorterim.entities.values.ValuesRepository;

public class WorkViewModel extends ru.labirint.core_tsd.ui.work.WorkViewModel  {

    public WorkViewModel(QueryHelper queryHelper, ValuesRepository valuesRepository) {
        super(queryHelper, valuesRepository);
    }

    @Override
    protected void initScanUseCase() {
        this.scanChain = new ScanChainUse(new Scankeys(), new ScanActions(this));
    }

}
