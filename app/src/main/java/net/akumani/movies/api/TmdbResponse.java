package net.akumani.movies.api;

import java.util.List;

public class TmdbResponse {
    public int page;
    public int total_results;
    public int total_pages;
    public List<TmdbMovie> results;
}
