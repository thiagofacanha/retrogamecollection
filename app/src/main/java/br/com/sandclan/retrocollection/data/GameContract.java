package br.com.sandclan.retrocollection.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

 class GameContract {
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + GameContentProvider.AUTHORITY);
    static final String PATH_GAME = "game";

    static final class GameEntry implements BaseColumns {
        static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_GAME).build();

        static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + GameContentProvider.AUTHORITY + "/" + PATH_GAME;

         static final String TABLE_NAME = "games";

         static final String COLUMN_ID_FROM_MOVIEDBAPI = "idapi";
         static final String COLUMN_GAME_TITLE = "GameTitle";
         static final String COLUMN_RELEASE_DATE = "release_date";
         static final String COLUMN_LOGO = "logo";
         static final String COLUMN_COVER_FRONT = "cover_front";
         static final String COLUMN_COVER_BACK = "cover_back";
         static final String COLUMN_DESCRIPTION = "description";
         static final String COLUMN_PLAYERS = "players";
         static final String COLUMN_COOP = "coop";
         static final String COLUMN_GENRE= "genre";
         static final String COLUMN_DEVELOPER= "developer";
         static final String COLUMN_PUBLISHER= "publisher";
         static final String COLUMN_RATING= "rating";
         static final String COLUMN_TRAILER= "trailer";
         static final String COLUMN_OWNED= "rating";
         static final String COLUMN_WISHLIST= "rating";

         static Uri buildGameByID(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }


        public static String getIdFromGame(Uri uri) {
            return uri.getPathSegments().get(0);
        }
    }
}
