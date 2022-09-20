package com.btw.guess.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;



public class SimAdapter extends RecyclerView.Adapter {
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new SimpleViewHolder(view);
        //return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        //SimpleModel usermodel =
                //moviesList.get(position);
        //holder.title.setText(movie.getTitle());
        //holder.genre.setText(movie.getGenre());
        //holder.year.setText(movie.getYear());
        //((SimpleViewHolder) holder).bindData(models.get(position));
                //models.get(position));
    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
