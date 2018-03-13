package com.example.sennevervaecke.crossexperience.controller;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.jcraft.jsch.SftpProgressMonitor;


/**
 * Created by sennevervaecke on 2/27/2018.
 */

public class DownloadMonitor implements SftpProgressMonitor {

    public static final int START = 0;
    public static final int END = 1;
    public static final int UPDATE = 2;

    private long max;
    private long progressByte;
    private int progressPercent;
    private Handler handler;

    public DownloadMonitor(Handler handler){
        this.handler = handler;
        progressPercent = 0;
        progressByte = 0;
    }

    @Override
    public void init(int i, String s, String s1, long l) {
        Message.obtain(handler, START).sendToTarget();
        max = l;
    }

    @Override
    public boolean count(long l) {
        progressByte += l;

        int newProgressPercent = (int)((progressByte * 100) / max);
        if(newProgressPercent > progressPercent){
            progressPercent = newProgressPercent;
            Message.obtain(handler, UPDATE, progressPercent, 0).sendToTarget();
        }
        return true;
    }

    @Override
    public void end() {}
}
