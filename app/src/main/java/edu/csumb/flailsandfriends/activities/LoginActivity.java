package edu.csumb.flailsandfriends.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import edu.csumb.flailsandfriends.database.FlailRepo;
import edu.csumb.flailsandfriends.databinding.ActivityLoginBinding;
import edu.csumb.flailsandfriends.entities.User;

public class LoginActivity extends AppCompatActivity {
    private static String TAG = "LoginActivity";

    private ActivityLoginBinding binding;

    private FlailRepo repository;

    private User user;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        repository = FlailRepo.getRepository(getApplication());

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyUser();
            }
        });
    }

    private void verifyUser() {
        String email = binding.emailLoginEditText.getText().toString();
        if (email.isEmpty()) {
            toastMaker("Email may not be blank");
            return;
        }

        String password = binding.passwordLoginEditText.getText().toString();
        if (password.isEmpty()) {
            toastMaker("Password may not be blank");
            return;
        }

        LoginActivity that = this;
        setIsLoading(true);
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // Firebase authentication has resolved with a success or error state
                        setIsLoading(false);

                        // If Firebase authentication is successful, then we need to proceed to
                        // launching our landing page activity.
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");

                            FirebaseUser firebaseUser = mAuth.getCurrentUser();

                            // Let's update our local database first by ensuring it has a user row.
                            LiveData<User> userObserver = repository.getUserByEmail(email);
                            userObserver.observe(that, flailUser -> {
                                // FlailRepository is missing the user that Firebase authentication
                                // says exists so we should update FlailRepository to contain that
                                // user because Firebase authentication is the source of truth.
                                if (flailUser == null) {
                                    flailUser = new User(email, password);
                                    repository.insertUser(flailUser);
                                }

                                Snackbar snackbar = toastMaker( "Welcome back, prepare to flail your friends ;)");
                                // Make flailUser final is necessary to suppress a warning
                                User finalFlailUser = flailUser;
                                snackbar.addCallback(new Snackbar.Callback() {
                                    @Override
                                    public void onDismissed(Snackbar snackbar, int event) {
                                        // Start the landing page activity when the Snackbar is dismissed
                                        startActivity(LandingPageActivity.landingPageIntentFactory(getApplicationContext(), finalFlailUser.getId()));
                                    }});
                            });
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            String errorMessage;
                            try {
                                errorMessage = task.getException().getMessage();
                            } catch (NullPointerException e) {
                                errorMessage= "Try again later.";
                            }
                            toastMaker("Authentication failed: " + errorMessage);
                        }
                    }
                });
    }

    private void setIsLoading(boolean b) {
        binding.emailLoginEditText.setEnabled(!b);
        binding.passwordLoginEditText.setEnabled(!b);
        binding.loginButton.setEnabled(!b);
    }

    private Snackbar toastMaker(String message)  {
        // Toast.makeText(CreateAccountActivity.this, message, Toast.LENGTH_LONG).show();
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG);
        View snackbarView = snackbar.getView();
        TextView snackbarText = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
        snackbarText.setMaxLines(10); // Set the maximum lines to prevent truncation
        snackbarText.setTextSize(16); // Optional: Adjust text size for better readability
        snackbar.show();
        return snackbar;
    }

    static Intent logInIntentFactory(Context context) {
        return new Intent(context, LoginActivity.class);
    }
}