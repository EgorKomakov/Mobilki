package com.example.kursovaya3;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.kursovaya3.databinding.FragmentRegisterBinding;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterFragment extends Fragment {
    private FragmentRegisterBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater, container, false);

        binding.btnRegister.setOnClickListener(v -> validateAndRegister());
        binding.tvLogin.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.loginFragment));

        return binding.getRoot();
    }

    private void validateAndRegister() {
        String email = binding.etEmail.getText().toString().trim();
        String password = binding.etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            binding.etEmail.setError("Введите email");
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etEmail.setError("Некорректный email");
            return;
        }

        if (password.length() < 6) {
            binding.etPassword.setError("Минимум 6 символов");
            return;
        }

        createUser(email, password);
    }

    private void createUser(String email, String password) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Navigation.findNavController(getView())
                                .navigate(R.id.newsListFragment);
                    } else {
                        Toast.makeText(getContext(), "Ошибка регистрации: " +
                                task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}