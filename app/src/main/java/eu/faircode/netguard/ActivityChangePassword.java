package eu.faircode.netguard;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.Nullable;

public class ActivityChangePassword extends BaseActivity { // It's a protected screen

    private static final String PREFS_NAME = "PasswordPrefs";
    private static final String KEY_PASSWORD_HASH = "password_hash";
    private SharedPreferences passwordPrefs;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_Material3);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        passwordPrefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        EditText etCurrentPassword = findViewById(R.id.etCurrentPassword);
        EditText etNewPassword = findViewById(R.id.etNewPassword);
        EditText etConfirmNewPassword = findViewById(R.id.etConfirmNewPassword);
        Button btnChangePassword = findViewById(R.id.btnChangePassword);

        btnChangePassword.setOnClickListener(v -> {
            String currentPassword = etCurrentPassword.getText().toString();
            String newPassword = etNewPassword.getText().toString();
            String confirmNewPassword = etConfirmNewPassword.getText().toString();

            String storedHash = passwordPrefs.getString(KEY_PASSWORD_HASH, "");

            // 1. Verify current password
            if (!String.valueOf(currentPassword.hashCode()).equals(storedHash)) {
                Toast.makeText(this, "הסיסמה הנוכחית שגויה", Toast.LENGTH_SHORT).show();
                return;
            }

            // 2. Verify new passwords match
            if (!newPassword.equals(confirmNewPassword)) {
                Toast.makeText(this, "הסיסמאות החדשות אינן תואמות", Toast.LENGTH_SHORT).show();
                return;
            }

            // 3. Verify new password is not empty
            if (newPassword.isEmpty()) {
                Toast.makeText(this, "הסיסמה החדשה לא יכולה להיות ריקה", Toast.LENGTH_SHORT).show();
                return;
            }

            // 4. Save new password hash
            String newPasswordHash = String.valueOf(newPassword.hashCode());
            passwordPrefs.edit().putString(KEY_PASSWORD_HASH, newPasswordHash).apply();

            Toast.makeText(this, "הסיסמה עודכנה בהצלחה", Toast.LENGTH_SHORT).show();
            finish(); // Close the screen
        });
    }
}