package com.example.kursovaya3;

import static java.security.AccessController.getContext;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class NewsListFragment extends Fragment {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private NewsAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_list, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.rvNews);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new NewsAdapter();
        recyclerView.setAdapter(adapter);

        loadNews();
        return view;
    }

    private void loadNews() {
        db.collection("news")
                .orderBy("date", Query.Direction.DESCENDING)
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Log.e("NewsListFragment", "Ошибка загрузки: " + error.getMessage());
                        return;
                    }

                    List<NewsItem> newsList = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : value) {
                        NewsItem news = doc.toObject(NewsItem.class);
                        newsList.add(news);
                    }

                    if (newsList.isEmpty()) {
                        Log.d("NewsListFragment", "Новостей нет в базе");
                    }

                    adapter.submitList(newsList);
                });
    }
}
