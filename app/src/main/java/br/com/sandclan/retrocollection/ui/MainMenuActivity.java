package br.com.sandclan.retrocollection.ui;

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
import retrofit2.converter.gson.GsonConverterFactory;

public class MainMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

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

        Retrofit retrofit = new Retrofit.Builder().baseUrl(GameServiceInterface.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();

        GameServiceInterface service = retrofit.create(GameServiceInterface.class);
        final Call<GamePlatform> requestGames = service.listGames();

        requestGames.enqueue(new Callback<GamePlatform>() {
            @Override
            public void onResponse(Call<GamePlatform> call, Response<GamePlatform> response) {
                Toast.makeText(MainMenuActivity.this,"Ok",Toast.LENGTH_SHORT).show();
                if(response.isSuccessful()){
                    GamePlatform platform = response.body();
                    GamePlatform platform2 = response.body();
                }
            }

            @Override
            public void onFailure(Call<GamePlatform> call, Throwable t) {
                Toast.makeText(MainMenuActivity.this,"NOk",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
