package net.akumani.movies.downloads;

import android.content.Context;
import android.content.Intent;

public class DownloadManager {

    public static void startDownload(Context ctx, DownloadRequest req) {
        Intent i = new Intent(ctx, DownloadService.class);
        i.putExtra("request", req);
        ctx.startForegroundService(i);
    }
}
