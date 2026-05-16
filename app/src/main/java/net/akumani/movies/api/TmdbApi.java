package net.akumani.movies.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TmdbApi {

    @GET("movie/popular")
    Call<TmdbResponse> getPopularMovies(@Query("api_key") String key);

    @GET("tv/popular")
    Call<TmdbResponse> getPopularTv(@Query("api_key") String key);

    @GET("movie/{id}")
    Call<TmdbMovie> getMovieDetails(
        @Path("id") int id,
        @Query("api_key") String key,
        @Query("append_to_response") String append);

    @GET("tv/{id}")
    Call<TmdbSerie> getTvDetails(
        @Path("id") int id,
        @Query("api_key") String key,
        @Query("append_to_response") String append);

    @GET("search/movie")
    Call<TmdbResponse> searchMovies(
        @Query("api_key") String key,
        @Query("query") String query);

    @GET("search/tv")
    Call<TmdbResponse> searchTv(
        @Query("api_key") String key,
        @Query("query") String query);
}
