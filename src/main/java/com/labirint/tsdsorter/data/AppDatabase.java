package com.labirint.tsdsorter.data;


import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ru.labirint.core.data.iAppDatabase;
import ru.labirint.core.entities.SplashResponse;
import ru.labirint.core.util.NotImplementedException;
import com.labirint.tsdsorter.App;
import com.labirint.tsdsorter.entities.PlaceResponse;
import com.labirint.tsdsorter.entities.TextResponse;
import com.labirint.tsdsorter.entities.values.Values;
import com.labirint.tsdsorter.entities.values.ValuesDao;

@Database(
        entities = {
                SplashResponse.class
                , Values.class


        },
        version = 1,
        exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase implements iAppDatabase {

    public abstract ValuesDao getValuesDao();

    private static volatile AppDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService executor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    //private static Executor executor = Executors.newSingleThreadExecutor();

    public static AppDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, ((App)context).getDbName())
                            .allowMainThreadQueries() //позволяет делать запросы к локальной базе в основном потоке
                            .fallbackToDestructiveMigration() //пересоздает структуру БД без миграции, но стирает данные

                            .addCallback(new Callback() {
                                @Override
                                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                    // при создании базы, заполняем таблицу Values
                                    executor.execute(() -> {
                                        getInstance(context).getValuesDao().insert(new Values());
                                    });
                                    super.onCreate(db);
                                }
                            })

                            .build();
                }
            }
        }
        return INSTANCE;
    }


//    public void clear() {
//        getOptionsDao().clear();
//        getBooksDao().delete();
//        getISBNDao().delete();
//    }



    public <T> List<T> get(Class<T> clazz){
        if (clazz == SplashResponse.class) return (List<T>)getSplashDao().getSplashResponse();
        return new ArrayList<>();
    }

    private <T> T getFrom(Object obj){
        List<T> listObj =  (List<T>)obj;
        T res = null;
        if (listObj.size() > 0) {res = listObj.get(0);}
        return res;
    }

    public <T> void save(Object obj, Class<T> clazz){
        if (clazz == SplashResponse.class){
            SplashResponse splashResponse = getFrom(obj);
            getSplashDao().insert(splashResponse);
            return;
        }

        return;
    }


}

