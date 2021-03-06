package br.com.sandclan.retrocollection;

import java.util.concurrent.TimeUnit;

import br.com.sandclan.retrocollection.models.GamePlatform;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GameServiceInterface {
    int GENESIS_ID = 18;
    int MD_GAMES = 36;
    String THEGAMEDB_BASE_IMAGE_URL = "http://thegamesdb.net/banners/";
    String THEGAMEDB_BASE_URL = "http://thegamesdb.net/api/";
    String THEGAMEDB_GET_GAMES_FROM_PLATFORM= "GetPlatformGames.php";
    String THEGAMEDB_GET_GAMES_FROM_GENESIS = "GetGame.php?platform=Sega%20Genesis";
    String THEGAMEDB_GET_GAME_FROM_GENESIS_BY_ID = "GetGame.php?platform=Sega%20Genesis";
    OkHttpClient httpTimeoutClient = new OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .build();

    @GET(value = THEGAMEDB_GET_GAMES_FROM_PLATFORM)
    Call<GamePlatform> listGamesByPlatform(@Query("platform") int platform);

    @GET(value = THEGAMEDB_GET_GAMES_FROM_GENESIS)
    Call<GamePlatform> listGamesByName(@Query("name") String name);

    @GET(value = THEGAMEDB_GET_GAMES_FROM_GENESIS)
    Call<GamePlatform> getGameById(@Query("id") long id);

}
