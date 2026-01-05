package eu.faircode.netguard;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

public class ApplicationEx extends Application implements LifecycleObserver {
    private static final String TAG = "NetGuard.App";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "Create version=" + Util.getSelfVersionName(this) + "/" + Util.getSelfVersionCode(this));

        // This is our new security guard. It watches the entire app.
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onAppInBackground() {
        // This method is called when the entire application goes into the background.
        Log.d(TAG, "App went to background. Locking...");
        AuthManager.setAuthenticated(false);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onAppInForeground() {
        // This method is called when the application comes back to the foreground.
        Log.d(TAG, "App came to foreground.");
        // We don't need to do anything here, the lock screen will handle authentication.
    }
}