package com.example.kursovaya3;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kursovaya3.databinding.FragmentFavoritesBinding;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class FavoritesFragment extends Fragment {
    private FragmentFavoritesBinding binding;
    private NewsAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 1) Обработчик кнопки "Назад"
        binding.btnBack.setOnClickListener(v ->
                NavHostFragment.findNavController(this)
                        .navigate(R.id.profileFragment)
        );

        // 2) Настройка RecyclerView
        RecyclerView rv = binding.rvFavorites;
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new NewsAdapter();
        rv.setAdapter(adapter);

        // 3) Загрузка избранного
        loadFavorites();
    }

    private void loadFavorites() {
        FirebaseFirestore.getInstance()
                .collection("favorites")
                .orderBy("addedAt", Query.Direction.DESCENDING)
                .addSnapshotListener((snap, err) -> {
                    if (err != null) return;
                    List<NewsItem> list = new ArrayList<>();
                    for (DocumentSnapshot ds : snap.getDocuments()) {
                        list.add(ds.toObject(NewsItem.class));
                    }
                    adapter.submitList(list);
                });
    }
}
