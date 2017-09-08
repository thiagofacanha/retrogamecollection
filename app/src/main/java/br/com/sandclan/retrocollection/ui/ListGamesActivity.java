package br.com.sandclan.retrocollection.ui;

import android.app.ProgressDialog;
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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.XMLFormatter;

import br.com.sandclan.retrocollection.GameServiceInterface;
import br.com.sandclan.retrocollection.R;
import br.com.sandclan.retrocollection.adapter.GameAdapter;
import br.com.sandclan.retrocollection.models.Game;
import br.com.sandclan.retrocollection.models.GamePlatform;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.SimpleXmlConverterFactory;

import static br.com.sandclan.retrocollection.GameServiceInterface.httpTimeoutClient;


public class ListGamesActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private List<Game> games = new ArrayList<>();
    private EditText searchText;
    private GameAdapter adapter;
    private Retrofit retrofit;
    private GameServiceInterface service;
    private ProgressDialog progressDialogLoading;
    private Button searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_games);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        searchText = (EditText) findViewById(R.id.searchText);
        searchButton = (Button) findViewById(R.id.searchButton);
        final RecyclerView gameListRecycleView = (RecyclerView) findViewById(R.id.gameRecycleView);
        adapter = new GameAdapter(ListGamesActivity.this, games);
        gameListRecycleView.setAdapter(adapter);
        gameListRecycleView.setLayoutManager(new LinearLayoutManager(this));

        retrofit = new Retrofit.Builder().client(httpTimeoutClient).baseUrl(GameServiceInterface.THEGAMEDB_BASE_URL).addConverterFactory(SimpleXmlConverterFactory.create()).build();

        service = retrofit.create(GameServiceInterface.class);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listGames(searchText.getText().toString());
            }
        });
    }

    public void listGames(String name) {
        showLoadingDialog();
        final Call<GamePlatform> requestGames;
        if (name == null || name.isEmpty()) {
            requestGames = service.listGamesByPlatform(GameServiceInterface.MD_GAMES);
        } else {
            requestGames = service.listGamesByName(name);

        }
        requestGames.enqueue(new Callback<GamePlatform>() {
            @Override
            public void onResponse(Call<GamePlatform> call, Response<GamePlatform> response) {
                if (response.isSuccessful()) {
                    games.clear();
                    if (response.body().getGames() != null) {
                        games.addAll(response.body().getGames());
                        adapter.notifyDataSetChanged();
                    }
                }
                dismissLoadingDialog();
            }

            @Override
            public void onFailure(Call<GamePlatform> call, Throwable t) {
                if (t instanceof SocketTimeoutException)
                    Toast.makeText(ListGamesActivity.this, R.string.timeout_error, Toast.LENGTH_SHORT).show();
                dismissLoadingDialog();
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

    private void showLoadingDialog() {
        if (progressDialogLoading != null) {
            progressDialogLoading.setMessage("loading");
            progressDialogLoading.show();
        } else {
            progressDialogLoading = new ProgressDialog(this);
            showLoadingDialog();
        }
    }

    private void dismissLoadingDialog() {
        if (progressDialogLoading != null && progressDialogLoading.isShowing()) {
            progressDialogLoading.dismiss();
        }
    }
}
