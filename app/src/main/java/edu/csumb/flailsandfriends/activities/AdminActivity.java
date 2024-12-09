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

    private ActivityAdminBinding binding;

    private FlailRepo repository;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        repository = FlailRepo.getRepository(getApplication());

        binding.adminAddEquipmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyEquipment();
            }
        });

        binding.makeAdminButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateAdmin();
            }
        });
        binding.deleteUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                illDoItMyself();
            }
        });
    }

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

    private void updateAdmin(){
        String email = binding.makeAdminNameInputEditText.getText().toString();
        int currentUserId = getIntent().getIntExtra(LANDING_PAGE_USER_ID, -1);
        if (email.isEmpty()) {
            toastMaker("Name may not be blank");
            return;
        }

        LiveData<User> userObserver = repository.getUserByEmail(email);
        userObserver.observe(this, flailUser -> {
            if (flailUser == null) {
                toastMaker("No such user exists");
                return;
            }
            else if(flailUser.getId() == currentUserId){
                toastMaker("Admin cannot remove status from self");
                return;
            }
            else {
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

    private void illDoItMyself(){
        String email = binding.deleteUserInputEditText.getText().toString();

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