package com.example.sennevervaecke.crossexperience.controller;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

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

    private Context context;
    private Session session = null;
    private ChannelSftp channel = null;
    private JSch ssh = null;
    private String path;
    private String fileName;
    private Handler handler;
    private Competition competition;
    private Course course;

    private static final String HOST = "10.3.50.40";
    private final String USERNAME = "appuser";
    private final String PASSWORD = "crossexp";

    public DownloadCompetitionTask(Context context, Handler handler, Competition competition, Course course, String fileType){
        this.context = context;
        this.path = "/crossexperience/wedstrijden/" + competition.getName() + "/" + course.getLevel() + fileType;
        this.fileName = competition.getName() + "_" +  course.getLevel() + fileType;

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
        try {
            session = ssh.getSession(USERNAME, HOST, 22);
            session.setPassword(PASSWORD);
            //security issue
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            String remoteFile = path;
            File downloadFile = new File(context.getFilesDir(), fileName);
            OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(downloadFile));

            channel = (ChannelSftp) session.openChannel("sftp");
            channel.connect();
            channel.get(remoteFile, outputStream, new DownloadMonitor(handler));

        } catch (JSchException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (SftpException e) {
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
        //replace this to DownloadMonitor maybe ???
        Message.obtain(handler, DownloadMonitor.END).sendToTarget();
        //LocalDB.setReadyState(competition.getId(), course.getId(), true);
    }
}
