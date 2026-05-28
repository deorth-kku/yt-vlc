package com.example.ytvlc;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
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

            ListPreference videoPref = findPreference("video_codec");
            if (videoPref != null) {
                String saved = Pref.getVideoCodec(requireContext());
                videoPref.setValue(saved);
                videoPref.setSummary(saved);
                videoPref.setOnPreferenceChangeListener((pref, newValue) -> {
                    Pref.setVideoCodec(requireContext(), (String) newValue);
                    return true;
                });
            }

            ListPreference audioPref = findPreference("audio_codec");
            if (audioPref != null) {
                String saved = Pref.getAudioCodec(requireContext());
                audioPref.setValue(saved);
                audioPref.setSummary(saved);
                audioPref.setOnPreferenceChangeListener((pref, newValue) -> {
                    Pref.setAudioCodec(requireContext(), (String) newValue);
                    return true;
                });
            }
        }
    }
}
