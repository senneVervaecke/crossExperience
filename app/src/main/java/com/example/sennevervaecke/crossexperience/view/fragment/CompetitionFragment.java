package com.example.sennevervaecke.crossexperience.view.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.sennevervaecke.crossexperience.R;
import com.example.sennevervaecke.crossexperience.model.Constant;
import com.example.sennevervaecke.crossexperience.model.interfaces.CompetitionFragmentCom;
import com.example.sennevervaecke.crossexperience.model.Competition;
import com.example.sennevervaecke.crossexperience.model.database.DatabaseHelper;
import com.example.sennevervaecke.crossexperience.view.activity.CompetitionAdapter;

import java.io.Serializable;
import java.util.ArrayList;

public class CompetitionFragment extends Fragment implements AdapterView.OnItemClickListener, Serializable {

    private ArrayList<Competition> competitions;
    private CompetitionFragmentCom communication;

    public CompetitionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        competitions = new DatabaseHelper(getContext()).getAllCompetitions();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wedstrijd, container, false);
        ListView listView = view.findViewById(R.id.wedstrijdListView);
        CompetitionAdapter adapter = new CompetitionAdapter(getContext(), competitions);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            communication = (CompetitionFragmentCom) activity;
        } catch (ClassCastException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        communication.onCompetitionItemClick(competitions.get(i));
    }
}
