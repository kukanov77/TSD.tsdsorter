package com.labirint.tsdsorter.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import io.reactivex.functions.Consumer;
import ru.labirint.core.data.Query;
import ru.labirint.core.data.QueryRepository;
import ru.labirint.core.entities.Barcode;
import com.labirint.tsdsorter.entities.PersonInfo;
import com.labirint.tsdsorter.entities.PlaceResponse;
import com.labirint.tsdsorter.entities.TextResponse;
import com.labirint.tsdsorter.entities.values.ValuesRepository;

import org.json.JSONArray;

public class QueryHelper extends ru.labirint.core.data.QueryHelper {

    ValuesRepository valuesRepository;

//    MutableLiveData<Throwable> error = new MutableLiveData<Throwable>();
//    MutableLiveData<Boolean> loadingIndicator = new MutableLiveData<Boolean>();

    // --------------------------------------------------------------------------------------


    public QueryHelper(QueryRepository queryRepository, ValuesRepository valuesRepository) {
        super(queryRepository);
        this.valuesRepository = valuesRepository;
    }

    // --------------------------------------------------------------------------------------
//    public LiveData<Throwable> onError(){
//        return  error;
//    }
//
//    Consumer<Throwable> onError = e -> {
//        if (loadingIndicator != null) {
//            error.postValue(e);
//        }
//    };
    @Override
    public String getSplashQuery() {
        return "SELECT 1 id, 0 error, '\"ver\":\"'+LastVersion+'\"' [message] FROM [SQLMAIN\\SQLSVR].Books.dbo.tblToDoKLProject WHERE ID_Project = 411";
    }

    // --------------------------------------------------------------------------------------
    private String getMulifuncStr() {
        return "EXEC[dbo].[spTSD_DynamicSorter_Multifunc] @func='%s'";
        //+
              //  ",@IdPerson =" + String.valueOf(valuesRepository.getIdPerson());
    }

    // --------------------------------------------------------------------------------------
    // --- 27 - fnGetStuffInfoForPerson - авторизация, cканируем бейдж
    public void queryPersonInfo(int id_person, Consumer<JSONArray> onResponse){
        String str_query = String.format(getMulifuncStr()
                +",@IdPerson=%d"
                , "fnGetStuffInfoForPerson", id_person);
        queryRepository.execute(new Query(str_query, JSONArray.class), onResponse, onError, loadingIndicator);
    }
    // --------------------------------------------------------------------------------------
    // --- 28 - fnTSDGetInfoForPutOnAdress - поиск места расстановки
    public void queryFnTSDGetInfoForPutOnAdress (int id_sales, int id_stetch, Consumer<JSONArray> onResponse){
        String str_query = String.format(getMulifuncStr()
                        +",@ID_Sales=%d,@ID_Stretch=%d"
                , "fnTSDGetInfoForPutOnAdress", id_sales, id_stetch);
        queryRepository.execute(new Query(str_query, JSONArray.class), onResponse, onError, loadingIndicator);
    }
    // --------------------------------------------------------------------------------------
    // --- 29 - spTSDPutOnAdress - положить на адрес
    public void querySpTSDPutOnAdress (int id_place, int id_sales, int id_person, int id_stretch, Consumer<JSONArray> onResponse){
        String str_query = String.format(getMulifuncStr()
                        +",@ID_Place=%d, @ID_Sales=%d, @IdPerson=%d, @ID_Stretch=%d"
                , "spTSDPutOnAdress",id_place,id_sales,id_person,id_stretch);
        queryRepository.execute(new Query(str_query, JSONArray.class), onResponse, onError, loadingIndicator);
    }
    // --------------------------------------------------------------------------------------
    // --- 30 - spTSDGetPlaceToRemove - запрос места для снятия
    public void querySpTSDGetPlaceToRemove (Consumer<JSONArray> onResponse){
        String str_query = String.format(getMulifuncStr()
                , "spTSDGetPlaceToRemove");
        queryRepository.execute(new Query(str_query, JSONArray.class), onResponse, onError, loadingIndicator);
    }
    // --------------------------------------------------------------------------------------
    // --- 31 - spTSDRemoveFromAdress - запрос снять с адреса
    public void querySpTSDRemoveFromAdress (int id_place, int id_person, Consumer<JSONArray> onResponse){
        String str_query = String.format(getMulifuncStr()
                        +",@ID_Place=%d,@IdPerson=%d"
                , "spTSDRemoveFromAdress", id_place, id_person);
        queryRepository.execute(new Query(str_query, JSONArray.class), onResponse, onError, loadingIndicator);
    }



}
