package com.example.sennevervaecke.crossexperience;

import android.app.Application;
import android.preference.PreferenceManager;

import com.example.sennevervaecke.crossexperience.controller.UpdateDatabaseTask;

/**
 * Created by sennevervaecke on 8/18/2018.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        PreferenceManager.setDefaultValues(this, R.xml.pref_general, false);
        PreferenceManager.setDefaultValues(this, R.xml.pref_data_sync, false);

        //check to delete

        //

        super.onCreate();
    }
}
