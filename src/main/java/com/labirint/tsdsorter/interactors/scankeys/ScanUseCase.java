package com.labirint.tsdsorter.interactors.scankeys;

import com.labirint.tsdsorter.interactors.RunnableUseCase;

import ru.labirint.core.entities.Barcode;
import ru.labirint.core.scankeys.Scankeys;

import static com.labirint.tsdsorter.interactors.scankeys.Scankeys.BAGE;
import static com.labirint.tsdsorter.interactors.scankeys.Scankeys.CANCEL;
import static com.labirint.tsdsorter.interactors.scankeys.Scankeys.CMD_ARRANGE;
import static com.labirint.tsdsorter.interactors.scankeys.Scankeys.CMD_GET;
import static com.labirint.tsdsorter.interactors.scankeys.Scankeys.CMD_GET_ONE;
import static com.labirint.tsdsorter.interactors.scankeys.Scankeys.FINISH;
import static com.labirint.tsdsorter.interactors.scankeys.Scankeys.PLACE;
import static com.labirint.tsdsorter.interactors.scankeys.Scankeys.STRETCH;


public class ScanUseCase extends ru.labirint.core.scankeys.ScanUseCase {

    RunnableUseCase run;

    public ScanUseCase(Scankeys scankeys, RunnableUseCase run) {
        super(scankeys);

        this.run = run;

        // -
        put(run.getPerson, BAGE);
        // - расстановка
        put(run.cmdArrange,                 BAGE, CMD_ARRANGE);
        put(run.cmdArrangeStretch,          BAGE, CMD_ARRANGE, STRETCH);
        put(run.cmdArrangeCancel,           BAGE, CMD_ARRANGE, STRETCH, CANCEL);
        put(run.cmdArrangeStretchPlace,     BAGE, CMD_ARRANGE, STRETCH, PLACE);
        put(run.cmdArrangeStretchPlacePlace,BAGE, CMD_ARRANGE, STRETCH, PLACE, PLACE);
        // - снять
        put(run.cmdGet, BAGE, CMD_GET);
        put(run.cmdGetPlace, BAGE, CMD_GET, PLACE);
        put(run.cmdGetCancel, BAGE, CMD_GET, CANCEL);
        // - снять один
        put(run.cmdGetOne, BAGE, CMD_GET_ONE);
        put(run.cmdGetOnePlace, BAGE, CMD_GET_ONE, PLACE);

        // - finish
        put(run.finish, BAGE, CMD_ARRANGE, FINISH);
        put(run.finish, BAGE, CMD_GET, FINISH);
        put(run.finish, BAGE, CMD_GET_ONE, FINISH);

        put(run.finishFinish, BAGE, FINISH);



    }

//    @Override
//    public void addKey(Barcode barcode){
//        this.barcode = barcode;
//        // -- исключение для определенных веток когда нужно добавить лююой штрихкод
//        if (thisEquals(BAGE, CMD_ARRANGE) && !barcode.equals(CMD.FINISH)){
//            scankeyString.setValue(scankeyString.getValue() + EAN13.getKey());
//        } else {
//        scankeyString.setValue(scankeyString.getValue() + scankeys.getByBarstring(barcode.toString()).getKey());
//        }
//    }
}
