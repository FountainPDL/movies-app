package net.akumani.movies.util;

import java.util.ArrayList;
import java.util.List;

public class SourceProvider {

    public static class Source {
        public final String url;
        public final String label;

        public Source(String url, String label) {
            this.url = url;
            this.label = label;
        }
    }

    public static List<Source> movieSources(Integer imdbId, int tmdbId) {
        List<Source> sources = new ArrayList<>();

        boolean hasImdb = imdbId != null && imdbId > 0;

        if (hasImdb) {
            sources.add(new Source(
                "https://vidsrc.xyz/embed/movie/" + imdbId,
                "VidSrc"
            ));
            sources.add(new Source(
                "https://www.2embed.cc/embed/" + imdbId,
                "2Embed"
            ));
            sources.add(new Source(
                "https://autoembed.cc/movie/imdb/" + imdbId,
                "AutoEmbed"
            ));
            sources.add(new Source(
                "https://multiembed.mov/?video_id=" + imdbId,
                "MultiEmbed"
            ));
        } else {
            sources.add(new Source(
                "https://vidsrc.xyz/embed/movie/" + tmdbId,
                "VidSrc (TMDB)"
            ));
            sources.add(new Source(
                "https://www.2embed.cc/embed/tmdb/movie?id=" + tmdbId,
                "2Embed (TMDB)"
            ));
            sources.add(new Source(
                "https://autoembed.cc/movie/tmdb/" + tmdbId,
                "AutoEmbed (TMDB)"
            ));
            sources.add(new Source(
                "https://multiembed.mov/?video_id=" + tmdbId + "&tmdb=1",
                "MultiEmbed (TMDB)"
            ));
        }

        return sources;
    }

    public static List<Source> tvSources(Integer imdbId, int tmdbId, int season, int episode) {
        List<Source> sources = new ArrayList<>();

        boolean hasImdb = imdbId != null && imdbId > 0;

        if (hasImdb) {
            sources.add(new Source(
                "https://vidsrc.xyz/embed/tv/" + imdbId + "/" + season + "/" + episode,
                "VidSrc"
            ));
            sources.add(new Source(
                "https://www.2embed.cc/embedtv/" + imdbId + "&s=" + season + "&e=" + episode,
                "2Embed"
            ));
            sources.add(new Source(
                "https://autoembed.cc/tv/imdb/" + imdbId + "/" + season + "/" + episode,
                "AutoEmbed"
            ));
            sources.add(new Source(
                "https://multiembed.mov/?video_id=" + imdbId + "&s=" + season + "&e=" + episode,
                "MultiEmbed"
            ));
        } else {
            sources.add(new Source(
                "https://vidsrc.xyz/embed/tv/" + tmdbId + "/" + season + "/" + episode,
                "VidSrc (TMDB)"
            ));
            sources.add(new Source(
                "https://www.2embed.cc/embedtv/tmdb/tv?id=" + tmdbId + "&s=" + season + "&e=" + episode,
                "2Embed (TMDB)"
            ));
            sources.add(new Source(
                "https://autoembed.cc/tv/tmdb/" + tmdbId + "/" + season + "/" + episode,
                "AutoEmbed (TMDB)"
            ));
            sources.add(new Source(
                "https://multiembed.mov/?video_id=" + tmdbId + "&tmdb=1&s=" + season + "&e=" + episode,
                "MultiEmbed (TMDB)"
            ));
        }

        return sources;
    }
}
