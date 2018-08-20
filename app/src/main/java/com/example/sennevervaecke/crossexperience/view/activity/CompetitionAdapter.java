package com.example.sennevervaecke.crossexperience.view.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.sennevervaecke.crossexperience.R;
import com.example.sennevervaecke.crossexperience.controller.Helper;
import com.example.sennevervaecke.crossexperience.model.Competition;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by sennevervaecke on 11/22/2017.
 */

public class CompetitionAdapter extends BaseAdapter {

    private ArrayList<Competition> competitions;
    private Context context;
    private LayoutInflater inflater;

    public CompetitionAdapter(Context context, ArrayList<Competition> competitions){
        this.context = context;
        this.competitions = competitions;
        inflater = LayoutInflater.from(this.context);
    }

    public void setCompetitions(ArrayList<Competition> competitions){
        this.competitions = competitions;
    }


    @Override
    public int getCount() {
        return competitions.size();
    }

    @Override
    public Object getItem(int i) {
        return competitions.get(i);
    }

    @Override
    public long getItemId(int i) {
        return competitions.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.wedstrijd_listview, null);
        TextView naam = view.findViewById(R.id.wedstrijdNaam);
        naam.setText(Helper.toCamelCase(competitions.get(i).getName()));

        TextView datum = view.findViewById(R.id.wedstrijdDatum);
        datum.setText(competitions.get(i).getStartDate().get(Calendar.DAY_OF_MONTH) + "/" +
                competitions.get(i).getStartDate().get(Calendar.MONTH) + "/" +
                competitions.get(i).getStartDate().get(Calendar.YEAR) + " - " +
                competitions.get(i).getEndDate().get(Calendar.DAY_OF_MONTH) + "/" +
                competitions.get(i).getEndDate().get(Calendar.MONTH) + "/" +
                competitions.get(i).getEndDate().get(Calendar.YEAR));
        return view;
    }
}
