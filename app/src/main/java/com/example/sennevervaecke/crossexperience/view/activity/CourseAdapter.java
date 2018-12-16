package com.example.sennevervaecke.crossexperience.view.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sennevervaecke.crossexperience.R;
import com.example.sennevervaecke.crossexperience.controller.Helper;
import com.example.sennevervaecke.crossexperience.model.Course;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by sennevervaecke on 11/22/2017.
 */

public class CourseAdapter extends BaseAdapter implements Serializable {

    private ArrayList<Course> courses;
    private Context context;
    private LayoutInflater inflater;

    public CourseAdapter(Context context, ArrayList<Course> courses){
        this.courses = courses;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return courses.size();
    }

    @Override
    public Object getItem(int i) {
        return courses.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.reeks_listview, null);
        TextView level = view.findViewById(R.id.reeksNaam);
        level.setText(Helper.toCamelCase(courses.get(i).getLevel()));

        return view;
    }
}
