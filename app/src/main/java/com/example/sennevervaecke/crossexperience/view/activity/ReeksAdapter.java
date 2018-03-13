package com.example.sennevervaecke.crossexperience.view.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sennevervaecke.crossexperience.R;
import com.example.sennevervaecke.crossexperience.model.Reeks;

import java.util.ArrayList;

/**
 * Created by sennevervaecke on 11/22/2017.
 */

public class ReeksAdapter extends BaseAdapter {

    private ArrayList<Reeks> reeksen;
    private Context context;
    private LayoutInflater inflater;

    public ReeksAdapter(Context context, ArrayList<Reeks> reeksen){
        this.reeksen = reeksen;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return reeksen.size();
    }

    @Override
    public Object getItem(int i) {
        return reeksen.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.reeks_listview, null);
        TextView niveau = view.findViewById(R.id.reeksNaam);
        niveau.setText(reeksen.get(i).getNiveau());

        ImageView image = view.findViewById(R.id.videoStaatImg);
        if(reeksen.get(i).isReadyState()){
            image.setImageResource(R.drawable.play);
        }
        else{
            image.setImageResource(R.drawable.download);
        }
        return view;
    }
}
