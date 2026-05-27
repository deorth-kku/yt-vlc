package com.example.ytvlc;

import android.content.Intent;
import android.net.Uri;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class YouTubeUtil {

    /**
     * 从分享 intent 提取 YouTube 视频 ID
     */
    public static String extractVideoId(Intent intent) {
        String text = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (text == null) return null;

        // YouTube native URI schemes
        Uri uri = Uri.parse(text);
        String scheme = uri.getScheme();

        if ("vnd.youtube".equals(scheme) || "vnd.youtube.launch".equals(scheme)
                || "vnd.youtube.player".equals(scheme) || "vnd.youtube.share".equals(scheme)) {
            // vnd.youtube://<video_id> 或 vnd.youtube://watch?v=<video_id>
            String id = uri.getSchemeSpecificPart();
            if (id.startsWith("watch?v=")) {
                id = id.substring("watch?v=".length());
            }
            return id;
        }

        // Extract video ID from various URL patterns
        String id = extractVideoIdFromUrl(text);
        if (id != null) return id;

        return null;
    }

    /**
     * 从纯文本 URL 提取视频 ID
     */
    public static String extractVideoIdFromUrl(String text) {
        // https://www.youtube.com/watch?v=xxx
        // https://youtu.be/xxx
        // https://m.youtube.com/watch?v=xxx
        // vnd.youtube share links
        Pattern p = Pattern.compile(
            "(?:youtube\\.com/(?:watch\\?v=|embed/|v/)|youtu\\.be/)([a-zA-Z0-9_-]{11})");
        Matcher m = p.matcher(text);
        if (m.find()) return m.group(1);

        return null;
    }

    /**
     * 构造纯净 YouTube URL (不含 query params)
     */
    public static String makeCleanUrl(String videoId) {
        return "https://www.youtube.com/watch?v=" + videoId;
    }

    /**
     * 构造 VLC 播放 URL
     * 格式: http://<endpoint>/manifest?url=<youtubeUrl>
     */
    public static Uri makeVlcUrl(String endpoint, String youtubeUrl) {
        return Uri.parse( endpoint + "?url=" + youtubeUrl);
    }
}
