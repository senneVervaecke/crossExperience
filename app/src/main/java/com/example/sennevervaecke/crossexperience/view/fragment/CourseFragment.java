package com.example.sennevervaecke.crossexperience.view.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.sennevervaecke.crossexperience.R;
import com.example.sennevervaecke.crossexperience.model.Constant;
import com.example.sennevervaecke.crossexperience.model.interfaces.CourseFragmentCom;
import com.example.sennevervaecke.crossexperience.model.Competition;
import com.example.sennevervaecke.crossexperience.model.Course;
import com.example.sennevervaecke.crossexperience.model.database.DatabaseHelper;
import com.example.sennevervaecke.crossexperience.view.activity.CourseAdapter;

import java.io.Serializable;
import java.util.ArrayList;

public class CourseFragment extends Fragment implements AdapterView.OnItemClickListener, Serializable {
    private CourseFragmentCom communication;
    private ArrayList<Course> courses;
    private DatabaseHelper databaseHelper;

    public CourseFragment() {
        // Required empty public constructor
    }


    public void setCourses(ArrayList<Course> courses) {
        this.courses = courses;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        if(savedInstanceState != null) {
            courses = (ArrayList<Course>) savedInstanceState.getSerializable(Constant.KEY_SELECTED_COURSES);
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        databaseHelper = new DatabaseHelper(getContext());
        View view = inflater.inflate(R.layout.fragment_reeks, container, false);
        ListView listView = view.findViewById(R.id.reeksListView);
        if(courses != null) {
            CourseAdapter adapter = new CourseAdapter(getContext(), courses);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(this);
        }
        else{
            Log.e("reeksFragment", "competition = null, propably courseFragment.setCompetition(Competition competition) is not called.");
        }
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        try {
            communication = (CourseFragmentCom) activity;
        }
        catch (ClassCastException e){
            e.printStackTrace();
        }
        super.onAttach(activity);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        communication.onCourseItemClick(courses.get(i));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(Constant.KEY_SELECTED_COURSES, courses);
        super.onSaveInstanceState(outState);
    }
}
