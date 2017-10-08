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
import retrofit2.Retrofit;

import static br.com.sandclan.retrocollection.GameServiceInterface.THEGAMEDB_BASE_IMAGE_URL;

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
            game = (Game) extras.getSerializable("gameExtra");
        }else{
            finish();
        }
        logo = (ImageView) findViewById(R.id.gameFrontCover);
        ((TextView) findViewById(R.id.overview)).setText(game.getOverview());
        if (game.getImages() != null) {
            Glide.with(GameDetailActivity.this).load(THEGAMEDB_BASE_IMAGE_URL.concat(game.getImages().get(0).getBoxart().get("front"))).into(logo);
        }

    }


}
