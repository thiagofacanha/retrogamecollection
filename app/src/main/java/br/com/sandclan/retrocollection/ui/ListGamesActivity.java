package br.com.sandclan.retrocollection.ui;

import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.sandclan.retrocollection.GameServiceInterface;
import br.com.sandclan.retrocollection.R;
import br.com.sandclan.retrocollection.adapter.GameAdapter;
import br.com.sandclan.retrocollection.models.Game;
import br.com.sandclan.retrocollection.models.GamePlatform;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ListGamesActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private List<Game> games = new ArrayList<>();
    private EditText searchText;
    private GameAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_games);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        searchText = (EditText) findViewById(R.id.searchText);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        final RecyclerView gameListRecycleView = (RecyclerView) findViewById(R.id.gameRecycleView);
        adapter = new GameAdapter(ListGamesActivity.this, games);
        gameListRecycleView.setAdapter(adapter);
        gameListRecycleView.setLayoutManager(new LinearLayoutManager(this));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  listGamesByNames(adapter);
            }
        });


    }

    public void listGamesByNames(View view) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(GameServiceInterface.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();

        GameServiceInterface service = retrofit.create(GameServiceInterface.class);
        final Call<GamePlatform> requestGames = service.listGamesByName(0, 100, searchText.getText().toString());
        requestGames.enqueue(new Callback<GamePlatform>() {
            @Override
            public void onResponse(Call<GamePlatform> call, Response<GamePlatform> response) {
                if (response.isSuccessful()) {
                    games.clear();
                    games.addAll(response.body().getGames());
                    adapter.notifyDataSetChanged();
                } else {
                    Snackbar.make(null, "Replace with your own action", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }

            @Override
            public void onFailure(Call<GamePlatform> call, Throwable t) {
                Snackbar.make(null, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
