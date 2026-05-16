package net.akumani.movies.downloads;

import android.content.Context;
import android.util.Log;

import net.akumani.movies.db.MovieDatabase;
import net.akumani.movies.db.MediaEntry;
import net.akumani.movies.db.DownloadEntry;

public class DownloadHelper {

    private static final String TAG = "DownloadHelper";

    public static void requestDownload(Context ctx, int tmdbId, String type,
                                       Integer season, Integer episode, String sourceUrl) {
        DownloadRequest req = new DownloadRequest(tmdbId, type, season, episode, sourceUrl);
        DownloadManager.startDownload(ctx, req);

        MovieDatabase db = MovieDatabase.getDatabase(ctx);
        DownloadEntry d = new DownloadEntry();
        d.tmdbId = tmdbId;
        d.type = type;
        d.season = season;
        d.episode = episode;
        d.sourceUrl = sourceUrl;
        d.queued = true;
        d.finished = false;

        db.downloadDao().insert(d);

        MediaEntry m = db.libraryDao().findById(tmdbId);
        if (m != null) {
            m.downloaded = false;
            db.libraryDao().update(m);
        } else {
            Log.d(TAG, "No library entry for tmdbId=" + tmdbId);
        }
    }

    public static void onDownloadFinished(Context ctx, int tmdbId, String type,
                                          Integer season, Integer episode) {
        MovieDatabase db = MovieDatabase.getDatabase(ctx);

        DownloadEntry d = db.downloadDao().findByEpisode(tmdbId, type, season, episode);
        if (d != null) {
            d.finished = true;
            d.queued = false;
            db.downloadDao().update(d);

            MediaEntry m = db.libraryDao().findById(tmdbId);
            if (m != null) {
                m.downloaded = true;
                m.lastWatched = System.currentTimeMillis();
                db.libraryDao().update(m);
            }
        }
    }
}
