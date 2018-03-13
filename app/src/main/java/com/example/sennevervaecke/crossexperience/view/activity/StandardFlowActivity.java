package com.example.sennevervaecke.crossexperience.view.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.sennevervaecke.crossexperience.R;
import com.example.sennevervaecke.crossexperience.controller.DownloadManager;
import com.example.sennevervaecke.crossexperience.controller.DownloadMonitor;
import com.example.sennevervaecke.crossexperience.controller.interfaces.ReeksFragmentCom;
import com.example.sennevervaecke.crossexperience.controller.interfaces.WedstrijdFragmentCom;
import com.example.sennevervaecke.crossexperience.model.LocalDB;
import com.example.sennevervaecke.crossexperience.model.Reeks;
import com.example.sennevervaecke.crossexperience.model.Wedstrijd;
import com.example.sennevervaecke.crossexperience.view.fragment.PlayerFragment;
import com.example.sennevervaecke.crossexperience.view.fragment.ReeksFragment;
import com.example.sennevervaecke.crossexperience.view.fragment.WedstrijdFragment;

public class StandardFlowActivity extends AppCompatActivity implements WedstrijdFragmentCom, ReeksFragmentCom{

    private WedstrijdFragment wedstrijdFragment;
    private ReeksFragment reeksFragment;
    private PlayerFragment playerFragment;
    private DownloadFragment downloadFragment;

    private DownloadManager downloadManager;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            DownloadManager.DownloadBinder binder = (DownloadManager.DownloadBinder) iBinder;
            downloadManager = binder.getService();
            downloadManager.setHandler(handler);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {}
    };
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == DownloadMonitor.UPDATE){
                downloadFragment.setProgress(msg.arg1);
            }
            else if(msg.what == DownloadMonitor.START){
                downloadFragment.setProgress(0);
                getSupportFragmentManager().beginTransaction().add(R.id.standardFlowDownloadContainer, downloadFragment, "download").commit();
            }
            else if(msg.what == DownloadMonitor.END){
                downloadManager.finishDownload();
                getSupportFragmentManager().beginTransaction().remove(downloadFragment).commit();
                reeksFragment.refresh();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_standard_flow);

        LocalDB.fill();

        Intent serviceIntent = new Intent(this, DownloadManager.class);
        startService(serviceIntent);
        bindService(serviceIntent, connection, getApplicationContext().BIND_AUTO_CREATE);

        wedstrijdFragment = new WedstrijdFragment();
        reeksFragment = new ReeksFragment();
        playerFragment = new PlayerFragment();
        downloadFragment = new DownloadFragment();

        startWedstrijdFragment();
    }

    @Override
    public void onWedstrijdItemClick(Wedstrijd wedstrijd){
        startReeksFragment(wedstrijd);
    }
    @Override
    public void onReeksItemClick(Wedstrijd wedstrijd, Reeks reeks) {
        if(reeks.isReadyState()) {
            startPlayerFragment(wedstrijd, reeks);
        }else{
            if(downloadManager != null){
                downloadManager.startDownload(wedstrijd, reeks);
            }
        }
    }

    public void startWedstrijdFragment(){
        getSupportActionBar().setTitle("crossExperience");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportFragmentManager().beginTransaction().replace(R.id.standardFlowBaseContainer, wedstrijdFragment, "wedstrijd").commit();
    }

    public void startReeksFragment(Wedstrijd wedstrijd){
        getSupportActionBar().setTitle(wedstrijd.getNaam());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        reeksFragment.setWedstrijd(wedstrijd);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.standardFlowBaseContainer, reeksFragment, "reeks");
        transaction.commit();
    }

    public void startPlayerFragment(Wedstrijd wedstrijd, Reeks reeks){
        getSupportActionBar().setTitle(wedstrijd.getNaam() + " " + reeks.getNiveau());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        playerFragment.setWedstrijd(wedstrijd);
        playerFragment.setReeks(reeks);
        getSupportFragmentManager().beginTransaction().replace(R.id.standardFlowBaseContainer, playerFragment, "player").commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            Fragment fragment;
            //check if current fragment is reeks
            fragment = fragmentManager.findFragmentByTag("reeks");
            if(fragment != null && fragment.isVisible()){
                startWedstrijdFragment();
                return super.onOptionsItemSelected(item);
            }
            //check if current fragment is player
            fragment = fragmentManager.findFragmentByTag("player");
            if(fragment != null && fragment.isVisible()){
                PlayerFragment playerFragment = (PlayerFragment) fragment;
                startReeksFragment(playerFragment.getWedstrijd());
                return super.onOptionsItemSelected(item);
            }
        }
        return super.onOptionsItemSelected(item);
    }

}

