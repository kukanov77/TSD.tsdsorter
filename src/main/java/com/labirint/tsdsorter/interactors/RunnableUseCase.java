package com.labirint.tsdsorter.interactors;

import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import ru.labirint.core.entities.Prefix;
import ru.labirint.core.util.messages.Beep;
import ru.labirint.core.util.messages.Msg;
import ru.labirint.core.util.messages.tsdmsg.StringHelper;

import com.labirint.tsdsorter.R;
import com.labirint.tsdsorter.entities.Place;
import com.labirint.tsdsorter.ui.work.WorkViewModel;

import org.json.JSONObject;

import static com.labirint.tsdsorter.interactors.scankeys.Scankeys.BAGE;
import static com.labirint.tsdsorter.interactors.scankeys.Scankeys.CMD_ARRANGE;
import static com.labirint.tsdsorter.interactors.scankeys.Scankeys.CMD_GET;
import static com.labirint.tsdsorter.interactors.scankeys.Scankeys.CMD_GET_ONE;

public class RunnableUseCase {



    WorkViewModel m;

    // ----------------------------------------------------------------------------------------

    public RunnableUseCase(WorkViewModel model) {
        this.m = model;
    }

    // ----------------------------------------------------------------------------------------
    // --- сканируем бейдж
    public Runnable getPerson = ()->{

        m.valuesRepository.setIdPerson(m.barcode.intBody());

        m.queryHelper.queryPersonInfo(
            jsons -> {
                if (jsons.length() > 0) {
                    JSONObject j = jsons.getJSONObject(0);
                    boolean is_error = j.getBoolean("isError");
                    if (!is_error) {
                        String name = j.getString("name");
                        m.valuesRepository.setPersonName(name);
                        m.msg.say("Сканируй команду");
                        m.title.setValue("Выбор команды");
                        m.backResource.set(R.drawable.command);
                    } else {
                        String str_error = j.getString("txt");
                        m.valuesRepository.setIdPerson(-1);
                        m.msg.alert(str_error, "Сканируй бейдж");
                    }
                } else {
                    m.valuesRepository.setIdPerson(-1);
                    m.msg.alert("Ошибка кода сотрудника!", "Сканируй бейдж");
                }
            }
        );
    };

    // ----------------------------------------------------------------------------------------
    // --- расстановка
    public Runnable cmdArrange = () -> {
        m.msg.say("Сканируй стрейч");
        m.title.setValue("Расстановка");
        m.backResource.set(R.drawable.box);
    };
    // ----------------------------------------------------------------------------------------
    // --- расстановка - стрейч
    public Runnable cmdArrangeStretch = () -> {
        m.valuesRepository.setIdSales(m.barcode);
        m.valuesRepository.setStretch(m.barcode);

        if (m.valuesRepository.getIdSales() == -1 || m.valuesRepository.getStretch() == -1) {
            m.msg.alert("ШК не распознался!", "Сканируй стрейч");
        } else {

            m.queryHelper.queryFnTSDGetInfoForPutOnAdress(
                jsons -> {

                    if (jsons.length() > 0) {

                        JSONObject j = jsons.getJSONObject(0);
                        boolean is_error = j.getBoolean("isError");
                        String txt =  j.isNull("txt")?"fnTSDGetInfoForPutOnAdress\nвернул NULL":j.getString("txt");
                        String txt_place =  j.isNull("txt_place")?"fnTSDGetInfoForPutOnAdress\nвернул NULL":j.getString("txt_place");

                        if (is_error) {
                            m.valuesRepository.clearPlace();
                            m.msg.alert(txt,"Сканируй стрейч");
                        } else {
                            m.valuesRepository.setIdPlace(j.getInt("id_Place"));
                            m.valuesRepository.setPlace(txt_place);
                            m.msg.say(txt);
                            m.backResource.set(R.drawable.place);
                        }

                    } else {
                        m.valuesRepository.setIdPlace(-1);
                        m.msg.alert("fnTSDGetInfoForPutOnAdress\nне вернул строку", "Сканируй стрейч");
                    }
                }
            );

        }

    };
    // ----------------------------------------------------------------------------------------
    // --- расстановка - стрейч - отмена
    public Runnable cmdArrangeCancel = () -> {
        m.valuesRepository.clearPlace();
        m.msg.say("Сканируй стрейч");
        m.title.setValue("Расстановка");
        m.backResource.set(R.drawable.box);
        m.scanUseCase.setScanKeys(BAGE, CMD_ARRANGE);
    };
    // ----------------------------------------------------------------------------------------
    // --- расстановка - стрейч - адрес
    public Runnable cmdArrangeStretchPlace = () -> {

        if (m.barcode.intBody() == m.valuesRepository.getIdPlace()) {

            m.queryHelper.querySpTSDPutOnAdress(
                js -> {

                    m.queryHelper.queryCheckPlaceToRemove(m.valuesRepository.getIdPlace(),
                      jsons -> {
                        m.valuesRepository.setIdSales(-1);
                        m.valuesRepository.setStretch(-1);

                        JSONObject j = jsons.getJSONObject(0);
                        int id_place = j.getInt("id_Place");
                        if (id_place > 0){

                            StringHelper txt = StringHelper.newSpannable();
                            txt.add(String.format("Уложен на адрес\n%s", m.valuesRepository.getPlace()));

                            StringHelper post = StringHelper.newSpannable();
                            post.red(String.format("Сними ящик\nСканируй адрес\n%s", j.getString("place")));
                            post.setBackResource(R.drawable.green);

                            m.msg.say(txt, post, Beep.BOX);
                            m.valuesRepository.setIdPlace(id_place);
                            m.valuesRepository.setPlace(j.getString("place"));
                            m.title.setValue("Снятие");
                            //m.backResource.set(R.drawable.place);
                        } else {
                            m.msg.say(String.format("Уложен на адрес\n%s", m.valuesRepository.getPlace()),"Сканируй стрейч");
                            m.valuesRepository.clearPlace();
                            m.backResource.set(R.drawable.box);
                            m.scanUseCase.setScanKeys(BAGE, CMD_ARRANGE);
                        }
                      }
                    );

                }
            );

        } else {
            m.msg.alert("Неверный адрес!", String.format("Сканируй адрес\n%s", m.valuesRepository.getPlace()) );
        }

    };
    // ----------------------------------------------------------------------------------------
    // --- расстановка - стрейч - адрес - снять с места
    public Runnable cmdArrangeStretchPlacePlace = () -> {
        int id_place = m.valuesRepository.getIdPlace();
        if ( m.barcode.intBody() == id_place) {
            m.queryHelper.querySpTSDRemoveFromAdress(id_place, m.valuesRepository.getIdPerson(),
                    jsons->{
                        if (jsons.length() > 0){
                            JSONObject j = jsons.getJSONObject(0);
                            int is_error = j.getInt("1");
                            if (is_error == 0) {
                                m.valuesRepository.clearPlace();
                                m.msg.say("OK","Сканируй стрейч");
                                m.backResource.set(R.drawable.box);
                                m.scanUseCase.setScanKeys(BAGE, CMD_ARRANGE);
                            } else {
                                m.msg.alert("Ошибка снятия\nПопробуйте еще раз!", String.format("Сканируй адрес\n%s", m.valuesRepository.getPlace()));
                            }
                        } else {
                            m.msg.alert("Ошибка снятия\nПопробуйте еще раз!", String.format("Сканируй адрес\n%s", m.valuesRepository.getPlace()));
                        }
                    }
            );
        } else {
            m.msg.alert("Неверный адрес!", String.format("Сканируй адрес\n%s", m.valuesRepository.getPlace()) );
        }
    };
    // ----------------------------------------------------------------------------------------
    // --- снятие
    public Runnable cmdGet = () -> {
        m.queryHelper.querySpTSDGetPlaceToRemove(
            jsons -> {
                if (jsons.length() > 0) {
                    JSONObject j = jsons.getJSONObject(0);
                    Place place = new Place(j);
                    m.valuesRepository.setPlace(place);
                    m.title.setValue("Снятие");
                    m.backResource.set(R.drawable.place);
                    m.msg.say(String.format("Сканируй адрес\n%s", place.getPlace()) );

                } else {
                    m.msg.alert("Нет мест к снятию!", "Сканируй команду");
                    m.title.setValue("Выбор команды");
                    m.backResource.set(R.drawable.command);
                }
            }
        );
    };
    // ----------------------------------------------------------------------------------------
    // --- снятие - адрес
    public Runnable cmdGetPlace = () -> {
        int id_place = m.valuesRepository.getIdPlace();
        if (m.barcode.intBody() == id_place) {
            m.queryHelper.querySpTSDRemoveFromAdress(id_place, m.valuesRepository.getIdPerson(),
                jsons->{
                    if (jsons.length() > 0){
                        JSONObject j = jsons.getJSONObject(0);
                        int is_error = j.getInt("1");
                        if (is_error == 0) {
                            m.valuesRepository.clearPlace();
                            m.scanUseCase.setScanKeys(BAGE, CMD_GET);
                            m.msg.say("OK");
                            new Handler().postDelayed(() -> cmdGet.run(), 100);
                        } else {
                            m.msg.alert("Ошибка снятия\nПопробуйте еще раз!", String.format("Сканируй адрес\n%s", m.valuesRepository.getPlace()));
                        }
                    } else {
                        m.msg.alert("Ошибка снятия\nПопробуйте еще раз!", String.format("Сканируй адрес\n%s", m.valuesRepository.getPlace()));
                    }
                }
            );
        } else {
            m.msg.alert("Неверный адрес!", String.format("Сканируй адрес\n%s", m.valuesRepository.getPlace()) );
        }
    };
    // ----------------------------------------------------------------------------------------
    // --- снятие - отмена
    public Runnable cmdGetCancel = () -> {
        m.valuesRepository.clearPlace();
        m.scanUseCase.setScanKeys(BAGE, CMD_GET);
        cmdGet.run();

    };
    // ----------------------------------------------------------------------------------------
    // --- снять один
    public Runnable cmdGetOne = () -> {
        m.msg.say("Сканируй адрес");
        m.title.setValue("Снять один");
        m.backResource.set(R.drawable.place);
    };
    // ----------------------------------------------------------------------------------------
    // --- снять один - адрес
    public Runnable cmdGetOnePlace = () -> {
        m.queryHelper.querySpTSDRemoveFromAdress(m.barcode.intBody(), m.valuesRepository.getIdPerson(),
            jsons->{
                if (jsons.length() > 0){
                    JSONObject j = jsons.getJSONObject(0);
                    int is_error = j.getInt("1");
                    if (is_error == 0) {
                        m.scanUseCase.setScanKeys(BAGE, CMD_GET_ONE);
                        m.msg.say("OK!\nЯщик снят", "Сканируй адрес");
                    } else {
                        m.msg.alert("Ошибка снятия\nПопробуйте еще раз!", "Сканируй адрес");
                    }
                } else {
                    m.msg.alert("Ошибка снятия\nПопробуйте еще раз!", "Сканируй адрес");
                }
            }
        );
    };
    // ----------------------------------------------------------------------------------------
    // --- закончить
    public Runnable finish = () -> {
        m.msg.say("Сканируй команду");
        m.title.setValue("Выбор команды");
        m.backResource.set(R.drawable.command);
        m.scanUseCase.setScanKeys(BAGE);

    };

    public Runnable finishFinish = () -> {
        m.valuesRepository.setIdPerson(-1);
        m.valuesRepository.setPersonName("");
        m.msg.say("Сканируй бейдж");
        m.title.setValue("Авторизация");
        m.backResource.set(R.drawable.bage);
        m.scanUseCase.clearScanKey();
    };

    // ----------------------------------------------------------------------------------------
    // --- с нять с адреса


}
