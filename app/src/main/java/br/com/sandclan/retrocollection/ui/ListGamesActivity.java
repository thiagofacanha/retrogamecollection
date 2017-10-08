package br.com.sandclan.retrocollection.ui;

import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import br.com.sandclan.retrocollection.R;
import br.com.sandclan.retrocollection.adapter.GameAdapter;
import br.com.sandclan.retrocollection.data.GameContract;
import br.com.sandclan.retrocollection.databasegame.GameDBUtil;
import br.com.sandclan.retrocollection.models.Game;


public class ListGamesActivity extends AppCompatActivity implements android.app.LoaderManager.LoaderCallbacks<Cursor> {
    public static final int LOADER_ID = 172;
    private List<Game> games = new ArrayList<>();
    private EditText searchText;
    private GameAdapter adapter;
    private Button searchButton;
    private RecyclerView gameListRecycleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GameDBUtil gdbu = new GameDBUtil(this);
        setContentView(R.layout.activity_list_games);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        searchText = (EditText) findViewById(R.id.searchText);
        getLoaderManager().initLoader(LOADER_ID, null, this);
        listGames();
        searchButton = (Button) findViewById(R.id.searchButton);
        gameListRecycleView = (RecyclerView) findViewById(R.id.gameRecycleView);
        DividerItemDecoration divider = new DividerItemDecoration(
                gameListRecycleView.getContext(),
                DividerItemDecoration.VERTICAL
        );
        gameListRecycleView.addItemDecoration(divider);
        adapter = new GameAdapter(ListGamesActivity.this);
        gameListRecycleView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        gameListRecycleView.setItemAnimator(new DefaultItemAnimator());

        gameListRecycleView.setAdapter(adapter);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listGames();
            }
        });
    }

    private void listGames() {
        getLoaderManager().restartLoader(LOADER_ID,null,this);
    }




    @Override
    public android.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {


        Uri gamesURI = GameContract.GameEntry.CONTENT_URI;
        String selection;
        String[] selectionArgs;
        String queryArgument = "%".concat(searchText.getText().toString()).concat("%");

            selection =  GameContract.GameEntry.TABLE_NAME +
                    "." + GameContract.GameEntry.COLUMN_GAME_TITLE + " LIKE ? ";
            selectionArgs= new String[]{queryArgument};




        return new CursorLoader(ListGamesActivity.this,
                gamesURI,
                null,
                selection,
                selectionArgs,
                GameContract.GameEntry.COLUMN_GAME_TITLE);
    }

    @Override
    public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(android.content.Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }

}
