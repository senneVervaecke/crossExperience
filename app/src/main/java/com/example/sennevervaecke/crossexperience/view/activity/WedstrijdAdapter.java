package com.example.sennevervaecke.crossexperience.view.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.sennevervaecke.crossexperience.R;
import com.example.sennevervaecke.crossexperience.model.Wedstrijd;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by sennevervaecke on 11/22/2017.
 */

public class WedstrijdAdapter extends BaseAdapter {

    private ArrayList<Wedstrijd> wedstrijden;
    private Context context;
    private LayoutInflater inflater;

    public WedstrijdAdapter(Context context, ArrayList<Wedstrijd> wedstrijden){
        this.context = context;
        this.wedstrijden = wedstrijden;
        inflater = LayoutInflater.from(this.context);
    }


    @Override
    public int getCount() {
        return wedstrijden.size();
    }

    @Override
    public Object getItem(int i) {
        return wedstrijden.get(i);
    }

    @Override
    public long getItemId(int i) {
        return wedstrijden.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.wedstrijd_listview, null);
        TextView naam = view.findViewById(R.id.wedstrijdNaam);
        naam.setText(wedstrijden.get(i).getNaam());

        TextView datum = view.findViewById(R.id.wedstrijdDatum);
        datum.setText(wedstrijden.get(i).getStartDatum().get(Calendar.DAY_OF_MONTH) + "/" +
                wedstrijden.get(i).getStartDatum().get(Calendar.MONTH) + "/" +
                wedstrijden.get(i).getStartDatum().get(Calendar.YEAR) + " - " +
                wedstrijden.get(i).getEindDatum().get(Calendar.DAY_OF_MONTH) + "/" +
                wedstrijden.get(i).getEindDatum().get(Calendar.MONTH) + "/" +
                wedstrijden.get(i).getEindDatum().get(Calendar.YEAR));
        return view;
    }
}
