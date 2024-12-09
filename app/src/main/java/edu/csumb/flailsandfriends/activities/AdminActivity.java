package edu.csumb.flailsandfriends.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import edu.csumb.flailsandfriends.database.FlailRepo;
import edu.csumb.flailsandfriends.databinding.ActivityAdminBinding;
import edu.csumb.flailsandfriends.entities.Equipment;
import edu.csumb.flailsandfriends.entities.User;

public class AdminActivity extends AppCompatActivity {
    private static final String LANDING_PAGE_USER_ID = "com.example.friendsandflails.LANDING_PAGE_USER_ID";

    static final String SAVED_INSTANCE_STATE_USERID_KEY = "com.example.friendsandflails.SAVED_INSTANCE_STATE_USERID_KEY";

    private final int currentUserId = getIntent().getIntExtra(LANDING_PAGE_USER_ID, -1);

    private ActivityAdminBinding binding;

    private FlailRepo repository;

    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        repository = FlailRepo.getRepository(getApplication());

        // leave this as is in case i forget what the lambda does
        binding.adminAddEquipmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyEquipment();
            }
        });

        binding.makeAdminButton.setOnClickListener(view -> updateAdmin());

        binding.deleteUserButton.setOnClickListener(view -> doItMyself());
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
            return;
        }
        else {
            repository.insertEquipment(new Equipment(equipmentName, imagePath, Integer.parseInt(buff)));
            String equipmentString = String.format("Name: %s, Image Path: %s, Buff: %s", equipmentName, imagePath, buff);
            toastMaker(equipmentString);
        }
    }

    /**
     * This method ensures that a valid email has been entered for the user look up.
     * It then ensures that the user exists, and is not the current user. Once these
     * checks have completed, the method toggles the admin status of the user
     * retrieved from the database.
     * **/
    private void updateAdmin(){
        String email = binding.makeAdminNameInputEditText.getText().toString();

        if (email.isEmpty()) {
            toastMaker("Email may not be blank");
            return;
        }

        LiveData<User> userObserver = repository.getUserByEmail(email);

        userObserver.observe(this, flailUser -> {
            if (flailUser == null) {
                toastMaker("No such user exists");
                return;
            } else if(flailUser.getId() == currentUserId){
                toastMaker("Admin cannot remove status from self");
                return;
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

    private void doItMyself(){
        String email = binding.deleteUserInputEditText.getText().toString();

        if (email.isEmpty()) {
            toastMaker("Email may not be blank");
            return;
        }

        LiveData<User> userObserver = repository.getUserByEmail(email);

        userObserver.observe(this, flailUser -> {
            if (flailUser == null) {
                toastMaker("User: Non-existant");
                return;
            } else if(flailUser.getId() == currentUserId){
                toastMaker("Self Delete: Illegal");
                return;
            }else if(flailUser.isAdmin()){
                toastMaker("Admins cannot be deleted");
                return;
            } else {
                repository.deleteUser(flailUser);
                toastMaker("I don't feel so good, Dr. C");
            }
        });
    }

    private void toastMaker(String message) {
        Toast.makeText(AdminActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    static Intent adminActivityIntentFactory(Context context, int userId){
        Intent intent = new Intent(context, AdminActivity.class);
        intent.putExtra(LANDING_PAGE_USER_ID, userId);
        return intent;
    }
}