package br.com.sandclan.retrocollection.widget;


import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Binder;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;

import java.util.concurrent.ExecutionException;

import br.com.sandclan.retrocollection.R;
import br.com.sandclan.retrocollection.data.GameContract;
import br.com.sandclan.retrocollection.ui.GameDetailActivity;

import static br.com.sandclan.retrocollection.GameServiceInterface.THEGAMEDB_BASE_IMAGE_URL;
import static br.com.sandclan.retrocollection.data.GameContentProvider.getGameFromCursor;
import static br.com.sandclan.retrocollection.ui.GameDetailActivity.GAME_EXTRA;

public class WidgetService extends RemoteViewsService {

    private static final String TAG = WidgetService.class.getSimpleName();
    public static final String RANDOM_LIMIT_1 = " RANDOM() LIMIT 1";

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteViewsFactory() {
            private Cursor data = null;

            @Override
            public void onCreate() {
            }

            @Override
            public void onDataSetChanged() {
                if (data != null) {
                    data.close();
                }
                final long identityToken = Binder.clearCallingIdentity();
                Uri gameURI = GameContract.GameEntry.CONTENT_URI;
                data = getContentResolver().query(gameURI,
                        null,
                        null,
                        null,
                        RANDOM_LIMIT_1);
                Binder.restoreCallingIdentity(identityToken);
            }

            @Override
            public void onDestroy() {
                if (data != null) {
                    data.close();
                    data = null;
                }
            }

            @Override
            public int getCount() {
                return data == null ? 0 : data.getCount();
            }

            @Override
            public RemoteViews getViewAt(int position) {
                if (position == AdapterView.INVALID_POSITION ||
                        data == null || !data.moveToPosition(position)) {
                    return null;
                }
                RemoteViews views = new RemoteViews(getPackageName(),
                        R.layout.widget_layout_item);
                Bitmap bitmap = null;
                try {
                    bitmap = Glide.with(getApplicationContext())
                            .asBitmap()
                            .load(THEGAMEDB_BASE_IMAGE_URL.concat(data.getString(data.getColumnIndex(GameContract.GameEntry.COLUMN_COVER_FRONT))))
                            .submit(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                            .get();
                } catch (InterruptedException e) {
                    Log.d(TAG,"Error using Glide.");
                } catch (ExecutionException e) {
                    Log.d(TAG,"Error using Glide.");
                }
                views.setImageViewBitmap(R.id.gameCoverWidget, bitmap);


                final Intent detailIntent = new Intent(getApplicationContext(), GameDetailActivity.class);
                detailIntent.putExtra(GAME_EXTRA, getGameFromCursor(data));
                views.setOnClickFillInIntent(R.id.widget_item, detailIntent);
                return views;
            }


            @Override
            public RemoteViews getLoadingView() {
                return new RemoteViews(getPackageName(), R.layout.widget_layout_item);
            }

            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public long getItemId(int position) {
                if (data.moveToPosition(position))
                    return data.getLong(0);
                return position;
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }
        };
    }


}