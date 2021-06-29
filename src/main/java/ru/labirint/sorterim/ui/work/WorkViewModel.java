package ru.labirint.sorterim.ui.work;

import ru.labirint.core.interactors.msg.MsgUse;
import ru.labirint.sorterim.data.QueryHelper;
import ru.labirint.sorterim.interactors.ScanActions;

import ru.labirint.sorterim.interactors.scanchains.ScanChainUse;
import ru.labirint.sorterim.interactors.scanchains.Scankeys;
import ru.labirint.sorterim.entities.values.ValuesRepository;

public class WorkViewModel extends ru.labirint.core_tsd.ui.work.WorkViewModel  {

    public WorkViewModel(ValuesRepository valuesRepository,MsgUse msg, ScanChainUse scanChain ) {
        super(valuesRepository, msg, scanChain);
    }

    @Override
    protected void initScanUseCase() {
        ScanActions scanActions = (ScanActions) scanChain.getScanActions();
        scanActions.onAttachModel(this);
        //this.scanChain = new ScanChainUse(new Scankeys(), new ScanActions(this,));
    }

}
