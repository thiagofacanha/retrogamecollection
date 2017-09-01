package br.com.sandclan.retrocollection.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.com.sandclan.retrocollection.R;
import br.com.sandclan.retrocollection.models.Game;

public class GameAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Game> games;
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
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        GameViewHolder viewHolder = (GameViewHolder) holder;
        viewHolder.gameTitle.setText(games.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return games.size();
    }

    public static class GameViewHolder extends RecyclerView.ViewHolder {
        public final TextView gameTitle;

        private GameViewHolder(View view) {
            super(view);
            gameTitle = (TextView) view.findViewById(R.id.gameTitle);
        }
    }
}
