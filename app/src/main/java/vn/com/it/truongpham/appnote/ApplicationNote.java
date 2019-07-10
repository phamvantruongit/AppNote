package vn.com.it.truongpham.appnote;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Room;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import vn.com.it.truongpham.appnote.data.AppDatabase;

public class ApplicationNote extends Application {
    public static AppDatabase db;
    String TAG=ApplicationNote.class.getSimpleName();
    public AppDatabase createDatabaseUser() {
        if (db == null) {
            return   db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "database-userEntity").allowMainThreadQueries().build();
        }
        return db;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        MobileAds.initialize(this, this.getString(R.string.app_ads));
        createDatabaseUser();


    }
}
