package br.com.sandclan.retrocollection;

import br.com.sandclan.retrocollection.models.GamePlatform;
import retrofit2.Call;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GameServiceInterface {

    public static final String BASE_URL = "https://www.giantbomb.com/api/";
    public static final String GENESIS_ID = "6";
    public static final String GET_GAMES_FROM_GENESIS = "games?api_key=YOURAPI&platforms="+ GENESIS_ID + "&format=json&sort=name";
    public static final String GET_GAMES_FROM_GENESIS_BY_NAME = "games?api_key=YOURAPI&platforms="+ GENESIS_ID + "&format=json&sort=name";

    @GET(value = GET_GAMES_FROM_GENESIS)
    Call<GamePlatform> listGames(@Query("offset") int offset,@Query("limit") int limit);

    @GET(value = GET_GAMES_FROM_GENESIS_BY_NAME)
    Call<GamePlatform> listGamesByName(@Query("offset") int offset,@Query("limit") int limit,@Query(value = "filter=name:", encoded = true)  String name);


}
