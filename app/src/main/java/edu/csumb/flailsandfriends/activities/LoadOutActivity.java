package edu.csumb.flailsandfriends.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import edu.csumb.flailsandfriends.R;
import edu.csumb.flailsandfriends.ViewHolders.InvSlotAdapter;
import edu.csumb.flailsandfriends.database.FlailRepo;
import edu.csumb.flailsandfriends.databinding.ActivityLoadoutBinding;
import edu.csumb.flailsandfriends.entities.User;

public class LoadOutActivity extends AppCompatActivity {

    private static final String LOADOUT_USER_ID = "edu.csumb.flailsandfriends.activities.LOADOUT_USER_ID";

    private static final int LOGGED_OUT = -1;

    private int loggedInUserId = -1;

    private ActivityLoadoutBinding binding;

    private FlailRepo repository;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //private static final String CHARACTER_SELECT_USER_ID = "edu.csumb.flailsandfriends.CHARACTER_SELECT_USER_ID";
        super.onCreate(savedInstanceState);
        binding = ActivityLoadoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        repository = FlailRepo.getRepository(getApplication());

        loggedInUserId = getIntent().getIntExtra(LOADOUT_USER_ID, LOGGED_OUT);

//        binding.lightButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                verifyUser();
//            }
//        });
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        int numberOfColumns = 5;
        recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));

        int totalItems = 35;
        InvSlotAdapter adapter = new InvSlotAdapter(totalItems);
        recyclerView.setAdapter(adapter);

        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(LandingPageActivity.landingPageIntentFactory(getApplicationContext(), loggedInUserId));
            }
        });
    }

    static Intent loadOutActivityIntentFactory(Context context, int userId) {
        Intent intent = new Intent(context, LoadOutActivity.class);
        intent.putExtra(LOADOUT_USER_ID, userId);
        return intent;
    }
}