package com.example.sennevervaecke.crossexperience;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ProgressBar;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;

/**
 * Created by sennevervaecke on 12/23/2017.
 */
public class DownloadWedstrijdFile extends AsyncTask<Void, Void, Void> {

    private Context context;
    private Handler handler;
    private double progres = 0;
    private double progresPercent;
    private double totaal = 100;
    String path;
    String fileName;

    public static final int DOWNLOADED = 0;

    public DownloadWedstrijdFile(Context context, Handler handler, String wedstrijdNaam, String reeksNaam, String fileType){
        this.context = context;
        this.handler = handler;
        this.path = wedstrijdNaam + "/" + reeksNaam + fileType;
        this.fileName = wedstrijdNaam + "_" +  reeksNaam + fileType;
    }

    InetAddress server;
    int port = 45788;
    String user = "appUser";
    String pass = "";
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

            /*
            // APPROACH #2: using InputStream retrieveFileStream(String)
            String remoteFile2 = "/wedstrijden/" + path;
            File downloadFile2 = new File(context.getFilesDir(), fileName);
            OutputStream outputStream2 = new BufferedOutputStream(new FileOutputStream(downloadFile2));
            InputStream inputStream = ftpClient.retrieveFileStream(remoteFile2);
            totaal = inputStream.available() / 4096;
            byte[] bytesArray = new byte[4096];
            int bytesRead = -1;
            while ((bytesRead = inputStream.read(bytesArray)) != -1) {
                outputStream2.write(bytesArray, 0, bytesRead);
                publishProgress();
                //wait(10);
            }

            boolean success = ftpClient.completePendingCommand();
            if (success) {
                Log.i("ftp", "File " + fileName + " has been downloaded successfully.");
            }
            else{
                Log.i("ftp", "File " + fileName + "failed to download.");
            }
            outputStream2.close();
            inputStream.close();
*/
        } catch (IOException ex) {
            Log.e("ftp","Error: " + ex.getMessage());
            ex.printStackTrace();
        }/* catch (InterruptedException e) {
            e.printStackTrace();
        } */finally {
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

    @Override
    protected void onProgressUpdate(Void... values) {
        /*
        progres++;
        if((int) ((progres / totaal)*100) > (int) progresPercent) {
            progresPercent++;
            handler.sendEmptyMessage(what.PROGRESSUPDATE.ordinal());
        }
        */
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        handler.sendEmptyMessage(DOWNLOADED);
    }
}
