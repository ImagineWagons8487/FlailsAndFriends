package edu.csumb.flailsandfriends.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

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

        binding.adminAddEquipmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyAdmin();
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

    private void verifyAdmin(){
        String name = binding.makeAdminNameInputEditText.getText().toString();
        if (name.isEmpty()) {
            toastMaker("Name may not be blank");
            return;
        }
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