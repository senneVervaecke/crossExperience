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
import com.example.sennevervaecke.crossexperience.model.interfaces.CourseFragmentCom;
import com.example.sennevervaecke.crossexperience.model.Competition;
import com.example.sennevervaecke.crossexperience.model.Course;
import com.example.sennevervaecke.crossexperience.model.database.DatabaseHelper;
import com.example.sennevervaecke.crossexperience.view.activity.CourseAdapter;

public class CourseFragment extends Fragment implements AdapterView.OnItemClickListener
{
    private CourseFragmentCom communication;
    private Competition competition;
    private DatabaseHelper databaseHelper;

    public CourseFragment() {
        // Required empty public constructor
    }

    public Competition getCompetition() {
        return competition;
    }

    public void setCompetition(Competition competition) {
        this.competition = competition;
    }

    public void updateCoursesReadyStates(Competition competition){
        if(databaseHelper != null && competition != null) {
            for (Course course : competition.getCourses()) {
                course.setReadyState(databaseHelper.checkReadyState(competition, course, ".mp4"));
            }
        }
    }

    public void refresh(){
        if(this.isAdded() && competition != null && databaseHelper != null){
            updateCoursesReadyStates(competition);
            ListView listView = getView().findViewById(R.id.reeksListView);
            CourseAdapter adapter = new CourseAdapter(getContext(), competition.getCourses());
            listView.setAdapter(adapter);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        databaseHelper = new DatabaseHelper(getContext());
        View view = inflater.inflate(R.layout.fragment_reeks, container, false);
        ListView listView = view.findViewById(R.id.reeksListView);
        if(competition != null) {
            updateCoursesReadyStates(competition);
            CourseAdapter adapter = new CourseAdapter(getContext(), competition.getCourses());
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
        communication.onCourseItemClick(competition, competition.getCourses().get(i));
    }
}
