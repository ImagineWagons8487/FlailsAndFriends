package edu.csumb.flailsandfriends.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.time.LocalDate;

import edu.csumb.flailsandfriends.R;
import edu.csumb.flailsandfriends.database.FlailRepo;
import edu.csumb.flailsandfriends.databinding.ActivityRecordsBinding;
import edu.csumb.flailsandfriends.entities.BattleRecord;
import edu.csumb.flailsandfriends.entities.User;

public class RecordsActivity extends AppCompatActivity {

    static final String SAVED_INSTANCE_STATE_USERID_KEY = "edu.csumb.flailsandfriends.SAVED_INSTANCE_STATE_USERID_KEY";

    private static final int LOGGED_OUT = -1;

    private ActivityRecordsBinding binding;

    private FlailRepo repository;

    private final int loggedInUserId = -1;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


    }

    private void addBattleRecord(int userId, String title, String record, int date){
        //userId =
        //I'm not sure how to do this one so I'll come back to it later

        title = binding.recordTitleInputEditText.getText().toString();

        record = repository.getBattleRecordByTitle(title).toString();

        date = localDateToInt(LocalDate.now());
    }

    private int localDateToInt(LocalDate date){
        int year = date.getYear();
        int month = date.getMonthValue();
        int day = date.getDayOfMonth();
        return year * 10000 + month * 100 + day;
    }

    static Intent recordsActivityIntentFactory(Context context) {
        return new Intent(context, RecordsActivity.class);
    }
}