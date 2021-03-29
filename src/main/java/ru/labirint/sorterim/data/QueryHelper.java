package ru.labirint.sorterim.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import io.reactivex.functions.Consumer;
import ru.labirint.core.data.Query;
import ru.labirint.core.data.QueryRepository;
import ru.labirint.core.entities.Barcode;
import ru.labirint.sorterim.entities.PersonInfo;
import ru.labirint.sorterim.entities.Place;
import ru.labirint.sorterim.entities.PlaceResponse;
import ru.labirint.sorterim.entities.TextResponse;
import ru.labirint.sorterim.entities.values.ValuesRepository;

import org.json.JSONArray;

public class QueryHelper extends ru.labirint.core_tsd.data.QueryHelper {

    ValuesRepository valuesRepository;

//    MutableLiveData<Throwable> error = new MutableLiveData<Throwable>();
//    MutableLiveData<Boolean> loadingIndicator = new MutableLiveData<Boolean>();

    // --------------------------------------------------------------------------------------


    public QueryHelper(QueryRepository queryRepository, ValuesRepository valuesRepository) {
        super(queryRepository, valuesRepository);
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
//    @Override
//    public String getSplashQuery() {
//        return "SELECT 1 id, 0 error, '\"ver\":\"'+LastVersion+'\"' [message] FROM [SQLMAIN\\SQLSVR].Books.dbo.tblToDoKLProject WHERE ID_Project = 411";
//    }

    // --------------------------------------------------------------------------------------
    private String getMulifuncStr() {
        return "EXEC[dbo].[spTSD_DynamicSorter_Multifunc] @func='%s'";
        //+
              //  ",@IdPerson =" + String.valueOf(valuesRepository.getIdPerson());
    }

    // --------------------------------------------------------------------------------------
    // --- 27 - fnGetStuffInfoForPerson - авторизация, cканируем бейдж
    public void queryPersonInfo(Consumer<JSONArray> onResponse){
        String str_query = String.format(getMulifuncStr()
                +",@IdPerson=%d"
                , "fnGetStuffInfoForPerson", valuesRepository.getIdPerson());
        queryRepository.execute(new Query("fnGetStuffInfoForPerson", str_query, JSONArray.class), onResponse, onError, loadingIndicator);
    }
    // --------------------------------------------------------------------------------------
    // --- 28 - fnTSDGetInfoForPutOnAdress - поиск места расстановки
    public void queryFnTSDGetInfoForPutOnAdress (Consumer<JSONArray> onResponse){
        String str_query = String.format(getMulifuncStr()
                        +",@ID_Sales=%d,@ID_Stretch=%d"
                , "fnTSDGetInfoForPutOnAdress", valuesRepository.getIdSales(), valuesRepository.getStretch());
        queryRepository.execute(new Query("fnTSDGetInfoForPutOnAdress", str_query, JSONArray.class, true), onResponse, onError, loadingIndicator);
    }
    // --------------------------------------------------------------------------------------
    // --- 29 - spTSDPutOnAdress - положить на адрес
    public void querySpTSDPutOnAdress(Consumer<JSONArray> onResponse){
        String str_query = String.format(getMulifuncStr()
                        +",@ID_Place=%d, @ID_Sales=%d, @IdPerson=%d, @ID_Stretch=%d"
                , "spTSDPutOnAdress",valuesRepository.getIdPlace(), valuesRepository.getIdSales(),valuesRepository.getIdPerson(),valuesRepository.getStretch());
        queryRepository.execute(new Query("spTSDPutOnAdress", str_query, JSONArray.class), onResponse, onError, loadingIndicator);
    }
    // --------------------------------------------------------------------------------------
    // --- 30 - spTSDGetPlaceToRemove - запрос места для снятия
    public void querySpTSDGetPlaceToRemove (Consumer<JSONArray> onResponse){
        String str_query = String.format(getMulifuncStr()
                , "spTSDGetPlaceToRemove");
        queryRepository.execute(new Query("spTSDGetPlaceToRemove", str_query, JSONArray.class, true), onResponse, onError, loadingIndicator);
    }
    // --------------------------------------------------------------------------------------
    // --- 31 - spTSDRemoveFromAdress - запрос снять с адреса
    public void querySpTSDRemoveFromAdress (Consumer<JSONArray> onResponse){
        String str_query = String.format(getMulifuncStr()
                        +",@ID_Place=%d,@IdPerson=%d"
                , "spTSDRemoveFromAdress", valuesRepository.getIdPlace(), valuesRepository.getIdPerson());
        queryRepository.execute(new Query("spTSDRemoveFromAdress", str_query, JSONArray.class), onResponse, onError, loadingIndicator);
    }
    // --------------------------------------------------------------------------------------
    // --- 32 - checkPlaceToRemove - проверка заполненности места
    public void queryCheckPlaceToRemove (Consumer<JSONArray> onResponse){
        String str_query = String.format(getMulifuncStr()
                        +",@ID_Place=%d"
                , "checkPlaceToRemove", valuesRepository.getIdPlace());
        queryRepository.execute(new Query("checkPlaceToRemove", str_query, JSONArray.class), onResponse, onError, loadingIndicator);
    }
    // --------------------------------------------------------------------------------------
    // -- 33 - spTSDSetDownTime - встать на простой
    public void querySpTSDSetDownTime (Consumer<JSONArray> onResponse){
        String str_query = String.format(getMulifuncStr()
                        +",@IdPerson=%d"
                , "spTSDSetDownTime", valuesRepository.getIdPerson());
        queryRepository.execute(new Query("spTSDSetDownTime", str_query, JSONArray.class), onResponse, onError, loadingIndicator);
    }

}
