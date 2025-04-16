package grsu.by.fitnessapp;

import android.app.Application;

import grsu.by.fitnessapp.database.AppDatabase;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        AppDatabase db = AppDatabase.getInstance(this);
    }
}

