package vn.com.it.truongpham.appnote;

import android.app.Application;
import android.widget.Toast;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

public class MyApplication extends Application implements LifecycleObserver {
    public static boolean checkRunningApp;
    @Override
    public void onCreate() {
        super.onCreate();
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void appInResumeState() {
        checkRunningApp=true;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void appInDestroy() {
        checkRunningApp=false;
    }
}
