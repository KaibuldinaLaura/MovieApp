package com.example.instagram;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.net.URI;
import java.util.List;

public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.NewsViewHolder> {

    private List<News> newsList;

    @Nullable
    private ItemClickListener listener;
    private ImageView iv2;

    public NewsListAdapter(List<News> newsList) {
        this.newsList = newsList;
    }

    public NewsListAdapter(List<News> newsList, @Nullable ItemClickListener listener) {
        this.newsList = newsList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news, null, false);
        RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        view.setLayoutParams(params);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, final int position) {
        final News news = newsList.get(position);
        holder.tvImageUrl.setImageResource(news.getImageUrl());
        holder.tvDate.setText(news.getDate());
        holder.tvAuthor.setText(news.getAuthor());
        holder.tvImageUrl2.setImageResource(news.getImageUrl2());
        holder.tvTheme.setText(news.getTheme());
        holder.tvRatingCount.setText(String.valueOf(news.getRatingCount()));
        holder.tvComments.setText(String.valueOf(news.getCommentsCount()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.itemClick(position, news);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder {

        ImageView tvImageUrl;
        ImageView tvImageUrl2;
        TextView tvDate;
        TextView tvAuthor;
        TextView tvTheme;
        TextView tvRatingCount;
        TextView tvComments;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            tvImageUrl = itemView.findViewById(R.id.tvImage);
            tvAuthor = itemView.findViewById(R.id.tvAuthor);
            tvImageUrl2 = itemView.findViewById(R.id.tvImage2);
            tvTheme = itemView.findViewById(R.id.tvTheme);
            tvRatingCount = itemView.findViewById(R.id.tvRating);
            tvComments = itemView.findViewById(R.id.tvComments);
            tvDate = itemView.findViewById(R.id.tvDate);
        }
    }

    interface ItemClickListener {
        void itemClick(int position, News item);
    }
}