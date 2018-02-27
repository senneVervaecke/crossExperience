package com.example.sennevervaecke.crossexperience;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;

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

    private static final String HOST = "10.3.50.40";
    private final String USERNAME = "appuser";
    private final String PASSWORD = "crossexp";

    public static final int DOWNLOADED = 0;

    public DownloadWedstrijdFile(Context context, Handler handler, String wedstrijdNaam, String reeksNaam, String fileType){
        this.context = context;
        this.path = "/crossexperience/wedstrijden/" + wedstrijdNaam + "/" + reeksNaam + fileType;
        this.fileName = wedstrijdNaam + "_" +  reeksNaam + fileType;
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

    @Override
    protected void onPostExecute(Void aVoid) {
        handler.sendEmptyMessage(DOWNLOADED);
    }
    /* local ftp server
    @Override
    protected Void doInBackground(Void... voids) {
        FTPClient ftpClient = new FTPClient();
        try {
            server = InetAddress.getByName("192.168.1.125");
            ftpClient.connect(server, port);
            ftpClient.login(user, pass);
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

            Log.i("ftp", ftpClient.getReplyString());

            // APPROACH #1: using retrieveFile(String, OutputStream)
            String remoteFile1 = "/wedstrijden/" + path;
            File downloadFile1 = new File(context.getFilesDir(), fileName);
            OutputStream outputStream1 = new BufferedOutputStream(new FileOutputStream(downloadFile1));
            boolean success = ftpClient.retrieveFile(remoteFile1, outputStream1);
            outputStream1.close();

            if (success) {
                Log.i("ftp", "File " + fileName + " has been downloaded successfully.");
            }
            else{
                Log.i("ftp", "File " + fileName + "failed to download.");
            }
        } catch (IOException ex) {
            Log.e("ftp","Error: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            try {
                if (ftpClient.isConnected()) {
                    ftpClient.logout();
                    ftpClient.disconnect();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }
    */
}
