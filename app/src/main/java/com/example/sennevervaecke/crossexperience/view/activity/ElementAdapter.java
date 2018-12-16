package com.example.sennevervaecke.crossexperience.view.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sennevervaecke.crossexperience.R;
import com.example.sennevervaecke.crossexperience.model.Element;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by sennevervaecke on 12/7/2018.
 */

public class ElementAdapter extends BaseAdapter implements Serializable {

    private ArrayList<Element> elements;
    private Context context;
    private LayoutInflater inflater;

    public ElementAdapter(ArrayList<Element> elements, Context context){
        this.elements = elements;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return elements.size();
    }

    @Override
    public Object getItem(int i) {
        return elements.get(i);
    }

    @Override
    public long getItemId(int i) {
        return elements.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.element_listview, null);
        ((TextView) view.findViewById(R.id.elementName)).setText(elements.get(i).getName());
        ImageView icon = view.findViewById(R.id.elementIcon);
        switch (elements.get(i).getFiles().getDataType()){
            case VIDEO:
                icon.setImageResource(R.drawable.ic_video);
                break;
            case VIDEO360:
                icon.setImageResource(R.drawable.ic_360video);
                break;
            case IMAGE:
                icon.setImageResource(R.drawable.ic_image);
                break;
            case SLIDESHOW:
                icon.setImageResource(R.drawable.ic_gallery);
                break;
            case PDF:
                icon.setImageResource(R.drawable.ic_pdf);
                break;
            case OTHER:
                break;
        }

        return view;
    }
}
