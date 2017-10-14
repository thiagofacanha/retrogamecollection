package br.com.sandclan.retrocollection.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.analytics.FirebaseAnalytics;

import br.com.sandclan.retrocollection.R;
import br.com.sandclan.retrocollection.models.Game;

import static br.com.sandclan.retrocollection.GameServiceInterface.THEGAMEDB_BASE_IMAGE_URL;
import static com.google.firebase.analytics.FirebaseAnalytics.Event.SELECT_CONTENT;

public class GameDetailActivity extends AppCompatActivity {
    public static final String GAME_EXTRA = "gameExtra";
    private Game game;
    private ImageView logo;
    private FirebaseAnalytics mFirebaseAnalytics;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        Bundle extras = getIntent().getExtras();

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();
        if (extras != null) {
            game = (Game) extras.getSerializable(GAME_EXTRA);
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, game.getGameTitle());
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "GAME SHOWED");
            mFirebaseAnalytics.logEvent(SELECT_CONTENT, bundle);
            if(getSupportActionBar() != null){
                getSupportActionBar().setTitle(game.getGameTitle());
            }
        }else{
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "ERROR");
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "GAME NOT FOUND");
            mFirebaseAnalytics.logEvent(SELECT_CONTENT, bundle);
            finish();
        }
        logo = (ImageView) findViewById(R.id.gameFrontCover);
        ((TextView) findViewById(R.id.overview)).setText(game.getOverview());
        if (game.getImages() != null) {
            Glide.with(GameDetailActivity.this).load(THEGAMEDB_BASE_IMAGE_URL.concat(game.getImages().get(0).getBoxart().get("front"))).into(logo);
        }
        startAds();
    }

    private void startAds(){
        MobileAds.initialize(this, "1:1047712399571:android:ace8b8afdf409edc");
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }


}
