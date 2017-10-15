package br.com.sandclan.retrocollection.databasegame;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.widget.Toast;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import br.com.sandclan.retrocollection.GameServiceInterface;
import br.com.sandclan.retrocollection.R;
import br.com.sandclan.retrocollection.data.GameContract;
import br.com.sandclan.retrocollection.models.Game;
import br.com.sandclan.retrocollection.models.GamePlatform;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.SimpleXmlConverterFactory;

import static br.com.sandclan.retrocollection.GameServiceInterface.httpTimeoutClient;

public class GameDBUtil {
    public static final String FRONT = "front";
    private Retrofit retrofit;
    private GameServiceInterface service;
    private ProgressDialog progressDialogLoading;
    private List<Game> games = new ArrayList<>();
    private final int SERVICE_UNAVAILABLE = 503;
    private Context context;
    public Call<GamePlatform> requestGames;
    public static final String GAMES_IMPORTED_SHARED_PREFERENCE_KEY = "gamesImported";

    public GameDBUtil(Context context) {
        this.context = context;
    }

    public void getGamesFromServer() {
        context.getContentResolver().delete(GameContract.GameEntry.CONTENT_URI, null, null);
        retrofit = new Retrofit.Builder().client(httpTimeoutClient).baseUrl(GameServiceInterface.THEGAMEDB_BASE_URL).addConverterFactory(SimpleXmlConverterFactory.create()).build();
        service = retrofit.create(GameServiceInterface.class);
        showLoadingDialog();

        ServiceTask serviceTask = new ServiceTask();
        serviceTask.execute();


    }

    private void setAllGameDetails(List<Game> games) {
        for (Game game : games) {
            getGameDetail(game.getId());
        }

    }

    private void setGameDetail(Game game) {
        boolean found = false;
        for (Game tempGame : games) {
            if (tempGame.getId() == (game.getId())) {
                found = true;
                tempGame.setImages(game.getImages());
                tempGame.setOverview(game.getOverview());
            }
        }
        if(!found){
            return;
        }
        ContentValues value = new ContentValues();
        value.put(GameContract.GameEntry._ID, game.getId());
        value.put(GameContract.GameEntry.COLUMN_GAME_TITLE, game.getGameTitle());
        value.put(GameContract.GameEntry.COLUMN_COVER_FRONT, game.getImages().get(0).getBoxart().get(FRONT));
        value.put(GameContract.GameEntry.COLUMN_DESCRIPTION, game.getOverview());
        context.getContentResolver().insert(GameContract.GameEntry.CONTENT_URI, value);

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
            }

            @Override
            public void onFailure(Call<GamePlatform> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(context, R.string.timeout_error, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void showLoadingDialog() {
        if (progressDialogLoading != null) {
            progressDialogLoading.setMessage(context.getString(R.string.loading));
            progressDialogLoading.setCancelable(false);
            progressDialogLoading.show();
        } else {
            progressDialogLoading = new ProgressDialog(context);
            showLoadingDialog();
        }
    }

    private void dismissLoadingDialog() {
        if (progressDialogLoading != null && progressDialogLoading.isShowing()) {
            progressDialogLoading.dismiss();
        }
    }

    public class ServiceTask extends AsyncTask<Void,Void,Void> {
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        @Override
        protected Void doInBackground(Void... params) {
            requestGames = service.listGamesByPlatform(GameServiceInterface.GENESIS_ID);
            requestGames.enqueue(new Callback<GamePlatform>() {
                @Override
                public void onResponse(Call<GamePlatform> call, Response<GamePlatform> response) {
                    if (response.isSuccessful()) {
                        games.clear();
                        if (response.body().getGames() != null) {
                            games.addAll(response.body().getGames());
                            Collections.sort(games, new Comparator<Game>() {
                                @Override
                                public int compare(final Game gameLeft, final Game gameRight) {
                                    return gameLeft.getGameTitle().compareTo(gameRight.getGameTitle());
                                }
                            });
                            setAllGameDetails(games);
                        }
                        prefs.edit().putBoolean(GAMES_IMPORTED_SHARED_PREFERENCE_KEY, true).apply();
                        Toast.makeText(context, R.string.games_imported_success, Toast.LENGTH_SHORT).show();
                    } else if (response.code() == SERVICE_UNAVAILABLE) {
                        prefs.edit().putBoolean(GAMES_IMPORTED_SHARED_PREFERENCE_KEY, false).apply();
                        Toast.makeText(context, R.string.service_unavailable, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, R.string.error_access_server, Toast.LENGTH_SHORT).show();
                        prefs.edit().putBoolean(GAMES_IMPORTED_SHARED_PREFERENCE_KEY, false).apply();
                    }

                }

                @Override
                public void onFailure(Call<GamePlatform> call, Throwable t) {
                    if (t instanceof SocketTimeoutException) {
                        Toast.makeText(context, R.string.timeout_error, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, R.string.error_access_server, Toast.LENGTH_SHORT).show();

                    }
                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dismissLoadingDialog();
        }
    }
}
