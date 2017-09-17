package br.com.sandclan.retrocollection.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

import br.com.sandclan.retrocollection.R;
import br.com.sandclan.retrocollection.data.GameContentProvider;
import br.com.sandclan.retrocollection.data.GameContract;
import br.com.sandclan.retrocollection.models.Game;
import br.com.sandclan.retrocollection.models.Image;
import br.com.sandclan.retrocollection.ui.GameDetailActivity;

import static br.com.sandclan.retrocollection.GameServiceInterface.THEGAMEDB_BASE_IMAGE_URL;
import static br.com.sandclan.retrocollection.data.GameContentProvider.sGameSelectionByID;

public class GameAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static List<Game> games;
    private Context context;

    public GameAdapter(Context context, List<Game> games) {
        this.context = context;
        this.games = games;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewGroup itemLayout = (ViewGroup) LayoutInflater.from(context).inflate(
                R.layout.game_list_item, parent, false);
        return new GameViewHolder(itemLayout);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final GameViewHolder viewHolder = (GameViewHolder) holder;
        final Game gameItem = games.get(position);
        viewHolder.gameTitle.setText(gameItem.getGameTitle());
        List<Image> gameCoverImage = gameItem.getImages();
        if (gameCoverImage != null && gameCoverImage.get(0) != null) {
            Glide.with(context).load(THEGAMEDB_BASE_IMAGE_URL.concat(gameCoverImage.get(0).getBoxart().get("front"))).into(viewHolder.gameFrontCover);
        }
        viewHolder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGameDetailActivity(gameItem);
            }
        });


        if (gameItem.isFavourite()) {
            viewHolder.favourite.setImageResource(R.drawable.favourite);
        } else {
            viewHolder.favourite.setImageResource(R.drawable.normal);
        }

        viewHolder.favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!gameItem.isFavourite()) {
                    ContentValues value = new ContentValues();
                    value.put(GameContract.GameEntry._ID, gameItem.getId());
                    value.put(GameContract.GameEntry.COLUMN_GAME_TITLE, gameItem.getGameTitle());
                    value.put(GameContract.GameEntry.COLUMN_COVER_FRONT, gameItem.getImages().get(0).getBoxart().get("front"));
                    Uri uriResult = context.getContentResolver().insert(GameContract.GameEntry.CONTENT_URI, value);
                    if (uriResult != null) {
                        gameItem.setFavourite(true);
                        notifyDataSetChanged();
                    } else {
                        Toast.makeText(context, "Error saving data", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    String[] selectionArgs;
                    String selection;
                    selection = sGameSelectionByID;
                    selectionArgs = new String[]{String.valueOf(gameItem.getId())};
                    int result = context.getContentResolver().delete(GameContract.GameEntry.CONTENT_URI,
                            selection,
                            selectionArgs
                    );
                    if (result > 0) {
                        gameItem.setFavourite(false);
                        notifyDataSetChanged();
                    } else {
                        Toast.makeText(context, "Error deleting data", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    private void openGameDetailActivity(Game gameExtra) {
        Intent gameDetailIntent = new Intent(this.context, GameDetailActivity.class);
        gameDetailIntent.putExtra("gameExtra", gameExtra);
        this.context.startActivity(gameDetailIntent);
    }

    @Override
    public int getItemCount() {
        return games.size();
    }

    public class GameViewHolder extends RecyclerView.ViewHolder {
        public final TextView gameTitle;
        public final ImageView gameFrontCover;
        public final RelativeLayout mainLayout;
        public final ImageView favourite;

        private GameViewHolder(View view) {
            super(view);
            gameTitle = (TextView) view.findViewById(R.id.gameTitle);
            gameFrontCover = (ImageView) view.findViewById(R.id.gameFrontCover);
            mainLayout = (RelativeLayout) view.findViewById(R.id.mainItemLayout);
            favourite = (ImageView) view.findViewById(R.id.favourite);

            gameTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openGameDetailActivity(games.get(getAdapterPosition()));
                }
            });

            gameFrontCover.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openGameDetailActivity(games.get(getAdapterPosition()));
                }
            });
        }


    }
}
