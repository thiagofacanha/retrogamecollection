package br.com.sandclan.retrocollection.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import br.com.sandclan.retrocollection.GameServiceInterface;
import br.com.sandclan.retrocollection.R;
import br.com.sandclan.retrocollection.models.Game;
import br.com.sandclan.retrocollection.models.GamePlatform;
import br.com.sandclan.retrocollection.models.Image;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.SimpleXmlConverterFactory;

public class GameDetailActivity extends AppCompatActivity {
    private Game game;
    private Retrofit retrofit;
    private GameServiceInterface service;
    private ProgressDialog progressDialogLoading;
    private ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Bundle extras = getIntent().getExtras();


        if (extras != null) {
            setGameByID(extras.getLong("gameId"));
        }
        logo = (ImageView) findViewById(R.id.gameFrontCover);

    }

    private void setGameByID(final long gameID) {
        retrofit = new Retrofit.Builder().baseUrl(GameServiceInterface.THEGAMEDB_BASE_URL).addConverterFactory(SimpleXmlConverterFactory.create()).build();
        service = retrofit.create(GameServiceInterface.class);
        showLoadingDialog();
        final Call<GamePlatform> gameRequest = service.getGameById(gameID);

        gameRequest.enqueue(new Callback<GamePlatform>() {


            @Override
            public void onResponse(Call<GamePlatform> call, Response<GamePlatform> response) {
                if (response.isSuccessful()) {
                    game = response.body().getGames().get(0);
                    if (game != null) {
                        ((TextView) findViewById(R.id.overview)).setText(game.getOverview());
                        Glide.with(GameDetailActivity.this).load(response.body().getBaseImgUrl() + game.getImages().get(0).getBoxart().get("front")).into(logo);
                    }
                }
                dismissLoadingDialog();
            }

            @Override
            public void onFailure(Call<GamePlatform> call, Throwable t) {
                dismissLoadingDialog();
            }
        });

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
