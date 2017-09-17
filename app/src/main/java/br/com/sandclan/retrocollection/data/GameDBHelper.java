package br.com.sandclan.retrocollection.data;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

 class GameDBHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "games.db";

     GameDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_GAME_TABLE = "CREATE TABLE " + GameContract.GameEntry.TABLE_NAME + " (" +
                GameContract.GameEntry._ID + " INTEGER PRIMARY KEY," +
                GameContract.GameEntry.COLUMN_GAME_TITLE + " TEXT NOT NULL, " +
                GameContract.GameEntry.COLUMN_RELEASE_DATE + " TEXT , " +
                GameContract.GameEntry.COLUMN_LOGO + " TEXT, " +
                GameContract.GameEntry.COLUMN_COVER_FRONT + " TEXT, " +
                GameContract.GameEntry.COLUMN_COVER_BACK + " TEXT, " +
                GameContract.GameEntry.COLUMN_DESCRIPTION + " TEXT, " +
                GameContract.GameEntry.COLUMN_PLAYERS + " INTEGER, " +
                GameContract.GameEntry.COLUMN_COOP + " TEXT, " +
                GameContract.GameEntry.COLUMN_GENRE + " TEXT, " +
                GameContract.GameEntry.COLUMN_DEVELOPER + " TEXT, " +
                GameContract.GameEntry.COLUMN_PUBLISHER + " TEXT, " +
                GameContract.GameEntry.COLUMN_RATING + " REAL, " +
                GameContract.GameEntry.COLUMN_TRAILER + " TEXT, " +
                GameContract.GameEntry.COLUMN_OWNED + " INTEGER, " +
                GameContract.GameEntry.COLUMN_WISH_LIST + " INTEGER " +
                " );";
        db.execSQL(SQL_CREATE_GAME_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + GameContract.GameEntry.TABLE_NAME);
        onCreate(db);
    }
}
