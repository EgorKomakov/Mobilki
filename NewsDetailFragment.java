package com.example.kursovaya3;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;
import com.example.kursovaya3.databinding.FragmentNewsDetailBinding;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class NewsDetailFragment extends Fragment {
    private FragmentNewsDetailBinding binding;
    private boolean isFavorite = false;
    private NewsItem newsItem;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentNewsDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String title = getArguments() != null
                ? getArguments().getString("title")
                : null;

        // Назад
        binding.btnBack.setOnClickListener(v ->
                NavHostFragment.findNavController(this).navigateUp()
        );

        // Клик по звёздочке
        binding.btnStar.setOnClickListener(v -> toggleFavorite());

        // Предварительно ставим контурную звезду
        binding.btnStar.setImageResource(R.drawable.ic_star_border);

        if (title != null) {
            loadNewsDetails(title);
        }
    }

    private void loadNewsDetails(String title) {
        FirebaseFirestore.getInstance()
                .collection("news")
                .whereEqualTo("title", title)
                .get()
                .addOnSuccessListener(qs -> {
                    if (qs.isEmpty()) return;
                    newsItem = qs.getDocuments().get(0).toObject(NewsItem.class);
                    if (newsItem == null) return;

                    // Заполняем детали
                    binding.title.setText(newsItem.getTitle());
                    binding.content.setText(newsItem.getContent());

                    if (newsItem.getDate() != null) {
                        Date d = newsItem.getDate().toDate();
                        String fmt = new SimpleDateFormat(
                                "dd.MM.yyyy HH:mm", Locale.getDefault()
                        ).format(d);
                        binding.date.setText(fmt);
                    }

                    Glide.with(this)
                            .load(newsItem.getImageUrl())
                            .into(binding.image);

                    // Проверяем в избранном и сразу обновляем иконку
                    checkIsFavorite(newsItem.getTitle());
                });
    }

    private void checkIsFavorite(String title) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("favorites")
                .whereEqualTo("title", title)
                .get()
                .addOnSuccessListener(qs -> {
                    isFavorite = !qs.isEmpty();
                    updateStarIcon();
                });
    }

    private void toggleFavorite() {
        if (newsItem == null) return;

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference favs = db.collection("favorites");
        Query query = favs.whereEqualTo("title", newsItem.getTitle());

        query.get().addOnSuccessListener(qs -> {
            if (!qs.isEmpty()) {
                // удалить все совпадающие
                for (DocumentSnapshot ds : qs) {
                    favs.document(ds.getId()).delete();
                }
                isFavorite = false;
                Toast.makeText(getContext(),
                        "Удалено из избранного", Toast.LENGTH_SHORT).show();
            } else {
                // добавить
                Map<String,Object> data = new HashMap<>();
                data.put("title",    newsItem.getTitle());
                data.put("content",  newsItem.getContent());
                data.put("imageUrl", newsItem.getImageUrl());
                data.put("date",     newsItem.getDate());
                data.put("addedAt",  FieldValue.serverTimestamp());
                favs.add(data).addOnSuccessListener(doc -> {
                    isFavorite = true;
                    Toast.makeText(getContext(),
                            "Добавлено в избранное", Toast.LENGTH_SHORT).show();
                });
            }
            updateStarIcon();
        });
    }

    private void updateStarIcon() {
        binding.btnStar.setImageResource(
                isFavorite
                        ? R.drawable.ic_star_filled
                        : R.drawable.ic_star_border
        );
    }
}
