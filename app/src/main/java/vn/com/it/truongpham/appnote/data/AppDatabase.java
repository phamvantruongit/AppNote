package vn.com.it.truongpham.appnote.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {TypeBook.class,Book.class},version = 3 ,exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract TypeBookDAO typeBookDAO();
    public abstract BookDAO bookDAO();
}
