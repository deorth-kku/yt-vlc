package com.example.ytvlc;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.EditTextPreference;
import androidx.preference.PreferenceFragmentCompat;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.settings_container, new SettingsFragment())
            .commit();
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.preferences, rootKey);

            EditTextPreference endpointPref = findPreference("endpoint");
            if (endpointPref != null) {
                String saved = Pref.getEndpoint(requireContext());
                endpointPref.setText(saved);
                endpointPref.setSummary(saved);
                endpointPref.setOnPreferenceChangeListener((pref, newValue) -> {
                    Pref.setEndpoint(requireContext(), (String) newValue);
                    return true;
                });
            }
        }
    }
}
