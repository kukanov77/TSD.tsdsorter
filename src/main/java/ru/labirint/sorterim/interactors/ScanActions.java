package ru.labirint.sorterim.interactors;

import android.os.Handler;


import ru.labirint.core.util.messages.Beep;
import ru.labirint.core.util.messages.tsdmsg.MsgHelper;
import ru.labirint.core.util.messages.tsdmsg.StringHelper;

import ru.labirint.sorterim.R;
import ru.labirint.sorterim.data.QueryHelper;
import ru.labirint.sorterim.entities.Place;
import ru.labirint.sorterim.entities.values.ValuesRepository;
import ru.labirint.sorterim.ui.work.WorkViewModel;

import org.json.JSONObject;

import static ru.labirint.sorterim.interactors.scankeys.Scankeys.BAGE;
import static ru.labirint.sorterim.interactors.scankeys.Scankeys.CMD_ARRANGE;
import static ru.labirint.sorterim.interactors.scankeys.Scankeys.CMD_GET;
import static ru.labirint.sorterim.interactors.scankeys.Scankeys.CMD_GET_ONE;

public class ScanActions extends ru.labirint.core_tsd.interactors.ScanActions {



    WorkViewModel model;
    QueryHelper queryHelper;
    ValuesRepository valuesRepository;
    MsgHelper msg;

    // ----------------------------------------------------------------------------------------

    public ScanActions(WorkViewModel model) {
        super(model);
        this.model = model;
        this.queryHelper = (QueryHelper) model.queryHelper;
        this.valuesRepository = (ValuesRepository) model.valuesRepository;
        this.msg = model.msg;
    }

    // ----------------------------------------------------------------------------------------
    // --- debug
    public void setTestPerson() {
        valuesRepository.setIdPerson(1224);
    }

    // ----------------------------------------------------------------------------------------
    // --- сканируем бейдж
    public Runnable getPerson = ()->{

        valuesRepository.setIdPerson(valuesRepository.getBarcode().intBody());

        queryHelper.queryPersonInfo(
            jsons -> {
                if (jsons.length() > 0) {
                    JSONObject j = jsons.getJSONObject(0);
                    boolean is_error = j.getBoolean("isError");
                    if (!is_error) {
                        String name = j.getString("name");
                        valuesRepository.setPersonName(name);
                        msg.say("Сканируй команду", R.drawable.command);
                        model.title.setValue("Выбор команды");
                    } else {
                        String str_error = j.getString("txt");
                        valuesRepository.setIdPerson(-1);
                        msg.alert(str_error, "Сканируй бейдж");
                    }
                } else {
                    valuesRepository.setIdPerson(-1);
                    msg.alert("Ошибка кода сотрудника!", "Сканируй бейдж");
                }
            }
        );
    };

    // ----------------------------------------------------------------------------------------
    // --- расстановка
    public Runnable cmdArrange = () -> {
        msg.say("Сканируй стрейч", R.drawable.box);
        model.title.setValue("Расстановка");
    };
    // ----------------------------------------------------------------------------------------
    // --- расстановка - стрейч
    public Runnable cmdArrangeStretch = () -> {
        valuesRepository.setIdSales(valuesRepository.getBarcode());
        valuesRepository.setStretch(valuesRepository.getBarcode());

        if (valuesRepository.getIdSales() == -1 || valuesRepository.getStretch() == -1) {
            msg.alert("ШК не распознался!", "Сканируй стрейч");
        } else {

            queryHelper.queryFnTSDGetInfoForPutOnAdress(
                jsons -> {

                    if (jsons.length() > 0) {

                        JSONObject j = jsons.getJSONObject(0);
                        boolean is_error = j.getBoolean("isError");
                        String txt =  j.isNull("txt")?"fnTSDGetInfoForPutOnAdress\nвернул NULL":j.getString("txt");
                        String txt_place =  j.isNull("txt_place")?"fnTSDGetInfoForPutOnAdress\nвернул NULL":j.getString("txt_place");

                        if (is_error) {
                            valuesRepository.clearPlace();
                            msg.alert(txt,"Сканируй стрейч");
                        } else {
                            valuesRepository.setIdPlace(j.getInt("id_Place"));
                            valuesRepository.setPlace(txt_place);
                            msg.say(txt, R.drawable.place);
                        }

                    } else {
                        valuesRepository.setIdPlace(-1);
                        msg.alert("fnTSDGetInfoForPutOnAdress\nне вернул строку", "Сканируй стрейч");
                    }
                }
            );

        }

    };
    // ----------------------------------------------------------------------------------------
    // --- расстановка - стрейч - отмена
    public Runnable cmdArrangeCancel = () -> {
        valuesRepository.clearPlace();
        msg.say("Сканируй стрейч", R.drawable.box);
        model.title.setValue("Расстановка");
        model.scanChain.setScanKeys(BAGE, CMD_ARRANGE);
    };
    // ----------------------------------------------------------------------------------------
    // --- расстановка - стрейч - адрес
    public Runnable cmdArrangeStretchPlace = () -> {
        if (valuesRepository.getBarcode().equals(valuesRepository.getIdPlace())) {
            queryHelper.querySpTSDPutOnAdress(
                js -> {
                    queryHelper.queryCheckPlaceToRemove(
                          jsons -> {
                            valuesRepository.setIdSales(-1);
                            valuesRepository.setStretch(-1);

                            JSONObject j = jsons.getJSONObject(0);
                            int id_place = j.getInt("id_Place");
                            if (id_place > 0){

                                StringHelper txt = StringHelper.newSpannable();
                                txt.add(String.format("Уложен на адрес\n%s", valuesRepository.getPlace()));

                                StringHelper post = StringHelper.newSpannable();
                                post.red(String.format("Сними ящик\nСканируй адрес\n%s", j.getString("place")));
                                post.setBackResource(R.drawable.green);

                                msg.say(txt, post, Beep.BOX);
                                valuesRepository.setIdPlace(id_place);
                                valuesRepository.setPlace(j.getString("place"));
                                model.title.setValue("Снятие");
                                //m.backResource.set(R.drawable.place);
                            } else {
                                msg.say(String.format("Уложен на адрес\n%s", valuesRepository.getPlace()),"Сканируй стрейч", R.drawable.box);
                                valuesRepository.clearPlace();
                                model.scanChain.setScanKeys(BAGE, CMD_ARRANGE);
                            }
                          }
                    );

                }
            );
        } else {
            msg.alert("Неверный адрес!", String.format("Сканируй адрес\n%s", valuesRepository.getPlace()) );
        }

    };
    // ----------------------------------------------------------------------------------------
    // --- расстановка - стрейч - адрес - снять с места
    public Runnable cmdArrangeStretchPlacePlace = () -> {

        if ( valuesRepository.getBarcode().equals(valuesRepository.getIdPlace())) {
            queryHelper.querySpTSDRemoveFromAdress(
                    jsons->{
                        if (jsons.length() > 0){
                            JSONObject j = jsons.getJSONObject(0);
                            int is_error = j.getInt("1");
                            if (is_error == 0) {
                                valuesRepository.clearPlace();
                                msg.say("OK","Сканируй стрейч", R.drawable.box);
                                model.scanChain.setScanKeys(BAGE, CMD_ARRANGE);
                            } else {
                                msg.alert("Ошибка снятия\nПопробуйте еще раз!", String.format("Сканируй адрес\n%s", valuesRepository.getPlace()));
                            }
                        } else {
                            msg.alert("Ошибка снятия\nПопробуйте еще раз!", String.format("Сканируй адрес\n%s", valuesRepository.getPlace()));
                        }
                    }
            );
        } else {
            msg.alert("Неверный адрес!", String.format("Сканируй адрес\n%s", valuesRepository.getPlace()) );
        }
    };
    // ----------------------------------------------------------------------------------------
    // --- снятие
    public Runnable cmdGet = () -> {
        queryHelper.querySpTSDGetPlaceToRemove(
            jsons -> {
                if (jsons.length() > 0) {
                    JSONObject j = jsons.getJSONObject(0);
                    Place place = new Place(j);
                    valuesRepository.setPlace(place);
                    model.title.setValue("Снятие");
                    msg.say(String.format("Сканируй адрес\n%s", place.getPlace()), R.drawable.place );
                } else {
                    msg.alert("Нет мест к снятию!", "Сканируй команду");
                    model.title.setValue("Выбор команды");
                    model.backResource.set(R.drawable.command);
                }
            }
        );
    };
    // ----------------------------------------------------------------------------------------
    // --- снятие - адрес
    public Runnable cmdGetPlace = () -> {

        if (valuesRepository.getBarcode().equals(valuesRepository.getIdPlace())) {
            queryHelper.querySpTSDRemoveFromAdress(
                jsons->{
                    if (jsons.length() > 0){
                        JSONObject j = jsons.getJSONObject(0);
                        int is_error = j.getInt("1");
                        if (is_error == 0) {
                            valuesRepository.clearPlace();
                            model.scanChain.setScanKeys(BAGE, CMD_GET);
                            msg.say("OK");
                            new Handler().postDelayed(() -> cmdGet.run(), 100);
                        } else {
                            msg.alert("Ошибка снятия\nПопробуйте еще раз!", String.format("Сканируй адрес\n%s", valuesRepository.getPlace()));
                        }
                    } else {
                        msg.alert("Ошибка снятия\nПопробуйте еще раз!", String.format("Сканируй адрес\n%s", valuesRepository.getPlace()));
                    }
                }
            );
        } else {
            msg.alert("Неверный адрес!", String.format("Сканируй адрес\n%s", valuesRepository.getPlace()) );
        }
    };
    // ----------------------------------------------------------------------------------------
    // --- снятие - отмена
    public Runnable cmdGetCancel = () -> {
        valuesRepository.clearPlace();
        model.scanChain.setScanKeys(BAGE, CMD_GET);
        cmdGet.run();
    };
    // ----------------------------------------------------------------------------------------
    // --- снять один
    public Runnable cmdGetOne = () -> {
        msg.say("Сканируй адрес", R.drawable.place);
        model.title.setValue("Снять один");
    };
    // ----------------------------------------------------------------------------------------
    // --- снять один - адрес
    public Runnable cmdGetOnePlace = () -> {
        valuesRepository.setIdPlace(valuesRepository.getBarcode().intBody());
        queryHelper.querySpTSDRemoveFromAdress(
            jsons->{
                if (jsons.length() > 0){
                    JSONObject j = jsons.getJSONObject(0);
                    int is_error = j.getInt("1");
                    if (is_error == 0) {
                        model.scanChain.setScanKeys(BAGE, CMD_GET_ONE);
                        msg.say("OK!\nЯщик снят", "Сканируй адрес");
                    } else {
                        msg.alert("Ошибка снятия\nПопробуйте еще раз!", "Сканируй адрес");
                    }
                } else {
                    msg.alert("Ошибка снятия\nПопробуйте еще раз!", "Сканируй адрес");
                }
            }
        );
    };
    // ----------------------------------------------------------------------------------------
    // --- закончить
    public Runnable finish = () -> {
        msg.say("Сканируй команду", R.drawable.command);
        model.title.setValue("Выбор команды");
        model.scanChain.setScanKeys(BAGE);
    };

    public Runnable finishFinish = () -> {
        valuesRepository.setIdPerson(-1);
        valuesRepository.setPersonName("");
        msg.say("Сканируй бейдж", R.drawable.bage);
        model.title.setValue("Авторизация");
        model.scanChain.clearScanKey();
    };

    // ----------------------------------------------------------------------------------------
    // --- встать на простой
    public Runnable cmdDownTime = () -> {
        queryHelper.querySpTSDSetDownTime(
                jsons -> {
                    StringHelper last_say = msg.getLastSay();
                    msg.say(String.format("Простой отмечен\nдля снятия\n%s", last_say.get().toString()), R.drawable.smoke);
                    model.scanChain.backScanKey();
                }
        );

    };


}
