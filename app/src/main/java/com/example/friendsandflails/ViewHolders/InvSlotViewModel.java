package com.example.friendsandflails.ViewHolders;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.friendsandflails.database.FlailRepo;
import com.example.friendsandflails.entities.Equipment;

import java.util.List;

public class InvSlotViewModel extends AndroidViewModel {
    private final FlailRepo repository;

    public InvSlotViewModel(Application application){
        super(application);
        repository = FlailRepo.getRepository(application);
    }

    public LiveData<List<Equipment>> getEquipmentById(int userId){
        return repository.getEquipmentById(userId);
    }

    public void insert(Equipment item){
        repository.insertEquipment(item);
    }
}
