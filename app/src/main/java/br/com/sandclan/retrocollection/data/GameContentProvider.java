package br.com.sandclan.retrocollection.data;


import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class GameContentProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private GameDBHelper mOpenHelper;
    private static final int GAME = 100;
    public static final String AUTHORITY = "br.com.sandclan.retrocollection.data";
    private static final SQLiteQueryBuilder sGameQueryBuilder = new SQLiteQueryBuilder();
    private static final String sGameSelectionByID =
            GameContract.GameEntry.TABLE_NAME +
                    "." + GameContract.GameEntry._ID + " = ? ";


    private Cursor getGameByID(Uri uri, String[] projection, String sortOrder) {
        String gameId = GameContract.GameEntry.getIdFromGame(uri);

        String[] selectionArgs;
        String selection;

        selection = sGameSelectionByID;
        selectionArgs = new String[]{gameId};

        return sGameQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
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

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case GAME: {
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
}
