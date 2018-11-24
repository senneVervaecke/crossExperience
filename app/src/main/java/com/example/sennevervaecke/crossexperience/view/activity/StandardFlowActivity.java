package com.example.sennevervaecke.crossexperience.view.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.sennevervaecke.crossexperience.R;
import com.example.sennevervaecke.crossexperience.controller.DownloadManager;
import com.example.sennevervaecke.crossexperience.controller.Helper;
import com.example.sennevervaecke.crossexperience.controller.UpdateDatabaseTask;
import com.example.sennevervaecke.crossexperience.model.Constant;
import com.example.sennevervaecke.crossexperience.model.interfaces.CompetitionFragmentCom;
import com.example.sennevervaecke.crossexperience.model.interfaces.CourseFragmentCom;
import com.example.sennevervaecke.crossexperience.model.Course;
import com.example.sennevervaecke.crossexperience.model.Competition;
import com.example.sennevervaecke.crossexperience.model.database.DatabaseHelper;
import com.example.sennevervaecke.crossexperience.model.interfaces.UpdateDatabaseCom;
import com.example.sennevervaecke.crossexperience.view.fragment.CompetitionFragment;
import com.example.sennevervaecke.crossexperience.view.fragment.CourseFragment;

public class StandardFlowActivity extends AppCompatActivity implements CompetitionFragmentCom, CourseFragmentCom, UpdateDatabaseCom {

    private CompetitionFragment competitionFragment;
    private CourseFragment courseFragment;
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
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_standard_flow);

        databaseHelper = new DatabaseHelper(getApplicationContext());

        Intent serviceIntent = new Intent(this, DownloadManager.class);
        if (!DownloadManager.isActive) {
            startService(serviceIntent);
        }
        bindService(serviceIntent, connection, Context.BIND_AUTO_CREATE);

        if(savedInstanceState == null) {
            UpdateDatabaseTask updateDatabaseTask = new UpdateDatabaseTask(getApplicationContext(), this);
            updateDatabaseTask.execute();

            competitionFragment = new CompetitionFragment();
            courseFragment = new CourseFragment();
            startCompetitionFragment();
        }
    }

    @Override
    public void onCompetitionItemClick(Competition competition){
        startCourseFragment(competition);
    }
    @Override
    public void onCourseItemClick(final Competition competition, final Course course) {
        //go to player activity
    }

    public void startCompetitionFragment(){
        getSupportActionBar().setTitle(R.string.app_name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportFragmentManager().beginTransaction().replace(R.id.standardFlowBaseContainer, competitionFragment, "competition").commit();
    }

    public void startCourseFragment(Competition competition){
        getSupportActionBar().setTitle(Helper.toCamelCase(competition.getName()));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        courseFragment.setCompetition(competition);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.standardFlowBaseContainer, courseFragment, "course");
        transaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            Fragment fragment;
            //check if current fragment is reeks
            fragment = fragmentManager.findFragmentByTag("course");
            if(fragment != null && fragment.isVisible()){
                startCompetitionFragment();
                return super.onOptionsItemSelected(item);
            }
        } else if(item.getItemId() == R.id.action_settings){
            startActivity(new Intent(this, SettingsActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {

        if(competitionFragment.isAdded())
            getSupportFragmentManager().putFragment(outState, Constant.TAG_COMPETITION, competitionFragment);
        if(courseFragment.isAdded())
            getSupportFragmentManager().putFragment(outState, Constant.TAG_COURSE, courseFragment);

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        FragmentManager fm = getSupportFragmentManager();
        competitionFragment = (CompetitionFragment) fm.getFragment(savedInstanceState, Constant.TAG_COMPETITION);
        if(competitionFragment == null){
            competitionFragment = new CompetitionFragment();
        }
        courseFragment = (CourseFragment) fm.getFragment(savedInstanceState, Constant.TAG_COURSE);
        if(courseFragment == null){
            courseFragment = new CourseFragment();
        }

        if(getSupportFragmentManager().findFragmentByTag(Constant.TAG_COMPETITION) == null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } else {
            getSupportActionBar().setTitle(R.string.app_name);
        }
        if(getSupportFragmentManager().findFragmentByTag(Constant.TAG_COURSE) != null && courseFragment != null && courseFragment.getCompetition() != null){
            getSupportActionBar().setTitle(Helper.toCamelCase(courseFragment.getCompetition().getName()));
        }

        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        unbindService(connection);
        super.onDestroy();
    }

    @Override
    public void onCompleteTask() {
        competitionFragment.refresh();
    }
}

