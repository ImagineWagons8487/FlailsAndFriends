package com.example.friendsandflails;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.LiveData;

import com.example.friendsandflails.database.FlailRepo;
import com.example.friendsandflails.databinding.ActivityCreateAccountBinding;
import com.example.friendsandflails.databinding.ActivityLoginBinding;
import com.example.friendsandflails.databinding.ActivityMainBinding;
import com.example.friendsandflails.entities.User;
import com.example.friendsandflails.entities.UserDAO;

public class CreateAccountActivity extends AppCompatActivity {
    private ActivityCreateAccountBinding binding;

    private FlailRepo repository;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateAccountBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        repository = FlailRepo.getRepository(getApplication());

        binding.signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyUser();
            }
        });
    }

    private void verifyUser() {
        String username = binding.usernameEditText.getText().toString();

        if (username.isEmpty()) {
            toastMaker("Username may not be blank");
            return;
        }

        String password = binding.passwordEditText.getText().toString();
        if (password.isEmpty()) {
            toastMaker("Password may not be blank");
            return;
        }
        else {
            repository.insertUser(new User(username, password));
            startActivity(MainActivity.mainActivityIntentFactory(getApplicationContext()));
        }
    }

    private void toastMaker(String message) {
        Toast.makeText(CreateAccountActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    static Intent createAccountIntentFactory(Context context) {
        return new Intent(context, CreateAccountActivity.class);
    }
}