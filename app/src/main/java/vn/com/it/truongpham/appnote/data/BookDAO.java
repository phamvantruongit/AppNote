package vn.com.it.truongpham.appnote.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface BookDAO {
    @Insert
    void insertBook(Book... Book);

    @Query("SELECT * FROM book where  id_type_book(:id) ")
    List<Book> getListBook(int id);
}
