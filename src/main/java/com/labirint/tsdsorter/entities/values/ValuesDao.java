package com.labirint.tsdsorter.entities.values;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

@Dao
public interface ValuesDao {

    @Query("SELECT * FROM tbl_values")
    Values getValues();

//    @Query("UPDATE options SET IdTable = -1, IdManager = -1, Manager = '', IdRack = -1, NumRack = -1, isStatic = 0")
//    void clear();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Values values);

    @Query("DELETE FROM tbl_values")
    void delete();

    @Transaction
    default void clear() {
        delete();
        insert(new Values());
    }

}
