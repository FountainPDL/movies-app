package net.akumani.movies.downloads;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class DownloadService extends Service {

    private static final String TAG = "DownloadService";
    private static final String CHANNEL_ID = "DOWNLOAD_CHANNEL";
    private static final int NOTIFICATION_ID = 1;

    private final IBinder binder = new DownloadBinder();
    private volatile boolean paused = false;
    private volatile boolean cancelled = false;

    private Map<DownloadRequest, String> currentUrls = new HashMap<>();
    private Map<DownloadRequest, Long> totalSizes = new HashMap<>();
    private Map<DownloadRequest, Long> writtenSizes = new HashMap<>();

    public class DownloadBinder extends Binder {
        public DownloadService getService() {
            return DownloadService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        createNotificationChannel();

        DownloadRequest req = (DownloadRequest) intent.getSerializableExtra("request");
        if (req == null) {
            stopSelf();
            return START_NOT_STICKY;
        }

        new DownloadTask(req).execute();
        return START_STICKY;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID,
                "Downloads",
                NotificationManager.IMPORTANCE_LOW
            );
            channel.setDescription("Download progress notifications");
            NotificationManager m = getSystemService(NotificationManager.class);
            if (m != null) {
                m.createNotificationChannel(channel);
            }
        }
    }

    private Notification createNotification(DownloadRequest req, int progress, String desc) {
        return new NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Downloading: " + req.tmdbId)
            .setContentText(desc)
            .setSmallIcon(android.R.drawable.ic_menu_upload)
            .setProgress(100, progress, false)
            .setOngoing(true)
            .build();
    }

    private class DownloadTask extends AsyncTask<Void, Integer, Boolean> {

        private final DownloadRequest req;
        private File destFile;

        public DownloadTask(DownloadRequest req) {
            this.req = req;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                String url = req.sourceUrl;
                HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
                conn.setRequestMethod("GET");
                conn.setInstanceFollowRedirects(true);

                long totalSize = conn.getContentLengthLong();
                totalSizes.put(req, totalSize);

                InputStream in = conn.getInputStream();
                destFile = DownloadPathResolver.getPathFor(DownloadService.this, req.tmdbId, req.type, req.season, req.episode);
                destFile.getParentFile().mkdirs();

                RandomAccessFile out = new RandomAccessFile(destFile, "rw");
                out.setLength(0);

                byte[] buffer = new byte[8192];
                int n;
                long totalWritten = 0;

                while (!isCancelled() && !cancelled && (n = in.read(buffer)) != -1) {
                    out.write(buffer, 0, n);
                    totalWritten += n;
                    writtenSizes.put(req, totalWritten);

                    int percent = 0;
                    if (totalSize > 0) {
                        percent = (int) ((totalWritten * 100) / totalSize);
                    }
                    if (percent > 0) {
                        publishProgress(percent);
                    }

                    if (isCancelled() || paused) {
                        break;
                    }
                }

                in.close();
                out.close();
                return !isCancelled() && !paused;
            } catch (IOException e) {
                Log.e(TAG, "Download failed for " + req.tmdbId, e);
                return false;
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            int progress = values[0];
            startForeground(
                NOTIFICATION_ID,
                createNotification(req, progress, "Downloading... " + progress + "%")
            );
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (destFile != null) {
                Log.d("DownloadService", "Finished to " + destFile.getAbsolutePath());
            }
            stopForeground(true);
            DownloadService.this.stopSelf();
        }

        @Override
        protected void onCancelled() {
            paused = false;
            cancelled = true;
            stopForeground(true);
            DownloadService.this.stopSelf();
        }
    }

    public void pauseDownload(DownloadRequest req) {
        paused = true;
    }

    public void resumeDownload(DownloadRequest req) {
        paused = false;
    }

    public void cancelDownload(DownloadRequest req) {
        cancelled = true;
    }

    public long getTotalSize(DownloadRequest req) {
        return totalSizes.getOrDefault(req, 0L);
    }

    public long getWrittenSize(DownloadRequest req) {
        return writtenSizes.getOrDefault(req, 0L);
    }
}
