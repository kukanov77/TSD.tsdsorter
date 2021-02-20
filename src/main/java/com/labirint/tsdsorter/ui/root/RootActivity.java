package com.labirint.tsdsorter.ui.root;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.ui.AppBarConfiguration;
import org.json.JSONException;
import org.json.JSONObject;
import ru.labirint.core.entities.Barcode;
import ru.labirint.core.scanner.Scanner;
import ru.labirint.core.util.DownloadFTP;
import ru.labirint.core.util.messages.Msg;
import ru.labirint.core.util.messages.YesNoDlg;
import ru.labirint.core.util.messages.YesNoDlgListener;
import com.labirint.tsdsorter.App;
import com.labirint.tsdsorter.R;
import com.labirint.tsdsorter.data.QueryHelper;
import ru.labirint.core.ui.base.BaseFragment;
import com.labirint.tsdsorter.ui.work.WorkFragment;


public class RootActivity extends ru.labirint.core.ui.root.RootActivity implements Scanner.ScannerListener {


    private String version;
    Scanner scn; //терминал, сканер

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        scn = Scanner.newInstance( this, this, Build.MODEL, true );

    }


    @Override
    protected void initAppBarConfiguration() {
            appBarConfiguration = new AppBarConfiguration.Builder(
            R.id.splashFragment, R.id.workFragment)
            .build();
    }

    private BaseFragment getFragment() {
        return (BaseFragment) navHostFragment.getChildFragmentManager().getFragments().get(0);
    }

    private App app(){return (App) getApplicationContext();}

    // ------------------------------------------------------------------------------------------
    // --- запросы в базу

    public QueryHelper getQueryHelper(){
        return ((App)getApplicationContext()).getQueryHelper();
    }

    // -----------------------------------------------------------------------------------------
    // --- события ТСД
    // -----------------------------------------------------------------------------------------


    @Override
    public void onResume() {
        super.onResume();
        if (scn != null) scn.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (scn != null) scn.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (scn != null) scn.onDestroy();
    }


    @Override
    public void onScan(final String value) {
        runOnUiThread( () -> {
            if (IsConnected()) {
                ((com.labirint.tsdsorter.ui.base.BaseFragment)getFragment()).onScan(new Barcode(value));
            } else {
                onError( new Throwable("Нет сети!"));
            }
        } );
    }

    // на каждый вывод сообщения разрешаем сканироавать дальше
    public void scanOn() {
        if (scn != null) {
            scn.on();
        }
    }


    // ------------------------------------------------------------------------------------------
    // --- wifi connection
    @Override
    public void connected() {
        getFragment().connected();
    }

    @Override
    public void disconnected() {
        getFragment().disconnected();
    }

    // ------------------------------------------------------------------------------------------

    Handler handler = new Handler();

    @Override
    public void onError(Throwable error) {
       hideLoading();
       handler.removeCallbacksAndMessages(null);
       YesNoDlg alert = Msg.OkAlert(this, error.getMessage(), true, Gravity.CENTER, true);

       handler.postDelayed(()->{
                    if (alert != null) alert.dismiss();
                },
                10000);

        BaseFragment fragment = getFragment();

        if (fragment instanceof WorkFragment){
            ((WorkFragment)fragment).backScanKey();
        }

       scanOn();
    }

    // ------------------------------------------------------------------------------------------

    @Override
    public void doAfterSplash(JSONObject j) {
        try {
            if (j != null){
                // --- проверка обновлений на ftp
                if (j.has( "ver" )) {
                    version = j.getString( "ver" );
                    //проверка обновлений на ftp
                    if (checkPermission()) {
                        checkAppVersion();
                    } else {
                        requestPermission();
                    }
                }
                // ---
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        getNavController().navigate(R.id.action_splashFragment_to_workFragment);
    }

    // ------------------------------------------------------------------------------------------


    //-----------------------------------------------------------------------------------
    // какая-то магия для проверки прав на чтение-запись в локальное хранилище
    // начиная с какой-то версии Андроида уже не достаточно просто прописать права в манифесте
    // нужно, что пользователь подтвердил ручками, что можно читать-писать файлы на устройство
    private static final int PERMISSION_REQUEST_CODE = 11;

    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                    && (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                    && (ContextCompat.checkSelfPermission(this, Manifest.permission.REQUEST_INSTALL_PACKAGES) == PackageManager.PERMISSION_GRANTED);
        } else {
            return true;
        }

    }

    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                &&ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                &&ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.REQUEST_INSTALL_PACKAGES)
        ) {
            Toast.makeText(this, "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.REQUEST_INSTALL_PACKAGES}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("value", "Permission Granted, Now you can use local drive .");
                    checkAppVersion();
                } else {
                    Log.e("value", "Permission Denied, You cannot use local drive .");
                }
                break;
        }
    }

    private void checkAppVersion() {
        if (!((App)getApplicationContext()).getVersion().equals(version)) {
            YesNoDlg dlg = YesNoDlg.newInstance( new SpannableString(String.format( "Есть новая версия %s\nОбновить? ", version)), "Да", "Нет", false, Gravity.CENTER, false );
            FragmentManager fm = getSupportFragmentManager();
            dlg.setYesNoDlgListener( new YesNoDlgListener() {
                @Override
                public void onYes() {
                    //new DownloadFileFromURL().execute();
                    showLoading();
                    DownloadFTP.get(RootActivity.this, "TSDSorter.apk");
                }
            } );
            dlg.show( fm, "yes_no_dlg" );
        }
    }


    // ------------------------------------------------------------------------------------------
}
