package com.example.sennevervaecke.crossexperience.view.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.sennevervaecke.crossexperience.R;
import com.example.sennevervaecke.crossexperience.controller.DownloadManager;
import com.example.sennevervaecke.crossexperience.controller.Helper;
import com.example.sennevervaecke.crossexperience.controller.UpdateDatabaseTask;
import com.example.sennevervaecke.crossexperience.model.Constant;
import com.example.sennevervaecke.crossexperience.model.Element;
import com.example.sennevervaecke.crossexperience.model.interfaces.CompetitionFragmentCom;
import com.example.sennevervaecke.crossexperience.model.interfaces.CourseFragmentCom;
import com.example.sennevervaecke.crossexperience.model.Course;
import com.example.sennevervaecke.crossexperience.model.Competition;
import com.example.sennevervaecke.crossexperience.model.interfaces.UpdateDatabaseCom;
import com.example.sennevervaecke.crossexperience.view.fragment.CompetitionFragment;
import com.example.sennevervaecke.crossexperience.view.fragment.CourseFragment;
import com.example.sennevervaecke.crossexperience.view.fragment.ElementFragment;

import java.util.ArrayList;

public class StandardFlowActivity extends AppCompatActivity implements CompetitionFragmentCom, CourseFragmentCom, UpdateDatabaseCom {

    private CompetitionFragment competitionFragment;
    private CourseFragment courseFragment;
    private ElementFragment elementFragment;

    private Competition selectedCompetition;
    private Course selectedCourse;

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
            elementFragment = new ElementFragment();

            startCompetitionFragment();
        }
    }

    @Override
    public void onCompetitionItemClick(Competition competition){
        selectedCompetition = competition;
        startCourseFragment(competition);
    }
    @Override
    public void onCourseItemClick( Course course) {
        selectedCourse = course;
        startElementFragment(course.getElements());
    }
    public void startCompetitionFragment(){
        getSupportActionBar().setTitle(R.string.app_name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportFragmentManager().beginTransaction().replace(R.id.standardFlowBaseContainer, competitionFragment, "competition").commit();
    }
    public void startCourseFragment(Competition competition){
        getSupportActionBar().setTitle(Helper.toCamelCase(selectedCompetition.getName()));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        courseFragment.setCourses(competition.getCourses());
        getSupportFragmentManager().beginTransaction().replace(R.id.standardFlowBaseContainer, courseFragment, "course").commit();
    }
    public void startElementFragment(ArrayList<Element> elements){
        getSupportActionBar().setTitle(Helper.toCamelCase(selectedCompetition.getName() + " " + selectedCourse.getLevel()));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        elementFragment.setElements(elements);
        getSupportFragmentManager().beginTransaction().replace(R.id.standardFlowBaseContainer, elementFragment, "element").commit();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            Fragment fragment;
            //check if current fragment is course
            fragment = fragmentManager.findFragmentByTag("course");
            if(fragment != null && fragment.isVisible()){
                startCompetitionFragment();
                return super.onOptionsItemSelected(item);
            }
            //check if current fragment is element
            fragment = fragmentManager.findFragmentByTag("element");
            if(fragment != null && fragment.isVisible()){
                //something wrong
                //getSupportFragmentManager().beginTransaction().replace(R.id.standardFlowBaseContainer, courseFragment, "course").commit();
                startCourseFragment(selectedCompetition);
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

        if(competitionFragment.isAdded()) {
            getSupportFragmentManager().putFragment(outState, Constant.TAG_COMPETITION, competitionFragment);
        }
        if(courseFragment.isAdded()) {
            getSupportFragmentManager().putFragment(outState, Constant.TAG_COURSE, courseFragment);
        }
        if(elementFragment.isAdded()) {
            getSupportFragmentManager().putFragment(outState, Constant.TAG_ELEMENT, elementFragment);
        }

        outState.putSerializable(Constant.KEY_SELECTED_COMPETITION, selectedCompetition);
        outState.putSerializable(Constant.KEY_SELECTED_COURSE, selectedCourse);

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {

        selectedCompetition = (Competition) savedInstanceState.getSerializable(Constant.KEY_SELECTED_COMPETITION);
        selectedCourse = (Course) savedInstanceState.getSerializable(Constant.KEY_SELECTED_COURSE);

        FragmentManager fm = getSupportFragmentManager();
        competitionFragment = (CompetitionFragment) fm.getFragment(savedInstanceState, Constant.TAG_COMPETITION);
        if(competitionFragment == null){
            competitionFragment = new CompetitionFragment();
        }

        courseFragment = (CourseFragment) fm.getFragment(savedInstanceState, Constant.TAG_COURSE);
        if(courseFragment == null){
            courseFragment = new CourseFragment();
        } else {
            getSupportActionBar().setTitle(selectedCompetition.getName());
        }
        elementFragment = (ElementFragment) fm.getFragment(savedInstanceState, Constant.TAG_ELEMENT);
        if(elementFragment == null){
            elementFragment = new ElementFragment();
        } else {
            getSupportActionBar().setTitle(selectedCompetition.getName() + " " + selectedCourse.getLevel());
        }

        //TODO title
        //set back arrow
        if(getSupportFragmentManager().findFragmentByTag(Constant.TAG_COMPETITION) == null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
//        else {
//            getSupportActionBar().setTitle(R.string.app_name);
//        }

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

