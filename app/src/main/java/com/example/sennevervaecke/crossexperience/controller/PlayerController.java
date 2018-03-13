package com.example.sennevervaecke.crossexperience.controller;

import android.os.Handler;

import com.example.sennevervaecke.crossexperience.view.activity.PlayerActivity;

/**
 * Created by sennevervaecke on 3/3/2018.
 */

public class PlayerController {
    private static Handler handler = null;
    private PlayerActivity playerActivity;

    private static boolean downloadProgress = false;

    public PlayerController(PlayerActivity playerActivity){
        this.playerActivity = playerActivity;
    }
/*
    public Handler getHandler(){
            return new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    if(msg.what == DownloadMonitor.UPDATE){
                        Log.e("progress", String.valueOf(msg.arg1));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            playerActivity.getProgressBar().setProgress(msg.arg1, true);
                        }
                        else{
                            playerActivity.getProgressBar().setProgress(msg.arg1);
                        }
                    }
                    else if(msg.what == DownloadMonitor.END){
                        downloadProgress = false;
                        playerActivity.getProgressBar().setVisibility(View.GONE);
                        playerActivity.setVideoURI();
                        LocalDB.setReadyState(LocalDB.wedstrijdPos, LocalDB.reeksPos, true);
                    }
                }
            };
    }
    */

    public static boolean isDownloadProgress() {
        return downloadProgress;
    }

    public static void setDownloadProgress(boolean downloadProgress) {
        PlayerController.downloadProgress = downloadProgress;
    }
}
