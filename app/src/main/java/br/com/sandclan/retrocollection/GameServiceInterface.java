package br.com.sandclan.retrocollection;

import br.com.sandclan.retrocollection.models.GamePlatform;
import retrofit2.Call;
import retrofit2.http.GET;

public interface GameServiceInterface {

    public static final String BASE_URL = "https://www.giantbomb.com/api/";
    public static final String GET_GAMES_FROM_GENESIS = "games?api_key=APIKEY&platforms=6&format=json&limit=30&offset=0";

    @GET(GET_GAMES_FROM_GENESIS)
    Call<GamePlatform> listGames();


}
