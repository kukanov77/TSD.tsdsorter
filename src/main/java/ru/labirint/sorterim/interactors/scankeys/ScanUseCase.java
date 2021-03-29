package ru.labirint.sorterim.interactors.scankeys;

import ru.labirint.sorterim.interactors.RunnableUseCase;

import ru.labirint.core.entities.Barcode;
import ru.labirint.core.scankeys.Scankeys;

import static ru.labirint.sorterim.interactors.scankeys.Scankeys.BAGE;
import static ru.labirint.sorterim.interactors.scankeys.Scankeys.CANCEL;
import static ru.labirint.sorterim.interactors.scankeys.Scankeys.CMD_ARRANGE;
import static ru.labirint.sorterim.interactors.scankeys.Scankeys.CMD_DOWN_TIME;
import static ru.labirint.sorterim.interactors.scankeys.Scankeys.CMD_GET;
import static ru.labirint.sorterim.interactors.scankeys.Scankeys.CMD_GET_ONE;
import static ru.labirint.sorterim.interactors.scankeys.Scankeys.FINISH;
import static ru.labirint.sorterim.interactors.scankeys.Scankeys.PLACE;
import static ru.labirint.sorterim.interactors.scankeys.Scankeys.STRETCH;


public class ScanUseCase extends ru.labirint.core_tsd.interactors.scankeys.ScanUseCase {

    RunnableUseCase run;

    public ScanUseCase(Scankeys scankeys, RunnableUseCase run) {
        super(scankeys, run);

        this.run = run;

        // -
        put(run.getPerson, BAGE);
        // - расстановка
        put(run.cmdArrange,                 BAGE, CMD_ARRANGE);
        put(run.cmdDownTime,                 BAGE, CMD_ARRANGE, CMD_DOWN_TIME);
        put(run.cmdArrangeStretch,          BAGE, CMD_ARRANGE, STRETCH);
        put(run.cmdArrangeCancel,           BAGE, CMD_ARRANGE, STRETCH, CANCEL);
        put(run.cmdArrangeStretchPlace,     BAGE, CMD_ARRANGE, STRETCH, PLACE);
        put(run.cmdArrangeStretchPlacePlace,BAGE, CMD_ARRANGE, STRETCH, PLACE, PLACE);
        // - снять
        put(run.cmdGet, BAGE, CMD_GET);
        put(run.cmdDownTime, BAGE, CMD_GET, CMD_DOWN_TIME);
        put(run.cmdGetPlace, BAGE, CMD_GET, PLACE);
        put(run.cmdGetCancel, BAGE, CMD_GET, CANCEL);
        // - снять один
        put(run.cmdGetOne, BAGE, CMD_GET_ONE);
        put(run.cmdDownTime, BAGE, CMD_GET_ONE, CMD_DOWN_TIME);
        put(run.cmdGetOnePlace, BAGE, CMD_GET_ONE, PLACE);

        // - finish
        put(run.finish, BAGE, CMD_ARRANGE, FINISH);
        put(run.finish, BAGE, CMD_GET, FINISH);
        put(run.finish, BAGE, CMD_GET_ONE, FINISH);

        put(run.finishFinish, BAGE, FINISH);


        //todo debug
        try {
            run.setTestPerson();
            run.cmdGet.run();
            Thread.sleep(2000);
            run.cmdDownTime.run();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


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
