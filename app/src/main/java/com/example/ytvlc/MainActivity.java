package com.example.ytvlc;

import android.content.SharedPreferences;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.EditTextPreference;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.settings_container, new SettingsFragment())
            .commit();
        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (intent == null || intent.getAction() == null) return;

        if (Intent.ACTION_SEND.equals(intent.getAction())
                && "text/plain".equals(intent.getType())) {
            String videoId = YouTubeUtil.extractVideoId(intent);
            if (videoId == null) {
                Toast.makeText(this, R.string.msg_invalid_url, Toast.LENGTH_LONG).show();
                return;
            }

            String cleanUrl = YouTubeUtil.makeCleanUrl(videoId);
            String endpoint = Pref.getEndpoint(this);
            String videoCodec = Pref.getVideoCodec(this);
            String audioCodec = Pref.getAudioCodec(this);

            Intent vlcIntent = new Intent(Intent.ACTION_VIEW);
            vlcIntent.setPackage("org.videolan.vlc");

            Uri videoUri = YouTubeUtil.makeVlcUrl(endpoint, cleanUrl, videoCodec, audioCodec);
            vlcIntent.setDataAndTypeAndNormalize(videoUri, "video/*");

            try {
                startActivity(vlcIntent);
                finish();
            } catch (Exception e) {
                Toast.makeText(this, R.string.msg_vlc_not_found, Toast.LENGTH_LONG).show();
            }
            return;
        }
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
