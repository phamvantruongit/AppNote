package vn.com.it.truongpham.appnote.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface BookDAO {
    @Insert
    void insertBook(Book... Book);

    @Query("SELECT * FROM Book  WHERE id_type_book in (:id)  ORDER BY id desc")
    LiveData<List<Book>> getListBook(int id);


    @Query("update Book set chapter= :title ,content= :content where id in (:uid)")
    void update(String title, String content, int uid);


    @Query("DELETE FROM Book WHERE id in (:id)")
    void delete(int id);


    @Query("select * from Book where chapter LIKE :title OR content LIKE :name  ORDER BY id desc")
    LiveData<List<Book>> findByName(String title ,String name);



}
