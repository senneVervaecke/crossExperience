package com.example.sennevervaecke.crossexperience.view.fragment;

import android.content.Context;
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
import com.example.sennevervaecke.crossexperience.model.Reeks;
import com.example.sennevervaecke.crossexperience.model.Wedstrijd;
import com.example.sennevervaecke.crossexperience.view.activity.PlayerActivity;

public class PlayerFragment extends Fragment {

    private VideoView videoView;
    private MediaController mediaController;
    int position;

    private Wedstrijd wedstrijd;
    private Reeks reeks;

    public PlayerFragment() {
        position = 0;
        // Required empty public constructor
    }

    public Wedstrijd getWedstrijd() {
        return wedstrijd;
    }

    public void setWedstrijd(Wedstrijd wedstrijd) {
        this.wedstrijd = wedstrijd;
    }

    public Reeks getReeks() {
        return reeks;
    }

    public void setReeks(Reeks reeks) {
        this.reeks = reeks;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
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
            }
        });
        if(reeks != null && wedstrijd != null) {
            setVideoURI();
        } else {
            Log.e("PlayerFragment", "oops, did you forget to set reeks and wedstrijd?");
        }

        return view;
    }
    public void setVideoURI(){
        try {
            videoView.setVideoURI(Uri.parse(getContext().getFilesDir() + "/" + wedstrijd.getNaam() + "_" + reeks.getNiveau() + ".mp4"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
