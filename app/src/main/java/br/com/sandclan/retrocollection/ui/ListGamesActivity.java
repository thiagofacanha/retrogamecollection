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
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import br.com.sandclan.retrocollection.R;
import br.com.sandclan.retrocollection.adapter.GameAdapter;
import br.com.sandclan.retrocollection.data.GameContract;


public class ListGamesActivity extends AppCompatActivity implements android.app.LoaderManager.LoaderCallbacks<Cursor> {
    public static final int LOADER_ID = 172;
    private EditText searchText;
    private GameAdapter adapter;
    private Button searchButton;
    private RecyclerView gameListRecycleView;
    private int gameListPosition = 0;
    private String gameListSearch = "";
    private final String LIST_POSITION = "gameListPosition";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_games);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getString(R.string.app_name));
        }

        searchText = (EditText) findViewById(R.id.searchText);
        searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    listGames();
                }
                return false;
            }
        });

        getLoaderManager().initLoader(LOADER_ID, null, this);
        listGames();
        searchButton = (Button) findViewById(R.id.searchButton);
        gameListRecycleView = (RecyclerView) findViewById(R.id.gameRecycleView);
        DividerItemDecoration divider = new DividerItemDecoration(
                gameListRecycleView.getContext(),
                DividerItemDecoration.VERTICAL
        );
        gameListRecycleView.addItemDecoration(divider);
        gameListRecycleView.setItemAnimator(new DefaultItemAnimator());
        gameListRecycleView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new GameAdapter(ListGamesActivity.this);
        gameListRecycleView.setAdapter(adapter);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listGames();
            }
        });

        if (savedInstanceState != null){
            searchText.setText(gameListSearch);

            gameListRecycleView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    listGames();
                    gameListRecycleView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            gameListRecycleView.scrollToPosition(gameListPosition);
                            gameListPosition = 0;
                        }
                    }, 100);
                }
            }, 100);

        }

    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putInt(LIST_POSITION, ((LinearLayoutManager)gameListRecycleView.getLayoutManager()).findFirstCompletelyVisibleItemPosition());
        super.onSaveInstanceState(savedInstanceState);
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        gameListPosition = savedInstanceState.getInt(LIST_POSITION);
        gameListSearch = searchText.getText().toString();
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

        selection = GameContract.GameEntry.TABLE_NAME +
                "." + GameContract.GameEntry.COLUMN_GAME_TITLE + " LIKE ? ";
        selectionArgs = new String[]{queryArgument};




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
