package net.akumani.movies.api;

public class TmdbSerie {
    public int id;
    public String name;
    public String poster_path;
    public String backdrop_path;
    public double vote_average;
    public String overview;
    public String first_air_date;
    public Integer imdb_id;
    public Season[] seasons;

    public static class Season {
        public int season_number;
        public int episode_count;
    }
}
