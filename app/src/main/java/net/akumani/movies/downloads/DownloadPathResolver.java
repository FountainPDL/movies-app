package net.akumani.movies.downloads;

import android.content.Context;

import java.io.File;

public class DownloadPathResolver {

    public static File getDownloadRoot(Context ctx) {
        File external = ctx.getExternalFilesDir(null);
        if (external == null) external = ctx.getFilesDir();
        return new File(external, "downloads");
    }

    public static File getMoviePath(Context ctx, int tmdbId) {
        File root = getDownloadRoot(ctx);
        return new File(root, "movies/" + tmdbId + ".mp4");
    }

    public static File getTvEpisodePath(Context ctx, int tmdbId, int season, int episode) {
        File root = new File(getDownloadRoot(ctx), "tv/" + tmdbId + "/season-" + season);
        root.mkdirs();
        return new File(root, "episode-" + episode + ".mp4");
    }

    public static File getPathFor(Context ctx, int tmdbId, String type, Integer season, Integer episode) {
        if ("movie".equals(type)) {
            return getMoviePath(ctx, tmdbId);
        } else if ("tv".equals(type) && season != null && episode != null) {
            return getTvEpisodePath(ctx, tmdbId, season, episode);
        }
        throw new IllegalArgumentException("Invalid type/season/episode");
    }
}
