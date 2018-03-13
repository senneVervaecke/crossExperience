package com.example.sennevervaecke.crossexperience.view.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.sennevervaecke.crossexperience.R;

/**
 * Created by sennevervaecke on 3/3/2018.
 */

public class DownloadFragment extends Fragment {
    private ProgressBar progressBar;

    public DownloadFragment(){}

    public void setProgress(int progress){
        if(progressBar != null) {
            progressBar.setProgress(progress);
        }
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
         View view = inflater.inflate(R.layout.fragment_download, container, false);
         progressBar = view.findViewById(R.id.downloadProgressBar);
         progressBar.setProgress(0);
         progressBar.setMax(100);

         return view;
    }
    @Override
    public void onDetach() {
        super.onDetach();
        setProgress(0);
    }
}
