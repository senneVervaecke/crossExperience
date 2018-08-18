package com.example.sennevervaecke.crossexperience.view.fragment;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.VideoView;

import com.example.sennevervaecke.crossexperience.R;
import com.example.sennevervaecke.crossexperience.model.Competition;
import com.example.sennevervaecke.crossexperience.model.Constant;
import com.example.sennevervaecke.crossexperience.model.Course;

import java.io.Serializable;

public class PlayerFragment extends Fragment implements Serializable {

    private VideoView videoView;
    private MediaController mediaController;
    int position;

    private Competition competition;
    private Course course;

    public PlayerFragment() {
        position = 0;
    }

    public Competition getCompetition() {
        return competition;
    }

    public void setCompetition(Competition competition) {
        this.competition = competition;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if(savedInstanceState != null){
            competition = (Competition) savedInstanceState.getSerializable(Constant.KEY_SELECTED_COMPETITION);
            course = (Course) savedInstanceState.getSerializable(Constant.KEY_SELECTED_COURSE);
            this.position = savedInstanceState.getInt(Constant.KEY_VIDEO_POSITION);
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_player, container, false);

        videoView = view.findViewById(R.id.playerVideoView);
        mediaController = new MediaController(getContext());
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);
        videoView.requestFocus();

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            public void onPrepared(MediaPlayer mediaPlayer) {
                videoView.seekTo(position);
                if (position == 0) {
                    videoView.start();
                }
                // When video Screen change size.
                mediaPlayer.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                    @Override
                    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                        // Re-Set the videoView that acts as the anchor for the MediaController
                        mediaController.setAnchorView(videoView);
                    }
                });

                mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                    @Override
                    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                        return false;
                    }
                });
            }
        });
        if(course != null && competition != null) {
            setVideoURI();
        } else {
            Log.e("PlayerFragment", "oops, did you forget to set course and competition?");
        }

        return view;
    }
    public void setVideoURI(){
        try {
            videoView.setVideoURI(Uri.parse(getContext().getFilesDir() + "/" + competition.getName() + "_" + course.getLevel() + ".mp4"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(Constant.KEY_SELECTED_COMPETITION, competition);
        outState.putSerializable(Constant.KEY_SELECTED_COURSE, course);
        outState.putInt(Constant.KEY_VIDEO_POSITION, videoView.getCurrentPosition());
        super.onSaveInstanceState(outState);
    }
}
