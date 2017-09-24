package br.com.sandclan.retrocollection.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import br.com.sandclan.retrocollection.GameServiceInterface;
import br.com.sandclan.retrocollection.Listener.InterceptorTouchListener;
import br.com.sandclan.retrocollection.R;
import br.com.sandclan.retrocollection.adapter.GameAdapter;
import br.com.sandclan.retrocollection.data.GameContract;
import br.com.sandclan.retrocollection.models.Game;
import br.com.sandclan.retrocollection.models.GamePlatform;
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
    private RecyclerView gameListRecycleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_games);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        searchText = (EditText) findViewById(R.id.searchText);
        listGames(searchText.getText().toString());
        searchButton = (Button) findViewById(R.id.searchButton);
        gameListRecycleView = (RecyclerView) findViewById(R.id.gameRecycleView);
        DividerItemDecoration divider = new DividerItemDecoration(
                gameListRecycleView.getContext(),
                DividerItemDecoration.VERTICAL
        );
        gameListRecycleView.addItemDecoration(divider);
        adapter = new GameAdapter(ListGamesActivity.this, games);
        gameListRecycleView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        gameListRecycleView.setItemAnimator(new DefaultItemAnimator());

        gameListRecycleView.setAdapter(adapter);
        gameListRecycleView.addOnItemTouchListener(new InterceptorTouchListener(getApplicationContext(), new InterceptorTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                if (position >= 0) {
                    Game game = games.get(position);
                    openGameDetailActivity(game);
                }
            }
        }));
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listGames(searchText.getText().toString());
            }
        });
    }

    private void openGameDetailActivity(Game gameExtra) {
        Intent gameDetailIntent = new Intent(this, GameDetailActivity.class);
        gameDetailIntent.putExtra("gameExtra", gameExtra);
        this.startActivity(gameDetailIntent);
    }

    public void listGames(String name) {
        retrofit = new Retrofit.Builder().client(httpTimeoutClient).baseUrl(GameServiceInterface.THEGAMEDB_BASE_URL).addConverterFactory(SimpleXmlConverterFactory.create()).build();

        service = retrofit.create(GameServiceInterface.class);
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
                } else if (response.code() == 503) {
                    Toast.makeText(ListGamesActivity.this, "Service Unavailable. Try again later.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ListGamesActivity.this, "Error accessing server. Try again later.", Toast.LENGTH_SHORT).show();

                }
                dismissLoadingDialog();
                Collections.sort(games, new Comparator<Game>() {
                    @Override
                    public int compare(final Game gameLeft, final Game gameRight) {
                        return gameLeft.getGameTitle().compareTo(gameRight.getGameTitle());
                    }
                });
                setAllGameDetails(games);
            }

            @Override
            public void onFailure(Call<GamePlatform> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(ListGamesActivity.this, R.string.timeout_error, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ListGamesActivity.this, "Error accessing server. Try again later.", Toast.LENGTH_SHORT).show();

                }
                dismissLoadingDialog();
            }
        });
    }

    private void setAllGameDetails(List<Game> games) {
        for (Game game : games) {
            getGameDetail(game.getId());
        }

    }

    private void setGameDetail(Game game) {
        Cursor checkExistenceCursor = getContentResolver().query(GameContract.GameEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        List<Long> alreadSavedList = new ArrayList<>();
        if (checkExistenceCursor != null) {
            while (checkExistenceCursor.moveToNext()) {
                alreadSavedList.add(checkExistenceCursor.getLong(checkExistenceCursor.getColumnIndex(GameContract.GameEntry._ID)));
            }
        }
        checkExistenceCursor.close();
        for (Game tempGame : games) {
            if (tempGame.getId() == (game.getId())) {
                tempGame.setImages(game.getImages());
                tempGame.setOverview(game.getOverview());
                tempGame.setFavourite(alreadSavedList.contains(tempGame.getId()));
            }
        }
        adapter.notifyDataSetChanged();
    }


    public void getGameDetail(long gameID) {
        retrofit = new Retrofit.Builder().client(httpTimeoutClient).baseUrl(GameServiceInterface.THEGAMEDB_BASE_URL).addConverterFactory(SimpleXmlConverterFactory.create()).build();
        service = retrofit.create(GameServiceInterface.class);
        showLoadingDialog();
        final Call<GamePlatform> gameRequest = service.getGameById(gameID);

        gameRequest.enqueue(new Callback<GamePlatform>() {


            @Override
            public void onResponse(Call<GamePlatform> call, Response<GamePlatform> response) {
                if (response.isSuccessful()) {
                    Game game = response.body().getGames().get(0);
                    if (game != null) {
                        setGameDetail(game);
                    }
                }
                dismissLoadingDialog();
            }

            @Override
            public void onFailure(Call<GamePlatform> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(ListGamesActivity.this, R.string.timeout_error, Toast.LENGTH_SHORT).show();
                }
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
