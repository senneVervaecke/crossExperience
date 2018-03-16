package com.example.sennevervaecke.crossexperience.view.activity;

import android.content.ComponentName;
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
import android.view.MenuItem;

import com.example.sennevervaecke.crossexperience.R;
import com.example.sennevervaecke.crossexperience.controller.DownloadManager;
import com.example.sennevervaecke.crossexperience.controller.DownloadMonitor;
import com.example.sennevervaecke.crossexperience.controller.interfaces.CompetitionFragmentCom;
import com.example.sennevervaecke.crossexperience.controller.interfaces.CourseFragmentCom;
import com.example.sennevervaecke.crossexperience.model.Course;
import com.example.sennevervaecke.crossexperience.model.Competition;
import com.example.sennevervaecke.crossexperience.model.database.Database;
import com.example.sennevervaecke.crossexperience.model.database.DatabaseHelper;
import com.example.sennevervaecke.crossexperience.view.fragment.CompetitionFragment;
import com.example.sennevervaecke.crossexperience.view.fragment.PlayerFragment;
import com.example.sennevervaecke.crossexperience.view.fragment.CourseFragment;

public class StandardFlowActivity extends AppCompatActivity implements CompetitionFragmentCom, CourseFragmentCom {

    private CompetitionFragment competitionFragment;
    private CourseFragment courseFragment;
    private PlayerFragment playerFragment;
    private DownloadFragment downloadFragment;
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
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_standard_flow);

        databaseHelper = new DatabaseHelper(getApplicationContext());
        //databaseHelper.fill();

        Intent serviceIntent = new Intent(this, DownloadManager.class);
        startService(serviceIntent);
        bindService(serviceIntent, connection, getApplicationContext().BIND_AUTO_CREATE);

        competitionFragment = new CompetitionFragment();
        courseFragment = new CourseFragment();
        playerFragment = new PlayerFragment();
        downloadFragment = new DownloadFragment();

        startCompetitionFragment();
    }

    @Override
    public void onCompetitionItemClick(Competition competition){
        startCourseFragment(competition);
    }
    @Override
    public void onCourseItemClick(Competition competition, Course course) {
        if(course.isReadyState()) {
            startPlayerFragment(competition, course);
        }else{
            if(downloadManager != null){
                downloadManager.startDownload(competition, course);
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

    public void startPlayerFragment(Competition competition, Course course){
        getSupportActionBar().setTitle(competition.getName() + " " + course.getLevel());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        playerFragment.setCompetition(competition);
        playerFragment.setCourse(course);
        getSupportFragmentManager().beginTransaction().replace(R.id.standardFlowBaseContainer, playerFragment, "player").commit();
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
        }
        return super.onOptionsItemSelected(item);
    }

}

