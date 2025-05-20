package com.example.kursovaya3;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class LoginFragment extends Fragment {
    private TextInputEditText etEmail, etPassword;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        etEmail = view.findViewById(R.id.etEmail);
        etPassword = view.findViewById(R.id.etPassword);

        view.findViewById(R.id.btnLogin).setOnClickListener(v -> loginUser());
        view.findViewById(R.id.btnRegister).setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.registerFragment));

        return view;
    }

    private void loginUser() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Navigation.findNavController(getView()).navigate(R.id.newsListFragment);
                        requireActivity().findViewById(R.id.bottomNavigation).setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(getContext(), "Ошибка авторизации", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
