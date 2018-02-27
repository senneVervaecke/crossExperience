package com.example.sennevervaecke.crossexperience;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.VideoView;
import android.media.MediaPlayer.OnPreparedListener;


public class PlayerActivity extends AppCompatActivity {

    private VideoView videoView;
    private int position = 0;
    private MediaController mediaController;
    ProgressBar progressBar;
    Handler handler;
    private Reeks reeks;
    private String wedstrijdNaam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        Intent intent = getIntent();
        reeks = (Reeks) intent.getSerializableExtra("reeks");
        wedstrijdNaam = intent.getStringExtra("wedstrijdNaam");

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(msg.what == DownloadMonitor.UPDATE){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        progressBar.setProgress(msg.arg1, true);
                    }
                    else{
                        progressBar.setProgress(msg.arg1);
                    }
                }
                else if(msg.what == DownloadMonitor.END){
                    progressBar.setVisibility(View.GONE);
                    setVideoURI();
                    LocalDB.setReadyState(LocalDB.wedstrijdPos, LocalDB.reeksPos, true);
                }
            }
        };

        if(reeks.isReadyState()){
            setVideoview();
            setVideoURI();
        }
        else{
            startVideoDownload();
            setVideoview();
           }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    private void startVideoDownload(){
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setMax(100);
        progressBar.setProgress(0);
        new DownloadWedstrijdFile(this, handler, wedstrijdNaam, reeks.getNiveau(), ".mp4").execute();
    }

    //always first setVideoView!!!
    private void setVideoURI(){
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

    // Find ID corresponding to the name of the resource (in the directory raw).
    public int getRawResIdByName(String resName) {
        String pkgName = this.getPackageName();
        // Return 0 if not found.
        int resID = this.getResources().getIdentifier(resName, "raw", pkgName);
        Log.i("AndroidVideoView", "Res Name: " + resName + "==> Res ID = " + resID);
        return resID;
    }


    // When you change direction of phone, this method will be called.
    // It store the state of video (Current position)
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        // Store current position.
        savedInstanceState.putInt("CurrentPosition", videoView.getCurrentPosition());
        videoView.pause();
    }


    // After rotating the phone. This method is called.
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // Get saved position.
        position = savedInstanceState.getInt("CurrentPosition");
        videoView.seekTo(position);
    }

}

//source: http://o7planning.org/en/10487/android-mediaplayer-and-videoview-tutorial
