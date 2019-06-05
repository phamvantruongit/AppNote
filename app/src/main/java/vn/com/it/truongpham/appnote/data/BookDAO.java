package vn.com.it.truongpham.appnote.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import io.reactivex.Single;

@Dao
public interface BookDAO {
    @Insert
    void insertBook(Book... Book);

    @Query("SELECT * FROM Book  WHERE id_type_book in (:id) ")
    LiveData<List<Book>> getListBook(int id);


    @Query("update Book set chapter= :title ,content= :content where id in (:uid)")
    void update(String title, String content, int uid);


    @Query("DELETE FROM Book WHERE id in (:id)")
    void delete(int id);



}
