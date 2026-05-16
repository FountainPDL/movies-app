package net.akumani.movies.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TmdbClient {
    public static final String API_KEY = "8baba8ab6b8bbe247645bcae7df63d0d";
    public static final String BASE_URL = "https://api.themoviedb.org/3/";

    private static TmdbApi api;

    public static TmdbApi getApi() {
        if (api == null) {
            Retrofit r = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
            api = r.create(TmdbApi.class);
        }
        return api;
    }
}
