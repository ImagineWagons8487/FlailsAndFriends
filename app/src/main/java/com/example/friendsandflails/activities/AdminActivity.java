package com.example.friendsandflails.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.friendsandflails.R;

public class AdminActivity extends AppCompatActivity {
    private static final String LANDING_PAGE_USER_ID = "com.example.friendsandflails.LANDING_PAGE_USER_ID";

    static final String SAVED_INSTANCE_STATE_USERID_KEY = "com.example.friendsandflails.SAVED_INSTANCE_STATE_USERID_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

    }

    static Intent adminActivityIntentFactory(Context context, int userId){
        Intent intent = new Intent(context, AdminActivity.class);
        intent.putExtra(LANDING_PAGE_USER_ID, userId);
        return intent;
    }
}