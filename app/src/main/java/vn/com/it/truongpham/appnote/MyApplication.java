package vn.com.it.truongpham.appnote;

import android.app.Application;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

public class MyApplication extends Application implements LifecycleObserver {
    public static boolean checkRunning,checkDestroy,checkStop;
    @Override
    public void onCreate() {
        super.onCreate();
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void appInResumeState() {
        checkRunning =true;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void appInON_STOP(){
        checkStop =true;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void appInDestroy() {
        checkDestroy=true;
    }
}
