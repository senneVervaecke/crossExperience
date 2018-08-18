package com.example.sennevervaecke.crossexperience.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;

import com.example.sennevervaecke.crossexperience.R;
import com.example.sennevervaecke.crossexperience.model.Competition;
import com.example.sennevervaecke.crossexperience.model.Constant;
import com.example.sennevervaecke.crossexperience.model.Course;

import java.io.Serializable;

import fi.finwe.orion360.OrionVideoView;

public class OrionPlayerFragment extends Fragment implements Serializable {

    private Competition competition;
    private Course course;
    private OrionVideoView orionVideoView;
    private MediaController mediaController;
    private GestureDetector gestureDetector;

    public OrionPlayerFragment() {
        // Required empty public constructor
    }

    public void setCompetition(Competition competition){
        this.competition = competition;
    }
    public void setCourse(Course course){
        this.course = course;
    }
    public void set(Competition competition, Course course){
        this.competition = competition;
        this.course = course;
    }
    public Competition getCompetition(){
        return competition;
    }
    public Course getCourse(){
        return course;
    }

    public void onCreate(Bundle savedInstanceState) {
        if(savedInstanceState != null){
            competition = (Competition) savedInstanceState.getSerializable(Constant.KEY_SELECTED_COMPETITION);
            course = (Course) savedInstanceState.getSerializable(Constant.KEY_SELECTED_COURSE);
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_orion_player, container, false);
        orionVideoView = view.findViewById(R.id.orionVideoView);

        orionVideoView.setOnPreparedListener(new OrionVideoView.OnPreparedListener() {
            @Override
            public void onPrepared(OrionVideoView orionVideoView) {
                orionVideoView.start();
                mediaController.show();
            }
        });

        mediaController = new MediaController(view.getContext());
        mediaController.setMediaPlayer(orionVideoView);
        mediaController.setAnchorView(orionVideoView);

        try {
            orionVideoView.prepare(getContext().getFilesDir() + "/" + competition.getName() + "_" + course.getLevel() + ".mp4");
        } catch (OrionVideoView.LicenseVerificationException e) {
            Log.e("OrionVideoView", "Orion360 SDK license could not be verified!", e);
        }

        orionVideoView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });

        // Toggle media controls by tapping the screen.
        gestureDetector = new GestureDetector(view.getContext(),
                new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onSingleTapConfirmed(MotionEvent e) {
                        if (mediaController.isShowing()) {
                            mediaController.hide();
                        } else {
                            mediaController.show();
                        }
                        return true;
                    }
                });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        orionVideoView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        orionVideoView.onResume();
    }

    @Override
    public void onPause() {
        orionVideoView.onPause();
        super.onPause();
    }

    @Override
    public void onStop() {
        orionVideoView.onStop();
        super.onStop();
    }

    @Override
    public void onDestroy() {
        orionVideoView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(Constant.KEY_SELECTED_COMPETITION, competition);
        outState.putSerializable(Constant.KEY_SELECTED_COURSE, course);
        super.onSaveInstanceState(outState);
    }
}