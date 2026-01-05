package eu.faircode.netguard;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity {
    private static final String TAG = "NetGuard.BaseActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!AuthManager.isAuthenticated()) {
            Log.w(TAG, "Access denied to " + this.getClass().getSimpleName() + ". Not authenticated.");
            Toast.makeText(this, "Access Denied", Toast.LENGTH_SHORT).show();
            finish();
            // Using return is critical to stop the rest of onCreate from executing.
            return;
        }
    }

    // The onResume check is no longer needed because of our new Application-level guard.
}