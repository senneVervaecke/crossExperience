package com.example.sennevervaecke.crossexperience.view.fragment;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.sennevervaecke.crossexperience.R;
import com.example.sennevervaecke.crossexperience.controller.interfaces.WedstrijdFragmentCom;
import com.example.sennevervaecke.crossexperience.model.LocalDB;
import com.example.sennevervaecke.crossexperience.model.Wedstrijd;
import com.example.sennevervaecke.crossexperience.view.activity.WedstrijdAdapter;

import java.util.ArrayList;

public class WedstrijdFragment extends Fragment implements AdapterView.OnItemClickListener{

    private ArrayList<Wedstrijd> wedstrijden;
    private WedstrijdFragmentCom communication;

    public WedstrijdFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        wedstrijden = LocalDB.getWedstrijden();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wedstrijd, container, false);
        ListView listView = view.findViewById(R.id.wedstrijdListView);
        WedstrijdAdapter adapter = new WedstrijdAdapter(getContext(), wedstrijden);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            communication = (WedstrijdFragmentCom) activity;
        } catch (ClassCastException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        communication.onWedstrijdItemClick(wedstrijden.get(i));
    }
}
