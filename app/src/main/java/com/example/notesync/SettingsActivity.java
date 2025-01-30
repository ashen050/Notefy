package com.example.notesync;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {

    private Switch darkModeSwitch;
    private RadioGroup languageGroup;
    private Button saveSettingsButton, backToMenuButton;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        darkModeSwitch = findViewById(R.id.switchDarkMode);
        languageGroup = findViewById(R.id.languageGroup);
        saveSettingsButton = findViewById(R.id.saveSettingsButton);
        backToMenuButton = findViewById(R.id.backToMenuButton);

        sharedPreferences = getSharedPreferences("AppSettings", MODE_PRIVATE);
        loadSettings();

        darkModeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        });

        saveSettingsButton.setOnClickListener(v -> saveSettings());

        backToMenuButton.setOnClickListener(v -> {
            startActivity(new Intent(SettingsActivity.this, MenuActivity.class));
            finish();
        });
    }

    private void saveSettings() {
        int selectedLanguageId = languageGroup.getCheckedRadioButtonId();
        RadioButton selectedLanguage = findViewById(selectedLanguageId);

        if (selectedLanguage != null) {
            String languageCode = "en";
            if (selectedLanguage.getText().toString().equals("Afrikaans")) {
                languageCode = "af";
            } else if (selectedLanguage.getText().toString().equals("Zulu")) {
                languageCode = "zu";
            }

            setLocale(languageCode);

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("language", languageCode);
            editor.putBoolean("darkMode", darkModeSwitch.isChecked());
            editor.apply();

            Toast.makeText(this, "Settings saved", Toast.LENGTH_SHORT).show();

            // Restart activity to apply changes
            finish();
            startActivity(getIntent());
        }
    }

    private void setLocale(String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.setLocale(locale); // Updated for newer Android versions
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
    }

    private void loadSettings() {
        boolean darkModeEnabled = sharedPreferences.getBoolean("darkMode", false);
        darkModeSwitch.setChecked(darkModeEnabled);

        String savedLanguage = sharedPreferences.getString("language", "en");
        switch (savedLanguage) {
            case "af":
                languageGroup.check(R.id.radioAfrikaans);
                break;
            case "zu":
                languageGroup.check(R.id.radioZulu);
                break;
            default:
                languageGroup.check(R.id.radioEnglish);
                break;
        }
    }
}
