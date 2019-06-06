package vn.com.it.truongpham.appnote;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.annotation.NonNull;

import java.util.List;

import vn.com.it.truongpham.appnote.data.TypeBook;

public class NoteBookViewModel extends AndroidViewModel {
    private LiveData<List<TypeBook>> liveData;
    public NoteBookViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<TypeBook>> getLiveData() {
        return liveData;
    }

    public void setLiveData(LiveData<List<TypeBook>> liveData) {
        this.liveData = liveData;
    }
}
