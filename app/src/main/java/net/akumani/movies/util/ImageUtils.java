package net.akumani.movies.util;

public class ImageUtils {
    private static final String BASE_IMG = "https://image.tmdb.org/t/p/";

    public static String getImageUrl(String path, String size) {
        if (path == null) return null;
        return BASE_IMG + size + path;
    }

    public static String getPosterUrl(String path) {
        return getImageUrl(path, "w500");
    }

    public static String getBackdropUrl(String path) {
        return getImageUrl(path, "original");
    }
}
