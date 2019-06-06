package vn.com.it.truongpham.appnote;

import android.app.Application;
import androidx.room.Room;

import vn.com.it.truongpham.appnote.data.AppDatabase;

public class ApplicationNote extends Application {
    public static AppDatabase db;

    public AppDatabase createDatabaseUser() {
        if (db == null) {
            return   db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "database-userEntity").allowMainThreadQueries().build();
        }
        return db;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        createDatabaseUser();
    }
}
