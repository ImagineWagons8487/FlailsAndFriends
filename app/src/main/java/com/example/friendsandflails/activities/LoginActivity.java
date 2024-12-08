package edu.csumb.flailsandfriends.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import edu.csumb.flailsandfriends.database.FlailRepo;
import edu.csumb.flailsandfriends.databinding.ActivityLoginBinding;
import edu.csumb.flailsandfriends.entities.User;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;

    private FlailRepo repository;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        repository = FlailRepo.getRepository(getApplication());

        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyUser();
            }
        });
    }

    private void verifyUser() {
        String username = binding.userNameLoginEditText.getText().toString();

        if (username.isEmpty()) {
            toastMaker("Username may not be blank");
            return;

        }

        LiveData<User> userObserver = repository.getUserByUsername(username);

        userObserver.observe(this, user -> {
            if (user != null) {
                String password = binding.passwordLoginEditText.getText().toString();
                if (password.equals(user.getPassword())) {
                    startActivity(LandingPageActivity.landingPageIntentFactory(getApplicationContext(), user.getId()));
                } else {
                    toastMaker("Invalid Password");
                    binding.passwordLoginEditText.setSelection(0);
                }
            } else {
                toastMaker(String.format("%s is not a valid username", username));
                binding.userNameLoginEditText.setSelection(0);
            }
        });
    }

    private void toastMaker(String message) {
        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    static Intent logInIntentFactory(Context context) {
        return new Intent(context, LoginActivity.class);
    }
}