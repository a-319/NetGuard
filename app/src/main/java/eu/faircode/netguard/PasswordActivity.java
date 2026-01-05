package eu.faircode.netguard;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class PasswordActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "PasswordPrefs";
    private static final String KEY_PASSWORD_HASH = "password_hash";
    private SharedPreferences passwordPrefs;

    private LinearLayout loginView;
    private LinearLayout createPasswordView;

    private EditText etPassword;
    private Button btnSubmit;
    private ImageButton btnSettings;

    private EditText etNewPassword;
    private EditText etConfirmPassword;
    private Button btnCreate;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_Material3);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        passwordPrefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        loginView = findViewById(R.id.login_view);
        createPasswordView = findViewById(R.id.create_password_view);
        etPassword = findViewById(R.id.etPassword);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnSettings = findViewById(R.id.btnSettings);
        etNewPassword = findViewById(R.id.etNewPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnCreate = findViewById(R.id.btnCreate);

        if (isPasswordSet()) {
            showLoginView();
        } else {
            showCreatePasswordView();
        }

        btnSubmit.setOnClickListener(v -> handleLogin());
        btnCreate.setOnClickListener(v -> handleCreatePassword());
        btnSettings.setOnClickListener(v -> {
            // THE FIX: We must authenticate BEFORE trying to open a protected activity
            AuthManager.setAuthenticated(true);
            Intent intent = new Intent(PasswordActivity.this, ActivityChangePassword.class);
            startActivity(intent);
        });
    }

    // Add onResume to de-authenticate when returning to this screen
    @Override
    protected void onResume() {
        super.onResume();
        // De-authenticate when the lock screen is shown again.
        // This ensures that after changing the password and returning,
        // the user has to log in again with the new password.
        AuthManager.setAuthenticated(false);
    }

    private boolean isPasswordSet() {
        return passwordPrefs.getString(KEY_PASSWORD_HASH, null) != null;
    }

    private void showLoginView() {
        loginView.setVisibility(View.VISIBLE);
        createPasswordView.setVisibility(View.GONE);
        btnSettings.setVisibility(View.VISIBLE);
    }

    private void showCreatePasswordView() {
        loginView.setVisibility(View.GONE);
        createPasswordView.setVisibility(View.VISIBLE);
        btnSettings.setVisibility(View.GONE);
    }

    private void handleLogin() {
        String enteredPassword = etPassword.getText().toString();
        String storedHash = passwordPrefs.getString(KEY_PASSWORD_HASH, "");

        if (String.valueOf(enteredPassword.hashCode()).equals(storedHash)) {
            AuthManager.setAuthenticated(true);
            Intent intent = new Intent(PasswordActivity.this, ActivityMain.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, R.string.password_incorrect, Toast.LENGTH_SHORT).show();
            etPassword.setText("");
        }
    }

    private void handleCreatePassword() {
        String newPassword = etNewPassword.getText().toString();
        String confirmPassword = etConfirmPassword.getText().toString();

        if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            Toast.makeText(this, R.string.password_mismatch, Toast.LENGTH_SHORT).show();
            return;
        }

        String passwordHash = String.valueOf(newPassword.hashCode());
        passwordPrefs.edit().putString(KEY_PASSWORD_HASH, passwordHash).apply();

        Toast.makeText(this, R.string.password_created_successfully, Toast.LENGTH_SHORT).show();

        AuthManager.setAuthenticated(true);
        Intent intent = new Intent(PasswordActivity.this, ActivityMain.class);
        startActivity(intent);
        finish();
    }
}