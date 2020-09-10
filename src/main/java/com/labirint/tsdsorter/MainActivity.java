package com.labirint.tsdsorter;

import android.content.Context;
import android.content.SharedPreferences;

import android.os.Build;
import android.os.Bundle;

import android.os.Handler;
import android.preference.PreferenceManager;

import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.labirint.dataaccess.BarCode;
import com.labirint.dataaccess.ExitActivity;
import com.labirint.dataaccess.Query;
import com.labirint.dataaccess.Scanner;
import com.labirint.dataaccess.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends com.labirint.dataaccess.MainActivity implements Scanner.ScannerListener {

    //----------------------------------------------------

    LinearLayout mainLayout;
    TextView textView;
    TextView textManager;
    TextView statusText;
    //EditText editText;
    Scanner tsd;
    Msg msg;
    GlobVars glob_var;
    private Handler handler = new Handler();

//    private AidcManager aidcManager;
//    private BarcodeReader barcodeReader;

    //----------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run);
   }

    // ------------------------------------------------------------------------------------------
    @Override
    protected void StartApp(String json_str) {
        setContentView(R.layout.activity_run);

        glob_var = (GlobVars) getApplicationContext();
        glob_var.setConnectionPreferences(this, getConnectionType());
        glob_var.setContext(this);

        mainLayout = findViewById(R.id.mainLayoutCommand);
        textView = findViewById(R.id.textViewCommand);
        textManager = findViewById(R.id.textManagerCommand);
        textManager.setText("");
        statusText = findViewById(R.id.statusText);

        tsd = Scanner.newInstance(this, this, Build.MODEL);
        msg = new Msg(textView, mainLayout);

        msg.Say(Prefix.BADGE);

    }

    @Override
    protected void InitActivity() {

    }

    // -------------------------------------------------------------------------------------------



    // -------------------------------------------------------------------------------------------





    private void Go(final BarCode barcode)  {


        msg.Cancel();
        handler.removeCallbacksAndMessages(null);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                statusText.setText(barcode.getBarcode());
            }
        });



//        glob_var.id_sales = barcode.getSales();
//        glob_var.i_stretch = barcode.getStretchNum();

        //-------
        //проверка на ожидаемый тип
        //
        if (msg.NotExpect(barcode)){ tsd.go(); return; }
        //if (barcode.getType() == BarCode.TYPE_STRETCH) {barcode = new BarCode("9500000000001");}



        switch (barcode.getPrefix())
        {
            case BADGE:

                    getPerson(barcode);

                break;

            case COMMAND:

                if (GlobVars.isCommandClose(barcode)) {
                    glob_var.setCommand(GlobVars.COMMAND_NO);

                    msg.Say(Prefix.COMMAND);
                    this.setTitle("Команда");
                    tsd.go();

                } else {

                switch (glob_var.setCommand(barcode)) {

                    case GlobVars.COMMAND_ARRANGE:
                        this.setTitle("Расстановка");
                        msg.Say(Prefix.STRETCH);
                        tsd.go();
                        break;

                    case GlobVars.COMMAND_REMOVE:
                        this.setTitle("Снятие");
                        //getListStretchToRemove();
                        getPlaceToRemove();
                        break;

                    case GlobVars.COMMAND_REMOVE_ONE:
                        this.setTitle("Снять один");
                        msg.Say(Prefix.PLACE);
                        tsd.go();
                        break;

                    case GlobVars.COMMAND_CANCEL:

                        switch (glob_var.getLastCommand()) {
                            //если отмена была во время расстановки
                            // то снова сканируем стрейч
                            case GlobVars.COMMAND_ARRANGE:


                                glob_var.setCommand(GlobVars.COMMAND_ARRANGE);
                                glob_var.setIdPlace(0);
                                msg.Say(Prefix.STRETCH);
                                tsd.go();


                                break;

                            // ппри снятии (не суть списком или одиночное снятие)
                            // то идем к выбору команды
                            default:

                                glob_var.setCommand(GlobVars.COMMAND_NO);
                                msg.Say(Prefix.COMMAND);
                                this.setTitle("Команда");
                                tsd.go();

                                break;
                        }

                        break;


                    default:

                        msg.Alert("Незнакомая команда!", Prefix.COMMAND);
                        tsd.go();

                        break;
                }

            }

                break;

            case STRETCH:


                    if (glob_var.getCommand() == GlobVars.COMMAND_ARRANGE)
                    {
                        findPlace(barcode);
                    }
                    else {
                        tsd.go();
                    }

                break;


            case PLACE:

                switch (glob_var.getCommand())
                {
                    case GlobVars.COMMAND_ARRANGE:

                        if (barcode.intBody() == glob_var.getIdPlace())
                        {
                            putOnAdres();
                        }
                        else
                        {
                            msg.Alert();
                            tsd.go();
                        }

                        break;

                    case GlobVars.COMMAND_REMOVE:

                        int id_place = glob_var.getPlace().getIdPlace(); //s.get(glob_var.indx_places).getIdPlace();

                        if (barcode.intBody() == id_place)
                        {
                            removeFromAddres(id_place);
                        }
                        else
                        {
                            msg.Alert();
                            tsd.go();
                        }

                        break;


                    case GlobVars.COMMAND_REMOVE_ONE:

                        removeFromAddres(barcode.intBody());


                    default:
                        tsd.go();
                }


                break;

            default:
                tsd.go();


        }



    }


    // ------------------------------------------------------------------------------------------
    // region --- РАССТАНОВКА

    // ------------------------------------------------------------------------------------------
    // --- запрос поиска места расстановки

    private void findPlace(BarCode barcode) {

        //to do

        //barcode = new BarCode("87");//168421830");

        //

        glob_var.setIdSales(barcode.getSales());
        glob_var.setStretch(barcode.getStretchNum());

        if (glob_var.getIdSales() == -1 || glob_var.getStretch() == -1)
        {
            msg.Alert("ШК не распознается!", Prefix.STRETCH);
            tsd.go();
        }
        else
        {
            //to do
//        glob_var.id_sales = 16842183;
//        glob_var.i_stretch = 2;
            //

            String str_query = String.format("SELECT * FROM [Sklad].[dbo].[fnTSDGetInfoForPutOnAdress](%d,%d)", glob_var.getIdSales(), glob_var.getStretch());
            glob_var.queryExecute(str_query, getInfoForPutOnAdressListener);


        }


    }

    // ------------------------------------------------------------------------------------------
    // --- отлик поиска места расстановки

    Query.QueryListener getInfoForPutOnAdressListener = new Query.QueryListener() {
        @Override
        public void onResult(JSONArray jsons) throws JSONException {

            if (msg.queryResOk(jsons))  {

                if (jsons.length() > 0) {

                    JSONObject j = jsons.getJSONObject(0);

                    boolean is_error = j.getBoolean("isError");
                    String txt =  j.isNull("txt")?"fnTSDGetInfoForPutOnAdress\\вернул NULL":j.getString("txt");

                    final String finalTxt = txt;

                    if (!is_error)
                    {
                        glob_var.setIdPlace(j.getInt("id_Place"));

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //msg.Say(Msg.SCAN_PLACE);
                                msg.Say(finalTxt, Prefix.PLACE);
                            }
                        });
                    }
                    else
                    {

                        glob_var.setIdPlace(-1);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                msg.Alert(finalTxt, Prefix.STRETCH);
                             //   msg.Alert();
                            }
                        });

                    }

                }
                else
                {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            glob_var.setIdPlace(-1);
                            msg.Alert("fnTSDGetInfoForPutOnAdress\\не вернул строку", Prefix.STRETCH);
                        }
                    });
                }

                tsd.go();


            } else {
                tsd.go();
            }
        }
    };

    // ------------------------------------------------------------------------------------------
    // --- запрос положить на адрес

    private void putOnAdres() {

        String str_query = String.format("EXEC Sklad.dbo.spTSDPutOnAdress %d, %d, %d, %d", glob_var.getIdPlace(), glob_var.getIdSales(), glob_var.getIdPerson(), glob_var.getStretch());

        glob_var.queryExecuteOnce(str_query, null);

        glob_var.setIdSales(-1);
        glob_var.setStretch(-1);
        glob_var.setIdPlace(-1);
        msg.Say(Prefix.STRETCH);
        tsd.go();

    }

    //endregion


    // ------------------------------------------------------------------------------------------
    // region --- СНЯТИЕ


    // ------------------------------------------------------------------------------------------
    // --- запрос места для снятия
    private void getPlaceToRemove(){

        //String str_query = "SELECT * FROM fnTSDGetPlaceToRemove(1)";
        String str_query = "SET NOCOUNT ON; EXEC spTSDGetPlaceToRemove 1";
        glob_var.queryExecute(str_query, placeToRemoveListener);

    }

    Query.QueryListener placeToRemoveListener = new Query.QueryListener() {
        @Override
        public void onResult(JSONArray jsons) throws JSONException {

            if (msg.queryResOk(jsons)) {

                if (jsons.length() > 0) {

                    JSONObject j = jsons.getJSONObject(0);

                    glob_var.setPlace(new Place(j));

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            msg.Say(getResources().getString(R.string.say_scan_box) + "\\" + glob_var.getPlace().getPlace(), Prefix.PLACE);
                            tsd.go();

                        }
                    });

                } else {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            msg.Alert("Нет мест к снятию!", Prefix.COMMAND);
                            ((AppCompatActivity) getThis()).setTitle("Команда");
                            tsd.go();
                        }
                    });


                }

            } else {
                tsd.go();
            }
        }};



    // ------------------------------------------------------------------------------------------
    // --- запрос снять с адреса

    private void removeFromAddres(int id_place) {

        String str_query = String.format("SET NOCOUNT ON; EXEC Sklad.dbo.spTSDRemoveFromAdress %d, %d", id_place, glob_var.getIdPerson());

        glob_var.queryExecute(str_query, removeFromAdressListener);

    }

//
//    private void removeFromAddresTest() {
//
//        String str_query = String.format("SET NOCOUNT ON; EXEC Sklad.dbo.spTSDRemoveFromAdress %d, %d", 1, 1);
//        Query query = new Query(this);
//        query.setQueryListener(removeFromAdressListener);
//        query.execute(str_query);
//
//    }

    // ------------------------------------------------------------------------------------------
    // --- отклик снять с адреса


    Query.QueryListener removeFromAdressListener = new Query.QueryListener() {
        @Override
        public void onResult(JSONArray jsons) throws JSONException {


            if (msg.queryResOk(jsons))  {

                if (jsons.length() > 0){

                    JSONObject j = jsons.getJSONObject(0);

                    int is_error = j.getInt("1");
                    //final String txt = rs.getString(2);

                    if (is_error == 0) {

                        if (glob_var.getPlace() == null) {
                            // режим снять один

                            msg.Say("OK!\\Ящик снят", Prefix.PLACE);

                            handler.postDelayed(

                                    new Runnable() {
                                        @Override
                                        public void run() {

                                            msg.Say(Prefix.PLACE);


                                        }

                                    }, Msg.SHORT_ALERT);

                                    tsd.go();

                        } else { // режим снятия со списком

                           // glob_var.indx_places++;

//                            if (glob_var.places.size() > glob_var.indx_places) {
                                glob_var.setPlace(null);

                                msg.Say("OK", Prefix.PLACE);

                                handler.postDelayed(

                                        new Runnable() {
                                            @Override
                                            public void run() {

//                                                msg.Say(getResources().getString(R.string.say_scan_box) + "\\" + glob_var.places.get(glob_var.indx_places).getPlace(), BarCode.TYPE_PLACE);
//                                                tsd.go();

                                                getPlaceToRemove();

                                            }

                                        }, 100);



                    }

                    } else{

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                glob_var.setIdPlace(0);
                                msg.Alert("Ошибка снятия\\Попробуйте еще раз!", Prefix.PLACE);
                                tsd.go();
                            }
                        });

                    }



                } else {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            glob_var.setIdPlace(0);
                            msg.Alert("Ошибка spTSDRemoveFromAdress", Prefix.COMMAND);
                            tsd.go();
                        }
                    });
                }

            } else {tsd.go();}
    }};





    // ------------------------------------------------------------------------------------------
    // --- запрос бейджа

    private void getPerson(BarCode barcode) {

        glob_var.setIdPerson(barcode.intBody());
        String str_query = String.format("Select * from [Sklad].[dbo].[fnGetStuffInfoForPerson](%d)", glob_var.getIdPerson());

        //String str_query = String.format("SELECT 'Иванов Иван Иванович' name, 'Нет такого человека!' txt, 0 isError FROM [Sklad].[dbo].[fnGetStatSmenaMerchForPerson](%d)", glob_var.id_person);
        glob_var.queryExecute(str_query, infoForPersonListener);

    }

    // ------------------------------------------------------------------------------------------
    // --- Отклик на запрос бейджа

    Query.QueryListener infoForPersonListener = new Query.QueryListener() {
        @Override
        public void onResult(JSONArray jsons) throws JSONException {

            if (msg.queryResOk(jsons)) {

                if (jsons.length() > 0) {

                    JSONObject j = jsons.getJSONObject(0);

                    boolean is_error = j.getBoolean("isError");

                    if (!is_error) {
                        String name = j.getString("name");
                        glob_var.setManager(name);
                        textManager.setText(glob_var.getManager());
                        msg.Say(Prefix.COMMAND);
                        ((AppCompatActivity)getThis()).setTitle("Команда");

                    } else {

                        String str_error = j.getString("txt");

                        glob_var.setIdPerson(-1);

                        final String finalStr_error = str_error;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                msg.Alert(finalStr_error, Msg.LONG_ALERT, 0, Prefix.BADGE);
                            }
                        });

                    }

                }
                else
                {
                 runOnUiThread(new Runnable() {
                     @Override
                     public void run() {
                         glob_var.setIdPerson(-1);
                         msg.Alert("fnGetStuffInfoForPerson\\Ошибка кода сотрудника!", Prefix.BADGE);
                     }
                 });
                }

                        tsd.go();


            } else {tsd.go();}
        }
    };



    // ------------------------------------------------------------------------------------------
    // --- события ТСД
    // ------------------------------------------------------------------------------------------



        @Override
        protected void onResume() {
            super.onResume();
            if (tsd != null)  tsd.onResume();
        }

        @Override
        public void onPause() {
            super.onPause();
            if (tsd != null)  tsd.onPause();
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            if (tsd != null)    tsd.onDestroy();
        }



        @Override
        public void onScan(final String value) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Go(new BarCode(value));
                }});
        }




        // ------------------------------------------------------------------------------------------

        public Context getThis() {
            return this;
        }

        @Override
        protected String[] getTestQueries() {
            return new String[]{"SELECT top 1 1 FROM [Sklad].[dbo].[spStuff]"};
        }

        @Override
        protected Server.ConnectionType[] getConnectionType() {
            return new Server.ConnectionType[]{Server.ConnectionType.Sklad};
        }

        @Override
        protected void getVersion() {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            String ver =  com.labirint.tsdsorter.BuildConfig.VERSION_NAME;
            editor.putString("version", ver);
            editor.commit();
        }

        @Override
        public void onBackPressed() {
            ExitActivity.exitApplication(this);
        }





    // ------------------------------------------------------------------------------------------

}
