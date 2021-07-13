package ru.labirint.sorterim.interactors.scanchains;

import ru.labirint.sorterim.entities.CMD;

import ru.labirint.core.entities.Prefix;
import ru.labirint.core.scanchains.Scan;


public class ScanEnum extends ru.labirint.core.scanchains.ScanEnum {

    //QWRYUIHJKLZXVBNM

    public static final Scan CMD_ARRANGE = new Scan (CMD.ARRANGE, "A", "Расстановка");
    public static final Scan CMD_GET = new Scan (CMD.GET, "T", "Снятие");
    public static final Scan CMD_GET_ONE = new Scan (CMD.GET_ONE, "O", "Снять");
    public static final Scan CANCEL = new Scan (CMD.CANCEL, "C", "Отмена");
    public static final Scan FINISH = new Scan (CMD.FINISH, "F", "Закончить");
    public static final Scan CMD_DOWN_TIME = new Scan (CMD.DOWN_TIME, "D", "Простой");
    public static final Scan BAGE = new Scan (Prefix.BAGE.toString(), "G", "Бейдж");
    public static final Scan PLACE = new Scan (Prefix.PLACE.toString(), "P", "Адрес");
    public static final Scan STRETCH = new Scan (Prefix.STRETCH.toString(), "S", "Стрейч");

    //case BOX: return "B";
    //case STRETCH: return "S";


    @Override
    public void initScankeys() {
        scanList.add(CMD_ARRANGE);
        scanList.add(CMD_GET);
        scanList.add(CMD_GET_ONE);
        scanList.add(CANCEL);
        scanList.add(FINISH);
        scanList.add(BAGE);
        scanList.add(PLACE);
        scanList.add(STRETCH);
        scanList.add(CMD_DOWN_TIME);
    }
}
