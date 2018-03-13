package com.example.sennevervaecke.crossexperience.view.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.VideoView;
import android.media.MediaPlayer.OnPreparedListener;

import com.example.sennevervaecke.crossexperience.R;
import com.example.sennevervaecke.crossexperience.controller.DownloadManager;
import com.example.sennevervaecke.crossexperience.model.LocalDB;
import com.example.sennevervaecke.crossexperience.model.Reeks;


public class PlayerActivity extends AppCompatActivity {

    private VideoView videoView;
    private int position = 0;
    private MediaController mediaController;
    ProgressBar progressBar;
    private Reeks reeks;
    private String wedstrijdNaam;
    private DownloadManager downloadManager;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.e("service", "onConnect");
            DownloadManager.DownloadBinder binder = (DownloadManager.DownloadBinder) iBinder;
            downloadManager = binder.getService();
            if(!reeks.isReadyState()){
                Log.e("service", "start download");
                startVideoDownload();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        Intent intent = getIntent();
        reeks = (Reeks) intent.getSerializableExtra("reeks");
        wedstrijdNaam = intent.getStringExtra("wedstrijdNaam");

        Intent serviceIntent = new Intent(this, DownloadManager.class);
        Log.e("service", "before bind");
        bindService(serviceIntent, connection, getApplicationContext().BIND_AUTO_CREATE);


        if(reeks.isReadyState()){
            setVideoview();
            setVideoURI();
        }
        else{
            setVideoview();
           }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void startVideoDownload(){
        //downloadManager.startDownload(wedstrijdNaam, reeks.getNiveau());

    }

    //always first setVideoView!!!
    public void setVideoURI(){
        try {
            // ID of video file.
            //int id = this.getRawResIdByName(wedstrijdNaam + "_" + reeks.getNiveau());
            videoView.setVideoURI(Uri.parse(getFilesDir() + "/" + wedstrijdNaam + "_" + reeks.getNiveau() + ".mp4"));

        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
    }

    private void setVideoview(){
        videoView = findViewById(R.id.crossPlayer);
        if (mediaController == null) {
            mediaController = new MediaController(PlayerActivity.this);

            // Set the videoView that acts as the anchor for the MediaController.
            mediaController.setAnchorView(videoView);


            // Set MediaController for VideoView
            videoView.setMediaController(mediaController);
        }


        videoView.requestFocus();


        // When the video file ready for playback.
        videoView.setOnPreparedListener(new OnPreparedListener() {

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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        if(item.getItemId() == android.R.id.home){
            LocalDB.reeksPos = 0;
            Intent intent = new Intent(this, ReeksActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    // When you change direction of phone, this method will be called.
    // It store the state of video (Current position)
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        // Store current position.
        savedInstanceState.putInt("CurrentPosition", videoView.getCurrentPosition());
        videoView.pause();

        if(progressBar != null && progressBar.getVisibility() == View.VISIBLE){
            savedInstanceState.putInt("currentprogress", progressBar.getProgress());
        }
    }

    // After rotating the phone. This method is called.
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // Get saved position.
        position = savedInstanceState.getInt("CurrentPosition");
        videoView.seekTo(position);

        int progress = savedInstanceState.getInt("currentprogress", 0);
        progressBar.setProgress(progress);
    }
}

//source: http://o7planning.org/en/10487/android-mediaplayer-and-videoview-tutorial
