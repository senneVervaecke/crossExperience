package com.example.sennevervaecke.crossexperience.view.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.sennevervaecke.crossexperience.R;
import com.example.sennevervaecke.crossexperience.controller.DownloadCompetitionTask;
import com.example.sennevervaecke.crossexperience.controller.DownloadManager;
import com.example.sennevervaecke.crossexperience.controller.DownloadMonitor;
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
import com.example.sennevervaecke.crossexperience.view.fragment.OrionPlayerFragment;
import com.example.sennevervaecke.crossexperience.view.fragment.PlayerFragment;
import com.example.sennevervaecke.crossexperience.view.fragment.CourseFragment;

import java.io.Serializable;
import java.util.concurrent.ExecutionException;

public class StandardFlowActivity extends AppCompatActivity implements CompetitionFragmentCom, CourseFragmentCom, UpdateDatabaseCom {

    private static final String KEY_PREF_VIDEOTYPE = "videotype";

    private CompetitionFragment competitionFragment;
    private CourseFragment courseFragment;
    private PlayerFragment playerFragment;
    private DownloadFragment downloadFragment;
    private OrionPlayerFragment orionPlayerFragment;
    private DatabaseHelper databaseHelper;
    private static boolean removeDownloadFragment;

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
                if(getSupportFragmentManager().findFragmentByTag("download") != null && !getSupportFragmentManager().findFragmentByTag("download").isStateSaved()){
                    getSupportFragmentManager().beginTransaction().remove(downloadFragment).commit();
                } else {
                    removeDownloadFragment = true;
                }
                courseFragment.refresh();
            } else if(msg.what == DownloadCompetitionTask.ERROR){
                Helper.showMessage(findViewById(R.id.standardFlowRoot), "there was an error downloading the video.");
            } else if(msg.what == DownloadCompetitionTask.NOFILE){
                Helper.showMessage(findViewById(R.id.standardFlowRoot), "there is no video available");
            } else if(msg.what == DownloadCompetitionTask.NO360FILE){
                final Competition competition = (Competition) msg.obj;
                Course course = null;
                for(int i = 0; i < competition.getCourses().size(); i++){
                    if(competition.getCourses().get(i).getId() == msg.arg1){
                        course = competition.getCourses().get(i);
                    }
                }
                if(course != null) {
                    if (!databaseHelper.checkReadyState(competition, course, Constant.EXTENSION_VIDEO)) {
                        final Course finalCourse = course;
                        new AlertDialog.Builder(StandardFlowActivity.this).setMessage("There is no 360° video available at the moment.")
                                .setPositiveButton("download regular video", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        downloadManager.startDownload(competition, finalCourse, Constant.EXTENSION_VIDEO);
                                    }
                                }).create().show();
                    } else {
                        Helper.showMessage(findViewById(R.id.standardFlowRoot), "there is no 360° video available");
                    }
                } else {
                    Helper.showMessage(findViewById(R.id.standardFlowRoot), "there is no 360° video available");
                }
            }
        }
    };

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

            removeDownloadFragment = false;

            competitionFragment = new CompetitionFragment();
            courseFragment = new CourseFragment();
            playerFragment = new PlayerFragment();
            downloadFragment = new DownloadFragment();
            orionPlayerFragment = new OrionPlayerFragment();

            startCompetitionFragment();
        }
    }

    @Override
    public void onCompetitionItemClick(Competition competition){
        startCourseFragment(competition);
    }
    @Override
    public void onCourseItemClick(final Competition competition, final Course course) {
        String videoPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString(KEY_PREF_VIDEOTYPE, "");
        if(databaseHelper.checkReadyState(competition, course, Constant.EXTENSION_VIDEO) || databaseHelper.checkReadyState(competition, course, Constant.EXTENSION_360VIDEO)) {
            //handle orionPlayer vs videoPlayer
            if(videoPref.equals("ORION") && databaseHelper.checkReadyState(competition, course, Constant.EXTENSION_360VIDEO)){
                startOrionPlayerFragment(competition, course);
            } else if(videoPref.equals("VIDEO") && databaseHelper.checkReadyState(competition, course, Constant.EXTENSION_VIDEO)) {
                startPlayerFragment(competition, course);
            } else if(videoPref.equals("ORION") && databaseHelper.checkReadyState(competition, course, Constant.EXTENSION_VIDEO)){

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("there is no 360° video available")
                        .setCancelable(true)
                        .setPositiveButton("download 360° video", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                downloadManager.startDownload(competition, course, Constant.EXTENSION_360VIDEO);
                            }
                        }).setNeutralButton("play regular video", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                startPlayerFragment(competition, course);
                            }
                        }).create().show();

            } else if(videoPref.equals("VIDEO") && databaseHelper.checkReadyState(competition, course, Constant.EXTENSION_360VIDEO)){
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("there is no 360° video available")
                        .setCancelable(true)
                        .setPositiveButton("download 360° video", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                downloadManager.startDownload(competition, course, Constant.EXTENSION_360VIDEO);
                            }
                        }).setNeutralButton("play regular video", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startPlayerFragment(competition, course);
                    }
                }).create().show();
            }
        }else{
            if(downloadManager != null && Helper.isInternetConnected(this)){
                if(videoPref.equals("VIDEO")) {
                    downloadManager.startDownload(competition, course, Constant.EXTENSION_VIDEO);
                } else if(videoPref.equals("ORION")){
                    downloadManager.startDownload(competition, course, Constant.EXTENSION_360VIDEO);
                }
            }
            else{
                Helper.showMessage(findViewById(R.id.standardFlowRoot), "there is no internet connection");
            }
        }
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

    public void startOrionPlayerFragment(Competition competition, Course course){
        getSupportActionBar().setTitle(Helper.toCamelCase(competition.getName() + " " + course.getLevel()));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        orionPlayerFragment.set(competition, course);
        getSupportFragmentManager().beginTransaction().replace(R.id.standardFlowBaseContainer, orionPlayerFragment, "orionPlayer").commit();
    }

    public void startPlayerFragment(Competition competition, Course course){
        getSupportActionBar().setTitle(Helper.toCamelCase(competition.getName() + " " + course.getLevel()));
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
            fragment = fragmentManager.findFragmentByTag("course");
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
    protected void onRestart() {
        if(removeDownloadFragment){
            getSupportFragmentManager().beginTransaction().remove(downloadFragment).commit();
            removeDownloadFragment = false;
        }
        //competitionFragment.refresh();
        super.onRestart();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        if(competitionFragment.isAdded())
            getSupportFragmentManager().putFragment(outState, Constant.TAG_COMPETITION, competitionFragment);
        if(courseFragment.isAdded())
            getSupportFragmentManager().putFragment(outState, Constant.TAG_COURSE, courseFragment);
        if(downloadFragment.isAdded())
            getSupportFragmentManager().putFragment(outState, Constant.TAG_DOWNLOAD, downloadFragment);
        if(playerFragment.isAdded())
            getSupportFragmentManager().putFragment(outState, Constant.TAG_PLAYER, playerFragment);
        if(orionPlayerFragment.isAdded())
            getSupportFragmentManager().putFragment(outState, Constant.TAG_ORION_PLAYER, orionPlayerFragment);

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
        downloadFragment = (DownloadFragment) fm.getFragment(savedInstanceState, Constant.TAG_DOWNLOAD);
        if(downloadFragment == null){
            downloadFragment = new DownloadFragment();
        }
        playerFragment = (PlayerFragment) fm.getFragment(savedInstanceState, Constant.TAG_PLAYER);
        if(playerFragment == null){
            playerFragment = new PlayerFragment();
        }
        orionPlayerFragment = (OrionPlayerFragment) fm.getFragment(savedInstanceState, Constant.TAG_ORION_PLAYER);
        if(orionPlayerFragment == null){
            orionPlayerFragment = new OrionPlayerFragment();
        }

        if(getSupportFragmentManager().findFragmentByTag(Constant.TAG_COMPETITION) == null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } else {
            getSupportActionBar().setTitle(R.string.app_name);
        }
        if(getSupportFragmentManager().findFragmentByTag(Constant.TAG_COURSE) != null && courseFragment != null && courseFragment.getCompetition() != null){
            getSupportActionBar().setTitle(Helper.toCamelCase(courseFragment.getCompetition().getName()));
        } else if(getSupportFragmentManager().findFragmentByTag(Constant.TAG_PLAYER) != null && playerFragment != null && playerFragment.getCompetition() != null && playerFragment.getCourse() != null){
            getSupportActionBar().setTitle(Helper.toCamelCase(playerFragment.getCompetition().getName() + " " + playerFragment.getCourse().getLevel()));
        } else if(getSupportFragmentManager().findFragmentByTag(Constant.TAG_ORION_PLAYER) != null && orionPlayerFragment != null && orionPlayerFragment.getCompetition() != null && orionPlayerFragment.getCourse() != null){
            getSupportActionBar().setTitle(Helper.toCamelCase(orionPlayerFragment.getCompetition().getName() + " " + orionPlayerFragment.getCourse().getLevel()));
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

