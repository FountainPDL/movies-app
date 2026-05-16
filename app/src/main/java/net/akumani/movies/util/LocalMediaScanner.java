package net.akumani.movies.util;

import android.content.Context;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class LocalMediaScanner {

    public static class LocalMediaItem {
        public final String id;
        public final String title;
        public final String path;
        public final long duration;
        public final String thumbnail;

        public LocalMediaItem(String id, String title, String path, long duration, String thumbnail) {
            this.id = id;
            this.title = title;
            this.path = path;
            this.duration = duration;
            this.thumbnail = thumbnail;
        }
    }

    public static LocalMediaItem[] scanLocalVideos(Context ctx) {
        List<LocalMediaItem> items = new ArrayList<>();

        File home = ctx.getExternalFilesDir(null);
        if (home == null) return items.toArray(new LocalMediaItem[0]);

        File[] dirs = new File[] {
            new File(home, "Movies"),
            new File(home, "TV"),
            new File(home, "Downloads")
        };

        for (File dir : dirs) {
            if (!dir.exists()) continue;
            scanDir(dir, items);
        }

        return items.toArray(new LocalMediaItem[0]);
    }

    private static void scanDir(File dir, List<LocalMediaItem> out) {
        File[] files = dir.listFiles();
        if (files == null) return;

        for (File f : files) {
            if (f.isDirectory()) {
                scanDir(f, out);
            } else if (isVideoExt(f.getName())) {
                String name = f.getName();
                long dur = estimateDuration(name);

                out.add(new LocalMediaItem(
                    f.getAbsolutePath(),
                    name,
                    f.getAbsolutePath(),
                    dur,
                    null
                ));
            }
        }
    }

    private static boolean isVideoExt(String name) {
        String n = name.toLowerCase();
        return n.endsWith(".mp4") || n.endsWith(".mkv") || n.endsWith(".webm");
    }

    private static long estimateDuration(String name) {
        return 60 * 60 * 1000; // 1 hour stub
    }
}
