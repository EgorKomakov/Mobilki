package com.example.kursovaya3;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.services.events.TimeStamp;

import com.bumptech.glide.Glide;
import com.example.kursovaya3.NewsItem;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NewsAdapter extends ListAdapter<NewsItem, NewsAdapter.ViewHolder> {

    // DIFF_CALLBACK для NewsItem
    private static final DiffUtil.ItemCallback<NewsItem> DIFF_CALLBACK = new DiffUtil.ItemCallback<NewsItem>() {
        @Override
        public boolean areItemsTheSame(@NonNull NewsItem oldItem, @NonNull NewsItem newItem) {
            return oldItem.getContent().equals(newItem.getContent()); // Используем content для сравнения
        }

        @Override
        public boolean areContentsTheSame(@NonNull NewsItem oldItem, @NonNull NewsItem newItem) {
            return oldItem.equals(newItem); // Проверяем равенство объектов
        }
    };

    public NewsAdapter() {
        super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_news, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NewsItem newsItem = getItem(position);
        holder.bind(newsItem);

        holder.itemView.setOnClickListener(v -> {
            Bundle args = new Bundle();
            args.putString("title", newsItem.getTitle());
            Navigation.findNavController(v).navigate(R.id.newsDetailFragment, args);
        });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvTitle;
        private final TextView tvContent;
        private final ImageView ivImage;

        private final TextView tvDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvContent = itemView.findViewById(R.id.tvContent);
            ivImage = itemView.findViewById(R.id.ivImage);
            tvDate = itemView.findViewById(R.id.tvDate);
        }

        void bind(NewsItem newsItem) {
            tvTitle.setText(newsItem.getTitle());
            tvContent.setText(newsItem.getContent());

            if (newsItem.getDate() != null) {
                Date date = newsItem.getDate().toDate();
                String formatted = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
                        .format(date);
                tvDate.setText(formatted);
            } else {
                tvDate.setText("");
            }

            Glide.with(itemView.getContext())
                    .load(newsItem.getImageUrl())
                    .into(ivImage);
        }
    }
}