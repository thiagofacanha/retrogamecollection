package br.com.sandclan.retrocollection.data;


import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.com.sandclan.retrocollection.databasegame.GameDBUtil;
import br.com.sandclan.retrocollection.models.Game;
import br.com.sandclan.retrocollection.models.Image;

public class GameContentProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private GameDBHelper mOpenHelper;
    private static final int GAME = 100;
    public static final String AUTHORITY = "br.com.sandclan.retrocollection.data";
    public static final String sGameSelectionByID =
            GameContract.GameEntry.TABLE_NAME +
                    "." + GameContract.GameEntry._ID + " = ? ";


    public Cursor getGameByID(long id) {

        String[] selectionArgs;
        String selection;

        selection = sGameSelectionByID;
        selectionArgs = new String[]{String.valueOf(id)};

        return query(GameContract.GameEntry.CONTENT_URI,
                null,
                selection,
                selectionArgs,
                null
        );
    }


    public int deleteGameById(long id) {
        String[] selectionArgs;
        String selection;
        selection = sGameSelectionByID;
        selectionArgs = new String[]{String.valueOf(id)};
        return delete(GameContract.GameEntry.CONTENT_URI,
                selection,
                selectionArgs
        );
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new GameDBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor;
        switch (sUriMatcher.match(uri)) {
            case GAME: {
                cursor = mOpenHelper.getReadableDatabase().query(
                        GameContract.GameEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (getContext() != null) {
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
        }
        return cursor;
    }


    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case GAME:
                return GameContract.GameEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int returnCount = 0;
        switch (match) {
            case GAME:
                db.beginTransaction();
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(GameContract.GameEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;


            default:
                return super.bulkInsert(uri, values);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case GAME: {
                Cursor gameQuery = getGameByID((long) values.get(GameContract.GameEntry._ID));
                if (gameQuery.moveToFirst()) {
                    returnUri = GameContract.GameEntry.buildGameByID((long) values.get(GameContract.GameEntry._ID));
                    break;
                }
                gameQuery.close();
                long _id = db.insert(GameContract.GameEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = GameContract.GameEntry.buildGameByID(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (getContext() != null) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[]
            selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        if (null == selection) selection = "1";
        switch (match) {
            case GAME:
                rowsDeleted = db.delete(
                        GameContract.GameEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsDeleted != 0 && getContext() != null) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String
            selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;
        switch (match) {
            case GAME:
                rowsUpdated = db.update(GameContract.GameEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0 && getContext() != null) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }


    static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(GameContentProvider.AUTHORITY, GameContract.PATH_GAME, GAME);
        return matcher;
    }

    public static Game getGameFromCursor(Cursor data) {
        Game game = new Game();
        List<Image> images = new ArrayList<>();
        HashMap<String, String> frontCoverHash = new HashMap<>();
        frontCoverHash.put(GameDBUtil.FRONT, data.getString(data.getColumnIndex(GameContract.GameEntry.COLUMN_COVER_FRONT)));
        Image frontCover = new Image();
        frontCover.setBoxart(frontCoverHash);
        images.add(frontCover);
        game.setId(data.getInt(data.getColumnIndex(GameContract.GameEntry._ID)));
        game.setGameTitle(data.getString(data.getColumnIndex(GameContract.GameEntry.COLUMN_GAME_TITLE)));
        game.setOverview(data.getString(data.getColumnIndex(GameContract.GameEntry.COLUMN_DESCRIPTION)));
        game.setImages(images);
        return game;
    }
}
