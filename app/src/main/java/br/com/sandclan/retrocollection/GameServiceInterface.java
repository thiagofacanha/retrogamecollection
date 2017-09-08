package br.com.sandclan.retrocollection;

import java.util.concurrent.TimeUnit;

import br.com.sandclan.retrocollection.models.Game;
import br.com.sandclan.retrocollection.models.GamePlatform;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GameServiceInterface {
    public static final int GENESIS_ID = 18;
    public static final int MD_GAMES = 36;
    public static final String THEGAMEDB_BASE_URL = "http://thegamesdb.net/api/";
    public static final String THEGAMEDB_GET_GAMES_FROM_PLATFORM= "GetPlatformGames.php";
    public static final String THEGAMEDB_GET_GAMES_FROM_GENESIS = "GetGame.php?platform=Sega%20Genesis";
    public static final String THEGAMEDB_GET_GAME_FROM_GENESIS_BY_ID = "GetGame.php?platform=Sega%20Genesis";
    public static OkHttpClient httpTimeoutClient = new OkHttpClient.Builder()
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
