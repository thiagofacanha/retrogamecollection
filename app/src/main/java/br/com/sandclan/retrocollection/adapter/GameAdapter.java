package br.com.sandclan.retrocollection.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import br.com.sandclan.retrocollection.R;
import br.com.sandclan.retrocollection.models.Game;
import br.com.sandclan.retrocollection.models.Image;

import static br.com.sandclan.retrocollection.GameServiceInterface.THEGAMEDB_BASE_IMAGE_URL;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.GameViewHolder> {
    private List<Game> games;
    private Context context;

    public GameAdapter(Context context, List<Game> games) {
        this.context = context;
        this.games = games;

    }


    @Override
    public GameViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewGroup itemLayout = (ViewGroup) LayoutInflater.from(parent.getContext()).inflate(
                R.layout.game_list_item, parent, false);
        return new GameViewHolder(itemLayout);
    }

    @Override
    public void onBindViewHolder(GameViewHolder holder, final int position) {
        Game gameItem = games.get(position);
        holder.gameTitle.setText(gameItem.getGameTitle());
        List<Image> gameCoverImage = gameItem.getImages();
        if (gameCoverImage != null && gameCoverImage.get(0) != null) {
            Glide.with(context).load(THEGAMEDB_BASE_IMAGE_URL.concat(gameCoverImage.get(0).getBoxart().get("front"))).into(holder.gameFrontCover);
        }

    }


    @Override
    public int getItemCount() {
        return games.size();
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
    }
}
