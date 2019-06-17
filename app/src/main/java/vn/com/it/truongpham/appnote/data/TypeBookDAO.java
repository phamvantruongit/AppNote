package vn.com.it.truongpham.appnote.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TypeBookDAO {

    @Insert void insertTypeBook(TypeBook... typeBook);

    @Query("SELECT * FROM typebook ORDER BY id desc ")
    LiveData<List<TypeBook>> getListTypeBook();


    @Query("update typebook set name = :name where id in (:uid)")
    void update(String name, int uid);

    @Query("select * from typebook where name LIKE :find_name  ORDER BY id desc")
    LiveData<List<TypeBook>> findByName(String find_name);


    @Query("select * from typebook where date LIKE :date  ORDER BY id desc")
    LiveData<List<TypeBook>> findByDate(String date);




}
