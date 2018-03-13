package com.example.sennevervaecke.crossexperience.controller;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import com.example.sennevervaecke.crossexperience.model.LocalDB;
import com.example.sennevervaecke.crossexperience.model.Reeks;
import com.example.sennevervaecke.crossexperience.model.Wedstrijd;
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
public class DownloadWedstrijdFile extends AsyncTask<Void, Void, Void> {

    private Context context;
    private Session session = null;
    private ChannelSftp channel = null;
    private JSch ssh = null;
    private String path;
    private String fileName;
    private Handler handler;
    private Wedstrijd wedstrijd;
    private Reeks reeks;

    private static final String HOST = "10.3.50.40";
    private final String USERNAME = "appuser";
    private final String PASSWORD = "crossexp";

    public DownloadWedstrijdFile(Context context, Handler handler, Wedstrijd wedstrijd, Reeks reeks, String fileType){
        this.context = context;
        this.path = "/crossexperience/wedstrijden/" + wedstrijd.getNaam() + "/" + reeks.getNiveau() + fileType;
        this.fileName = wedstrijd.getNaam() + "_" +  reeks.getNiveau() + fileType;

        this.wedstrijd = wedstrijd;
        this.reeks = reeks;
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

    public Wedstrijd getWedstrijd() {
        return wedstrijd;
    }

    public Reeks getReeks() {
        return reeks;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        //replace this to DownloadMonitor maybe ???
        Message.obtain(handler, DownloadMonitor.END).sendToTarget();
        LocalDB.setReadyState(wedstrijd.getId(), reeks.getId(), true);
    }
}
