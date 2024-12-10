package edu.csumb.flailsandfriends;

import android.app.Application;

import com.google.firebase.BuildConfig;

import timber.log.Timber;

public class theApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Timber.plant(new Timber.DebugTree());

        Timber.tag("goofy tag");
    }
}
