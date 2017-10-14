package br.com.sandclan.retrocollection.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import br.com.sandclan.retrocollection.R;
import br.com.sandclan.retrocollection.databasegame.GameDBUtil;


public class MainMenuActivity extends AppCompatActivity {
    private static final String WIDGET_UPDATE_ACTION = "android.appwidget.action.APPWIDGET_UPDATE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        Boolean gamesImported = prefs.getBoolean(GameDBUtil.GAMES_IMPORTED_SHARED_PREFERENCE_KEY,false);
        if(!gamesImported){
            GameDBUtil  gameDBUtil = new GameDBUtil(this);
            gameDBUtil.getGamesFromServer();
        }
        updateWidgets(this);
        findViewById(R.id.buttonManageCollection).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenuActivity.this, ListGamesActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.buttonSettings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenuActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });


        findViewById(R.id.buttonExit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        startAds();
    }


    private static void updateWidgets(Context context) {
        Intent dataUpdatedIntent = new Intent(WIDGET_UPDATE_ACTION)
                                .setPackage(context.getPackageName());
               context.sendBroadcast(dataUpdatedIntent);
    }

    private void startAds(){
        MobileAds.initialize(this, "FIREBASE_ADMOB_API_HERE");
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }
}
