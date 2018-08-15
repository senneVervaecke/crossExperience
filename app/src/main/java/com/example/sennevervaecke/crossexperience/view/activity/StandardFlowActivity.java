package com.example.sennevervaecke.crossexperience.view.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.sennevervaecke.crossexperience.R;
import com.example.sennevervaecke.crossexperience.controller.DownloadCompetitionTask;
import com.example.sennevervaecke.crossexperience.controller.DownloadManager;
import com.example.sennevervaecke.crossexperience.controller.DownloadMonitor;
import com.example.sennevervaecke.crossexperience.controller.Helper;
import com.example.sennevervaecke.crossexperience.controller.UpdateDatabaseTask;
import com.example.sennevervaecke.crossexperience.model.interfaces.CompetitionFragmentCom;
import com.example.sennevervaecke.crossexperience.model.interfaces.CourseFragmentCom;
import com.example.sennevervaecke.crossexperience.model.Course;
import com.example.sennevervaecke.crossexperience.model.Competition;
import com.example.sennevervaecke.crossexperience.model.database.DatabaseHelper;
import com.example.sennevervaecke.crossexperience.view.fragment.CompetitionFragment;
import com.example.sennevervaecke.crossexperience.view.fragment.OrionPlayerFragment;
import com.example.sennevervaecke.crossexperience.view.fragment.PlayerFragment;
import com.example.sennevervaecke.crossexperience.view.fragment.CourseFragment;
import com.example.sennevervaecke.crossexperience.view.fragment.SettingsFragment;

import java.util.concurrent.ExecutionException;

public class StandardFlowActivity extends AppCompatActivity implements CompetitionFragmentCom, CourseFragmentCom {

    private CompetitionFragment competitionFragment;
    private CourseFragment courseFragment;
    private PlayerFragment playerFragment;
    private DownloadFragment downloadFragment;
    private SettingsFragment settingsFragment;
    private OrionPlayerFragment orionPlayerFragment;
    private DatabaseHelper databaseHelper;

    private DownloadManager downloadManager;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            DownloadManager.DownloadBinder binder = (DownloadManager.DownloadBinder) iBinder;
            downloadManager = binder.getService();
            downloadManager.setHandler(handler);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {}
    };
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == DownloadMonitor.UPDATE){
                downloadFragment.setProgress(msg.arg1);
            }
            else if(msg.what == DownloadMonitor.START){
                downloadFragment.setProgress(0);
                getSupportFragmentManager().beginTransaction().add(R.id.standardFlowDownloadContainer, downloadFragment, "download").commit();
            }
            else if(msg.what == DownloadMonitor.END){
                downloadManager.finishDownload();
                getSupportFragmentManager().beginTransaction().remove(downloadFragment).commit();
                courseFragment.refresh();
            } else if(msg.what == DownloadCompetitionTask.ERROR){
                Helper.showMessage(findViewById(R.id.standardFlowRoot), "there was an error downloading the video.");
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_standard_flow);

        UpdateDatabaseTask updateDatabaseTask = new UpdateDatabaseTask(getApplicationContext());
        //TODO: make happen async
        try {
            String result = updateDatabaseTask.execute().get();
            Log.e("test", result);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        databaseHelper = new DatabaseHelper(getApplicationContext());

        Intent serviceIntent = new Intent(this, DownloadManager.class);
        startService(serviceIntent);
        bindService(serviceIntent, connection, Context.BIND_AUTO_CREATE);

        competitionFragment = new CompetitionFragment();
        courseFragment = new CourseFragment();
        playerFragment = new PlayerFragment();
        downloadFragment = new DownloadFragment();
        settingsFragment = new SettingsFragment();
        orionPlayerFragment = new OrionPlayerFragment();

        //startSettingsFragment();
        startCompetitionFragment();
    }

    @Override
    public void onCompetitionItemClick(Competition competition){
        startCourseFragment(competition);
    }
    @Override
    public void onCourseItemClick(Competition competition, Course course) {
        if(databaseHelper.checkReadyState(competition, course, ".mp4")) {
            //TODO something
            //startPlayerFragment(competition, course);
            startOrionPlayerFragment(competition, course);
        }else{
            if(downloadManager != null && Helper.isInternetConnected(this)){
                downloadManager.startDownload(competition, course);
            }
            else{
                Helper.showMessage(findViewById(R.id.standardFlowRoot), "there is no internet connection");
            }
        }
    }

    public void startCompetitionFragment(){
        getSupportActionBar().setTitle("crossExperience");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportFragmentManager().beginTransaction().replace(R.id.standardFlowBaseContainer, competitionFragment, "wedstrijd").commit();
    }

    public void startCourseFragment(Competition competition){
        getSupportActionBar().setTitle(competition.getName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        courseFragment.setCompetition(competition);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.standardFlowBaseContainer, courseFragment, "reeks");
        transaction.commit();
    }

    public void startOrionPlayerFragment(Competition competition, Course course){
        getSupportActionBar().setTitle(competition.getName() + " " + course.getLevel());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        orionPlayerFragment.set(competition, course);
        getSupportFragmentManager().beginTransaction().replace(R.id.standardFlowBaseContainer, orionPlayerFragment, "orionPlayer").commit();
    }

    public void startPlayerFragment(Competition competition, Course course){
        getSupportActionBar().setTitle(competition.getName() + " " + course.getLevel());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        playerFragment.setCompetition(competition);
        playerFragment.setCourse(course);
        getSupportFragmentManager().beginTransaction().replace(R.id.standardFlowBaseContainer, playerFragment, "player").commit();
    }

    public void startSettingsFragment(){
        getSupportFragmentManager().beginTransaction().replace(R.id.standardFlowBaseContainer, settingsFragment, "settings").commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            Fragment fragment;
            //check if current fragment is reeks
            fragment = fragmentManager.findFragmentByTag("reeks");
            if(fragment != null && fragment.isVisible()){
                startCompetitionFragment();
                return super.onOptionsItemSelected(item);
            }
            //check if current fragment is player
            fragment = fragmentManager.findFragmentByTag("player");
            if(fragment != null && fragment.isVisible()){
                PlayerFragment playerFragment = (PlayerFragment) fragment;
                startCourseFragment(playerFragment.getCompetition());
                return super.onOptionsItemSelected(item);
            }
            //check if current fragment is orion player
            fragment = fragmentManager.findFragmentByTag("orionPlayer");
            if(fragment != null && fragment.isVisible()){
                OrionPlayerFragment orionPlayerFragment = (OrionPlayerFragment) fragment;
                startCourseFragment(orionPlayerFragment.getCompetition());
                return super.onOptionsItemSelected(item);
            }
        }
        return super.onOptionsItemSelected(item);
    }
}

