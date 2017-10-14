package br.com.sandclan.retrocollection.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import br.com.sandclan.retrocollection.R;
import br.com.sandclan.retrocollection.models.Game;
import br.com.sandclan.retrocollection.ui.GameDetailActivity;

import static br.com.sandclan.retrocollection.GameServiceInterface.THEGAMEDB_BASE_IMAGE_URL;
import static br.com.sandclan.retrocollection.data.GameContentProvider.getGameFromCursor;
import static br.com.sandclan.retrocollection.data.GameContract.GameEntry.COLUMN_COVER_FRONT;
import static br.com.sandclan.retrocollection.data.GameContract.GameEntry.COLUMN_GAME_TITLE;
import static br.com.sandclan.retrocollection.ui.GameDetailActivity.GAME_EXTRA;

public class GameAdapter extends RecyclerViewCursorAdapter<GameAdapter.GameViewHolder> implements View.OnClickListener {
    private Context context;
    private OnItemClickListener onItemClickListener;

    public GameAdapter(Context context) {
        super();
        this.context = context;
    }

    @Override
    public GameViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewGroup itemLayout = (ViewGroup) LayoutInflater.from(parent.getContext()).inflate(
                R.layout.game_list_item, parent, false);
        itemLayout.setOnClickListener(this);
        return new GameViewHolder(itemLayout);
    }

    @Override
    public void onBindViewHolder(GameViewHolder holder, final Cursor cursor) {
        holder.bindData(cursor);

    }

    @Override
    public void onClick(View v) {
        final RecyclerView recyclerView = (RecyclerView) v.getParent();
        final int position = recyclerView.getChildLayoutPosition(v);
        if (position != RecyclerView.NO_POSITION) {
            final Cursor cursor = this.getItem(position);
            Game game = getGameFromCursor(cursor);
            openGameDetailActivity(game);
        }
    }

    public void setOnItemClickListener(final OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public class GameViewHolder extends RecyclerView.ViewHolder {
        public TextView gameTitle;
        public ImageView gameFrontCover;
        public RelativeLayout mainLayout;

        private GameViewHolder(View view) {
            super(view);
            gameTitle = (TextView) view.findViewById(R.id.gameTitle);
            gameFrontCover = (ImageView) view.findViewById(R.id.gameFrontCover);
            mainLayout = (RelativeLayout) view.findViewById(R.id.mainLayout);

        }

        public void bindData(final Cursor cursor) {
            gameTitle.setText(cursor.getString(cursor.getColumnIndex(COLUMN_GAME_TITLE)));
            Glide.with(context).load(THEGAMEDB_BASE_IMAGE_URL.concat(cursor.getString(cursor.getColumnIndex(COLUMN_COVER_FRONT)))).into(gameFrontCover);

        }


    }

    public interface OnItemClickListener {
        void onItemClicked(Cursor cursor);
    }

    private void openGameDetailActivity(Game gameExtra) {
        Intent gameDetailIntent = new Intent(context, GameDetailActivity.class);
        gameDetailIntent.putExtra(GAME_EXTRA, gameExtra);
        context.startActivity(gameDetailIntent);
    }


}
