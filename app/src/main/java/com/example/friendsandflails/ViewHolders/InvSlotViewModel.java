package edu.csumb.flailsandfriends.ViewHolders;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import edu.csumb.flailsandfriends.database.FlailRepo;
import edu.csumb.flailsandfriends.entities.Equipment;

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
