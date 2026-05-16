package net.akumani.movies;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import net.akumani.movies.api.TmdbClient;
import net.akumani.movies.api.TmdbResponse;
import net.akumani.movies.api.TmdbMovie;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recycler = findViewById(R.id.recycler);

        Call<TmdbResponse> call = TmdbClient.getApi().getPopularMovies(TmdbClient.API_KEY);
        call.enqueue(new Callback<TmdbResponse>() {
            @Override
            public void onResponse(Call<TmdbResponse> call, Response<TmdbResponse> resp) {
                if (resp.isSuccessful() && resp.body() != null) {
                    Log.d(TAG, "Got " + resp.body().results.size() + " movies.");
                    // TODO: bind to RecyclerView adapter
                }
            }

            @Override
            public void onFailure(Call<TmdbResponse> call, Throwable t) {
                Log.e(TAG, "Failed to fetch movies", t);
            }
        });
    }
}
