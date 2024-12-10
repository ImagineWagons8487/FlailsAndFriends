package edu.csumb.flailsandfriends.activities;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import edu.csumb.flailsandfriends.R;
import edu.csumb.flailsandfriends.database.FlailRepo;
import edu.csumb.flailsandfriends.databinding.ActivityAdminBinding;
import edu.csumb.flailsandfriends.entities.Equipment;
import edu.csumb.flailsandfriends.entities.User;

public class AdminActivity extends AppCompatActivity {
    private static final String ADMIN_PAGE_USER_ID = "edu.csumb.flailsandfriends.ADMIN_PAGE_USER_ID";

    static final String SAVED_INSTANCE_STATE_USERID_KEY = "edu.csumb.flailsandfriends.SAVED_INSTANCE_STATE_USERID_KEY";

    private static final int LOGGED_OUT = -1;

    private ActivityAdminBinding binding;

    private FlailRepo repository;

    private int loggedInUserId = -1;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loggedInUserId = getIntent().getIntExtra(ADMIN_PAGE_USER_ID, -1);

        repository = FlailRepo.getRepository(getApplication());

        logInUser(savedInstanceState);

        if(loggedInUserId == LOGGED_OUT){
            Intent intent = LoginActivity.logInIntentFactory(getApplicationContext());
            startActivity(intent);
        }
        updateSharedPreference();

        // leave this as is in case i forget what the lambda does
        binding.adminAddEquipmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyEquipment();
            }
        });

        binding.makeAdminButton.setOnClickListener(view -> updateAdmin(loggedInUserId));

        binding.deleteUserButton.setOnClickListener(view -> doItMyself(loggedInUserId));

        binding.backToLandingPageButton.setOnClickListener(view ->
                startActivity(LandingPageActivity.landingPageIntentFactory(getApplicationContext(), loggedInUserId)));
    }

    /**
     * This method checks all of the fields for the equipment addition form.
     * If all fields are valid, it then creates an Equipment object with all
     * the field values as the parameters. It then inserts the Equipment object
     * into our database.
     * **/
    private void verifyEquipment(){

        String equipmentName = binding.equipmentNameInputEditText.getText().toString();
        if (equipmentName.isEmpty()) {
            toastMaker("Equipment name may not be blank");
            return;
        }

        String imagePath = binding.imagePathInputEditText.getText().toString();
        if (imagePath.isEmpty()) {
            toastMaker("Image path may not be blank");
            return;
        }

        String buff = binding.buffInputEditText.getText().toString();
        if (buff.isEmpty()) {
            toastMaker("Buff amount may not be blank");
        }
        else {
            try {
                int buffNum = Integer.parseInt(buff);
                repository.insertEquipment(new Equipment(equipmentName, imagePath, buffNum));
                String equipmentString = String.format("Name: %s, Image Path: %s, Buff: %s", equipmentName, imagePath, buff);
                toastMaker(equipmentString);
            }catch(NumberFormatException wrongNum){
                toastMaker("Buff must be a valid number");
            }

        }
    }

    /**
     * This method ensures that a valid email has been entered for the user look up.
     * It then ensures that the user exists, and is not the current user. Once these
     * checks have completed, the method toggles the admin status of the user
     * retrieved from the database.
     * **/
    private void updateAdmin(int id){
        String email = binding.makeAdminNameInputEditText.getText().toString();

        if (email.isEmpty()) {
            toastMaker("Email may not be blank");
            return;
        }

        LiveData<User> userObserver = repository.getUserByEmail(email);

        userObserver.observe(this, flailUser -> {
            if (flailUser == null) {
                toastMaker("No such user exists");
            } else if(flailUser.getId() == id){
                toastMaker("Admin cannot remove status from self");
            } else {
                if(flailUser.isAdmin()){
                    flailUser.setAdmin(false);
                    repository.updateUser(flailUser);
                }else{
                    flailUser.setAdmin(true);
                    repository.updateUser(flailUser);
                }
            }
        });
    }

    /**
     * This method ensures that a valid email has been entered for the user look up.
     * It then ensures that the user exists, is not the current user, and is not an admin.
     * Once these checks have completed, the method snaps its proverbial fingers and deletes
     * someone from existence(the database).
     * **/
    private void doItMyself(int id){
        String email = binding.deleteUserInputEditText.getText().toString();

        if (email.isEmpty()) {
            toastMaker("Email may not be blank");
            return;
        }

        LiveData<User> userObserver = repository.getUserByEmail(email);

        userObserver.observe(this, flailUser -> {
            if (flailUser == null) {
                toastMaker("User: Non-existent");
            } else if(flailUser.getId() == id){
                toastMaker("Self Delete: Illegal");
            }else if(flailUser.isAdmin()){
                toastMaker("Admins cannot be deleted");
            } else {
                repository.deleteUser(flailUser);
                toastMaker("I don't feel so good, Dr. C");
            }
        });
    }

    private void toastMaker(String message) {
        Toast.makeText(AdminActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    public static Intent adminActivityIntentFactory(Context context, int userId){
        Intent intent = new Intent(context, AdminActivity.class);
        intent.putExtra(ADMIN_PAGE_USER_ID, userId);
        return intent;
    }

    /**
     * Everything below here is back magic. DO NOT TOUCH!!!
     * **/

    private void logInUser(Bundle savedInstanceState){
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        loggedInUserId = sharedPreferences.getInt(getString(R.string.preference_userid_key), LOGGED_OUT);

        if(loggedInUserId == LOGGED_OUT & savedInstanceState != null && savedInstanceState.containsKey(SAVED_INSTANCE_STATE_USERID_KEY)){
            loggedInUserId = savedInstanceState.getInt(SAVED_INSTANCE_STATE_USERID_KEY, LOGGED_OUT);
        }
        if(loggedInUserId == LOGGED_OUT){
            loggedInUserId = getIntent().getIntExtra(ADMIN_PAGE_USER_ID, LOGGED_OUT);
        }
        if(loggedInUserId == LOGGED_OUT){
            return;
        }

        LiveData<User> userObserver = repository.getUserByUserId(loggedInUserId);
        userObserver.observe(this,user -> {
            this.user = user;
            if(user != null){
                invalidateOptionsMenu();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putInt(SAVED_INSTANCE_STATE_USERID_KEY, loggedInUserId);
        updateSharedPreference();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.logout_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu){
        MenuItem item  = menu.findItem(R.id.logoutMenuId);
        item.setVisible(true);
        if(user == null){
            return false;
        }
        item.setTitle(user.getUsername());
        item.setOnMenuItemClickListener(menuItem -> {
            showLogoutDialog();
            return false;
        });
        return true;
    }

    private void showLogoutDialog(){
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(AdminActivity.this);
        final AlertDialog alertDialog = alertBuilder.create();

        alertBuilder.setMessage("Logout");

        alertBuilder.setPositiveButton("Logout", (dialogInterface, i) -> logout());

        alertBuilder.setNegativeButton("Cancel", (dialogInterface, i) -> alertDialog.dismiss());

        alertBuilder.create().show();
    }

    private void logout(){
        loggedInUserId = LOGGED_OUT;
        updateSharedPreference();
        getIntent().putExtra(ADMIN_PAGE_USER_ID, LOGGED_OUT);

        startActivity(MainActivity.mainActivityIntentFactory(getApplicationContext()));
    }

    private void updateSharedPreference(){
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor sharedPreferenceEditor = sharedPreferences.edit();
        sharedPreferenceEditor.putInt(getString(R.string.preference_userid_key), LOGGED_OUT);
        sharedPreferenceEditor.apply();
    }
}