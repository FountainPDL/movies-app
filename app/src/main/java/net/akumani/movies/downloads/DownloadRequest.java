package net.akumani.movies.downloads;

public class DownloadRequest implements java.io.Serializable {

    public final int tmdbId;
    public final String type;
    public final Integer season;
    public final Integer episode;
    public final String sourceUrl;

    public DownloadRequest(int tmdbId, String type, Integer season, Integer episode, String sourceUrl) {
        this.tmdbId = tmdbId;
        this.type = type;
        this.season = season;
        this.episode = episode;
        this.sourceUrl = sourceUrl;
    }
}
