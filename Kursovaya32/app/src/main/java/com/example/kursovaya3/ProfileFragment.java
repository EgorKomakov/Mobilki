package com.example.kursovaya3;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.kursovaya3.databinding.FragmentProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ProfileFragment extends Fragment {
    private FragmentProfileBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            binding.tvEmail.setText(user.getEmail());
            binding.tvRegistrationDate.setText("Зарегистрирован: " +
                    new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
                            .format(new Date(user.getMetadata().getCreationTimestamp())));
        }

        binding.btnLogout.setOnClickListener(v -> logoutUser());
        binding.btnFavorites.setOnClickListener(v ->
                Navigation.findNavController(v)
                        .navigate(R.id.action_profileFragment_to_favoritesFragment)
        );


        return binding.getRoot();
    }

    private void logoutUser() {
        FirebaseAuth.getInstance().signOut();
        Navigation.findNavController(getView())
                .navigate(R.id.loginFragment);
    }
}
