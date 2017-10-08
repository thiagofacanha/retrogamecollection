package br.com.sandclan.retrocollection.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import br.com.sandclan.retrocollection.GameServiceInterface;
import br.com.sandclan.retrocollection.R;
import br.com.sandclan.retrocollection.models.GamePlatform;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        updateWidgets(this);
        findViewById(R.id.buttonManageCollection).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenuActivity.this, ListGamesActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.buttonExit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private static void updateWidgets(Context context) {
                       Intent dataUpdatedIntent = new Intent("android.appwidget.action.APPWIDGET_UPDATE")
                                .setPackage(context.getPackageName());
               context.sendBroadcast(dataUpdatedIntent);
    }
}
