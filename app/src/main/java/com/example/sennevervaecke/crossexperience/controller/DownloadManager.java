package com.example.sennevervaecke.crossexperience.controller;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.sennevervaecke.crossexperience.model.Reeks;
import com.example.sennevervaecke.crossexperience.model.Wedstrijd;

import java.util.ArrayList;

/**
 * Created by sennevervaecke on 3/4/2018.
 */

public class DownloadManager extends Service{
    private DownloadWedstrijdFile downloadWedstrijdFile;
    private ArrayList<DownloadWedstrijdFile> queue;
    private Handler handler;
    private final IBinder binder = new DownloadBinder();

    public void startDownload(Wedstrijd wedstrijd, Reeks reeks){
        if(downloadWedstrijdFile == null) {
            Log.e("service", "execute download task");
            downloadWedstrijdFile = new DownloadWedstrijdFile(getApplicationContext(), handler, wedstrijd, reeks, ".mp4");
            downloadWedstrijdFile.execute();
        }
        else{
            boolean isStarted = false;
            if(wedstrijd.equals(downloadWedstrijdFile.getWedstrijd()) && reeks.equals(downloadWedstrijdFile.getReeks())) {
                isStarted = true;
            }
            for(int i = 0; i < queue.size(); i++){
                if(wedstrijd.equals(queue.get(i).getWedstrijd()) && reeks.equals(queue.get(i).getReeks())){
                    isStarted = true;
                }
            }
            if(!isStarted) {
                queue.add(new DownloadWedstrijdFile(getApplicationContext(), handler, wedstrijd, reeks, ".mp4"));
            }
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        queue = new ArrayList<>();
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.e("service", "bind called");
        return binder;
    }
    public void setHandler(Handler handler){
        this.handler = handler;
        if(downloadWedstrijdFile != null) {
            downloadWedstrijdFile.setHandler(handler);
        }
    }

    public Handler getHandler(){
        if(downloadWedstrijdFile != null) {
            return downloadWedstrijdFile.getHandler();
        }
        return handler;
    }
    public void finishDownload(){
        if(queue.size() == 0){
            downloadWedstrijdFile = null;
            return;
        }
        downloadWedstrijdFile = queue.remove(0);
        downloadWedstrijdFile.execute();
    }
    public class DownloadBinder extends Binder {
        public DownloadManager getService(){
            return DownloadManager.this;
        }
    }
}
