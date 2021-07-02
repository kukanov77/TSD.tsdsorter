package ru.labirint.sorterim.ui.work;

import ru.labirint.core.helper.msg.MsgHelper;
import ru.labirint.sorterim.interactors.ScanActions;

import ru.labirint.sorterim.interactors.scanchains.ScanChainUse;
import ru.labirint.sorterim.interactors.scanchains.Scankeys;
import ru.labirint.sorterim.entities.values.ValuesRepository;

public class WorkViewModel extends ru.labirint.core_tsd.ui.work.WorkViewModel  {

    public WorkViewModel(ValuesRepository valuesRepository, MsgHelper msg, ScanChainUse scanChain ) {
        super(valuesRepository, msg, scanChain);
    }

    @Override
    protected void initScanUseCase() {

    }

}
