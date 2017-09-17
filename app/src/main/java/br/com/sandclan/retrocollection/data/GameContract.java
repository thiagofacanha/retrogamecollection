package br.com.sandclan.retrocollection.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

 public class GameContract {
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + GameContentProvider.AUTHORITY);
    static final String PATH_GAME = "game";

    public static final class GameEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_GAME).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + GameContentProvider.AUTHORITY + "/" + PATH_GAME;

        public  static final String TABLE_NAME = "games";

        public  static final String COLUMN_GAME_TITLE = "GameTitle";
        public  static final String COLUMN_RELEASE_DATE = "release_date";
        public  static final String COLUMN_LOGO = "logo";
        public  static final String COLUMN_COVER_FRONT = "cover_front";
        public  static final String COLUMN_COVER_BACK = "cover_back";
        public  static final String COLUMN_DESCRIPTION = "description";
        public  static final String COLUMN_PLAYERS = "players";
        public  static final String COLUMN_COOP = "coop";
        public  static final String COLUMN_GENRE= "genre";
        public  static final String COLUMN_DEVELOPER= "developer";
        public  static final String COLUMN_PUBLISHER= "publisher";
        public  static final String COLUMN_RATING= "rating";
        public  static final String COLUMN_TRAILER= "trailer";
        public  static final String COLUMN_OWNED= "owned";
        public  static final String COLUMN_WISH_LIST= "wish_list";

         static Uri buildGameByID(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
