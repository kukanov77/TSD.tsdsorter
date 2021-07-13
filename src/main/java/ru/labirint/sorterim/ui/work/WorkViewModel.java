package ru.labirint.sorterim.ui.work;

import ru.labirint.core.helper.msg.MsgHelper;
import ru.labirint.sorterim.interactors.ScanActions;

import ru.labirint.sorterim.interactors.scanchains.ScanUse;
import ru.labirint.sorterim.interactors.scanchains.ScanEnum;
import ru.labirint.sorterim.entities.values.ValuesRepository;

public class WorkViewModel extends ru.labirint.core_tsd.ui.work.WorkViewModel  {

    public WorkViewModel(ValuesRepository valuesRepository, MsgHelper msg, ScanUse scanChain ) {
        super(valuesRepository, msg, scanChain);
    }

    @Override
    protected void initScanUseCase() {

    }

}
