package ru.labirint.sorterim.interactors.scankeys;

import ru.labirint.sorterim.entities.CMD;
import ru.labirint.core.entities.Prefix;
import ru.labirint.core.scankeys.Scankey;


public class Scankeys extends ru.labirint.core.scankeys.Scankeys {

    //QWRYUIDHJKLZXVBNM

    public static final Scankey CMD_ARRANGE = new Scankey (CMD.ARRANGE, "A", "Расстановка");
    public static final Scankey CMD_GET = new Scankey (CMD.GET, "T", "Снятие");
    public static final Scankey CMD_GET_ONE = new Scankey (CMD.GET_ONE, "O", "Снять");
    public static final Scankey CANCEL = new Scankey (CMD.CANCEL, "C", "Отмена");
    public static final Scankey FINISH = new Scankey (CMD.FINISH, "F", "Закончить");

    public static final Scankey BAGE = new Scankey (Prefix.BADGE.toString(), "G", "Бейдж");
    public static final Scankey PLACE = new Scankey (Prefix.PLACE.toString(), "P", "Адрес");
    public static final Scankey STRETCH = new Scankey (Prefix.STRETCH.toString(), "S", "Стрейч");

    //case BOX: return "B";
    //case STRETCH: return "S";


    @Override
    public void initScankeys() {
        keys.add(CMD_ARRANGE);
        keys.add(CMD_GET);
        keys.add(CMD_GET_ONE);
        keys.add(CANCEL);
        keys.add(FINISH);
        keys.add(BAGE);
        keys.add(PLACE);
        keys.add(STRETCH);
    }
}
