package com.example.sennevervaecke.crossexperience.view.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.sennevervaecke.crossexperience.R;
import com.example.sennevervaecke.crossexperience.controller.interfaces.ReeksFragmentCom;
import com.example.sennevervaecke.crossexperience.model.LocalDB;
import com.example.sennevervaecke.crossexperience.model.Wedstrijd;
import com.example.sennevervaecke.crossexperience.view.activity.ReeksAdapter;

public class ReeksFragment extends Fragment implements AdapterView.OnItemClickListener
{
    private ReeksFragmentCom communication;
    private Wedstrijd wedstrijd;

    public ReeksFragment() {
        // Required empty public constructor
    }

    public Wedstrijd getWedstrijd() {
        return wedstrijd;
    }

    public void setWedstrijd(Wedstrijd wedstrijd) {
        this.wedstrijd = wedstrijd;
    }

    public void refresh(){
        if(this.isAdded() && wedstrijd != null){
            wedstrijd = LocalDB.getWedstrijd(wedstrijd.getNaam());
            ListView listView = getView().findViewById(R.id.reeksListView);
            ReeksAdapter adapter = new ReeksAdapter(getContext(), wedstrijd.getReeksen());
            listView.setAdapter(adapter);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_reeks, container, false);
        ListView listView = view.findViewById(R.id.reeksListView);
        if(wedstrijd != null) {
            ReeksAdapter adapter = new ReeksAdapter(getContext(), wedstrijd.getReeksen());
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(this);
        }
        else{
            Log.e("reeksFragment", "wedstrijd = null, propably reeksFragment.setWedstrijd(Wedstrijd wedstrijd) is not called.");
        }
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        try {
            communication = (ReeksFragmentCom) activity;
        }
        catch (ClassCastException e){
            e.printStackTrace();
        }
        super.onAttach(activity);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        communication.onReeksItemClick(wedstrijd, wedstrijd.getReeksen().get(i));
    }
}
