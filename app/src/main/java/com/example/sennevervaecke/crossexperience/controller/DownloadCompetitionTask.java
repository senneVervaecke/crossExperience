package com.example.sennevervaecke.crossexperience.controller;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.sennevervaecke.crossexperience.model.Competition;
import com.example.sennevervaecke.crossexperience.model.Course;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * Created by sennevervaecke on 12/23/2017.
 */
public class DownloadCompetitionTask extends AsyncTask<Void, Void, Void> {

    public static final int ERROR = 666;
    public static final int NOFILE = 667;
    public static final int NO360FILE = 668;

    private Context context;
    private Session session = null;
    private ChannelSftp channel = null;
    private JSch ssh = null;
    private String path;
    private String fileName;
    private String fileType;
    private Handler handler;
    private Competition competition;
    private Course course;

    private static final String HOST = "ssh.alsingen.be";
    private final String USERNAME = "alsingen.be";
    private final String PASSWORD = "crossExp2018";

    public DownloadCompetitionTask(Context context, Handler handler, Competition competition, Course course, String fileType){
        this.context = context;
        this.path = "competitions/" + competition.getName().toLowerCase() + "/" + course.getLevel().toLowerCase() + fileType;
        this.fileName = competition.getName() + "_" +  course.getLevel() + fileType;
        this.fileType = fileType;

        this.competition = competition;
        this.course = course;
        this.handler = handler;
    }

    @Override
    protected void onPreExecute() {
        ssh = new JSch();
    }

    @Override
    protected Void doInBackground(Void ... voids) {
        File downloadFile = new File(context.getFilesDir(), fileName);
        try {
            session = ssh.getSession(USERNAME, HOST, 22);
            session.setPassword(PASSWORD);
            //security issue
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            String remoteFile = path;
            OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(downloadFile));

            channel = (ChannelSftp) session.openChannel("sftp");
            channel.connect();
            channel.get(remoteFile, outputStream, new DownloadMonitor(handler));

        } catch (SftpException e) {
            downloadFile.delete();
            if(e.id == 2) {
                if (fileType.equals(".mp4")) {
                    Message.obtain(handler, NOFILE).sendToTarget();
                } else if (fileType.equals("360.mp4")) {
                    Message.obtain(handler, NO360FILE, course.getId(), 0, competition).sendToTarget();
                }
            } else {
                Message.obtain(handler, ERROR).sendToTarget();
            }
        } catch (FileNotFoundException | JSchException e){
            downloadFile.delete();
            e.printStackTrace();
        }
        return null;
    }

    public Handler getHandler() {
        return handler;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public Competition getCompetition() {
        return competition;
    }

    public Course getCourse() {
        return course;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        Message.obtain(handler, DownloadMonitor.END).sendToTarget();
    }
}
