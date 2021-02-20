package com.labirint.tsdsorter.interactors.scankeys;

import ru.labirint.core.entities.Barcode;
import ru.labirint.core.scankeys.Scankeys;


public class ScanUseCase extends ru.labirint.core.scankeys.ScanUseCase {
    public ScanUseCase(Scankeys scankeys) {
        super(scankeys);
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
