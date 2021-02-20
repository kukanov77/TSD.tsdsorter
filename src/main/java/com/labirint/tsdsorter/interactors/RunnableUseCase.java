package com.labirint.tsdsorter.interactors;

import androidx.appcompat.app.AppCompatActivity;

import ru.labirint.core.entities.Prefix;

import com.labirint.tsdsorter.R;
import com.labirint.tsdsorter.ui.work.WorkViewModel;

import org.json.JSONObject;

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

        m.queryHelper.queryPersonInfo(m.barcode.intBody(),
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

}
