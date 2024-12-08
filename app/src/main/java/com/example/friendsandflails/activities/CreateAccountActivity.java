package edu.csumb.flailsandfriends.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import edu.csumb.flailsandfriends.database.FlailRepo;
import edu.csumb.flailsandfriends.databinding.ActivityCreateAccountBinding;
import edu.csumb.flailsandfriends.entities.User;

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