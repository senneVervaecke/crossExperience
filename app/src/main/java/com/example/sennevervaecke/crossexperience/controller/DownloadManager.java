package com.example.sennevervaecke.crossexperience.controller;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.sennevervaecke.crossexperience.model.Competition;
import com.example.sennevervaecke.crossexperience.model.Course;

import java.util.ArrayList;

/**
 * Created by sennevervaecke on 3/4/2018.
 */

public class DownloadManager extends Service{
    private DownloadCompetitionTask downloadCompetitionTask;
    private ArrayList<DownloadCompetitionTask> queue;
    private Handler handler;
    private final IBinder binder = new DownloadBinder();

    public void startDownload(Competition competition, Course course){
        if(downloadCompetitionTask == null) {
            Log.e("service", "execute download task");
            downloadCompetitionTask = new DownloadCompetitionTask(getApplicationContext(), handler, competition, course, ".mp4");
            downloadCompetitionTask.execute();
        }
        else{
            boolean isStarted = false;
            if(competition.equals(downloadCompetitionTask.getCompetition()) && course.equals(downloadCompetitionTask.getCourse())) {
                isStarted = true;
            }
            for(int i = 0; i < queue.size(); i++){
                if(competition.equals(queue.get(i).getCompetition()) && course.equals(queue.get(i).getCourse())){
                    isStarted = true;
                }
            }
            if(!isStarted) {
                queue.add(new DownloadCompetitionTask(getApplicationContext(), handler, competition, course, ".mp4"));
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
        if(downloadCompetitionTask != null) {
            downloadCompetitionTask.setHandler(handler);
        }
    }

    public Handler getHandler(){
        if(downloadCompetitionTask != null) {
            return downloadCompetitionTask.getHandler();
        }
        return handler;
    }
    public void finishDownload(){
        if(queue.size() == 0){
            downloadCompetitionTask = null;
            return;
        }
        downloadCompetitionTask = queue.remove(0);
        downloadCompetitionTask.execute();
    }
    public class DownloadBinder extends Binder {
        public DownloadManager getService(){
            return DownloadManager.this;
        }
    }
}
