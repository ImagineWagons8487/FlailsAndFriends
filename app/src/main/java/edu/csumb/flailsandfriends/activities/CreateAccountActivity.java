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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import edu.csumb.flailsandfriends.database.FlailRepo;
import edu.csumb.flailsandfriends.databinding.ActivityCreateAccountBinding;
import edu.csumb.flailsandfriends.entities.User;

public class CreateAccountActivity extends AppCompatActivity {
    private static final String TAG = "CreateAccount";

    private ActivityCreateAccountBinding binding;

    private FlailRepo repository;

    private FirebaseAuth mAuth;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateAccountBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        repository = FlailRepo.getRepository(getApplication());

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        binding.signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyUser();
            }
        });

        binding.backToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void verifyUser() {
        String email = binding.emailEditText.getText().toString();

        if (email.isEmpty()) {
            toastMaker("Email may not be blank");
            return;
        }

        // Simple email validation before querying firebase
        if (!email.contains("@")) {
            toastMaker("Email must contain @ symbol");
            return;
        }

        String password = binding.passwordEditText.getText().toString();
        if (password.isEmpty()) {
            toastMaker("Password may not be blank");
            return;
        }

        setIsLoading(true);
        // API sign-up using Firebase Authentication
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        setIsLoading(false);
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();

                            // Update the local repository
                            User flailUser = new User(email, password);
                            repository.insertUser(flailUser);

                            Snackbar snackbar = toastMaker( "Let's go boys, we've got a new user!! ");
                            snackbar.addCallback(new Snackbar.Callback() {
                                @Override
                                public void onDismissed(Snackbar snackbar, int event) {
                                    // Start the main activity when the Snackbar is dismissed
                                    startActivity(MainActivity.mainActivityIntentFactory(getApplicationContext()));
                                }});
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            String errorMessage;
                            try {
                                errorMessage = task.getException().getMessage();
                            } catch (NullPointerException e) {
                                errorMessage= "Try again later.";
                            }
                            toastMaker( "Authentication failed: "+errorMessage);
                        }
                    }
                });
    }

    private void setIsLoading(boolean b) {
        binding.emailEditText.setEnabled(!b);
        binding.passwordEditText.setEnabled(!b);
        binding.signUpButton.setEnabled(!b);
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

    public static Intent createAccountIntentFactory(Context context) {
        return new Intent(context, CreateAccountActivity.class);
    }
}