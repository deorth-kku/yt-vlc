package com.example.ytvlc;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

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
        private final Preference.OnPreferenceChangeListener summaryUpdater = (pref, newValue) -> {
            pref.setSummary(newValue.toString());
            return true;
        };

        private final SharedPreferences.OnSharedPreferenceChangeListener sharedListener = (sharedPrefs, key) -> {
            EditTextPreference ep = findPreference("endpoint");
            if (ep != null) ep.setSummary(sharedPrefs.getString("endpoint", ""));
            ListPreference vp = findPreference("video_codec");
            if (vp != null) vp.setSummary(sharedPrefs.getString("video_codec", ""));
            ListPreference ap = findPreference("audio_codec");
            if (ap != null) ap.setSummary(sharedPrefs.getString("audio_codec", ""));
        };

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.preferences, rootKey);

            EditTextPreference endpointPref = findPreference("endpoint");
            if (endpointPref != null) {
                String saved = Pref.getEndpoint(requireContext());
                endpointPref.setText(saved);
                endpointPref.setSummary(saved);
                endpointPref.setOnPreferenceChangeListener(summaryUpdater);
            }

            ListPreference videoPref = findPreference("video_codec");
            if (videoPref != null) {
                String saved = Pref.getVideoCodec(requireContext());
                videoPref.setValue(saved);
                videoPref.setSummary(saved);
                videoPref.setOnPreferenceChangeListener(summaryUpdater);
            }

            ListPreference audioPref = findPreference("audio_codec");
            if (audioPref != null) {
                String saved = Pref.getAudioCodec(requireContext());
                audioPref.setValue(saved);
                audioPref.setSummary(saved);
                audioPref.setOnPreferenceChangeListener(summaryUpdater);
            }

            PreferenceManager.getDefaultSharedPreferences(requireContext())
                .registerOnSharedPreferenceChangeListener(sharedListener);
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            PreferenceManager.getDefaultSharedPreferences(requireContext())
                .unregisterOnSharedPreferenceChangeListener(sharedListener);
        }
    }
}
