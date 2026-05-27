package com.example.ytvlc;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn = findViewById(R.id.btn_settings);
        btn.setOnClickListener(v -> {
            startActivity(new Intent(this, SettingsActivity.class));
        });

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

            Intent vlcIntent = new Intent(Intent.ACTION_VIEW);
            vlcIntent.setPackage("org.videolan.vlc");

            // Set the media source path and the mime type
            Uri videoUri = YouTubeUtil.makeVlcUrl(endpoint, cleanUrl);
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
}
