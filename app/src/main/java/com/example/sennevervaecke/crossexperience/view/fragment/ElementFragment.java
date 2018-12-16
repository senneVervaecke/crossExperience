package com.example.sennevervaecke.crossexperience.view.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.sennevervaecke.crossexperience.R;
import com.example.sennevervaecke.crossexperience.model.Constant;
import com.example.sennevervaecke.crossexperience.model.Element;
import com.example.sennevervaecke.crossexperience.view.activity.ElementAdapter;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ElementFragment extends Fragment implements AdapterView.OnItemClickListener, Serializable {

    private String parentName = "";
    private ArrayList<Element> elements;

    public ElementFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        if(savedInstanceState != null){
            elements = (ArrayList<Element>) savedInstanceState.getSerializable(Constant.KEY_SELECTED_ELEMENTS);
        }
        super.onCreate(savedInstanceState);
    }

    public void setElements(ArrayList<Element> elements){
        this.elements = elements;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_element, container, false);
        if(elements != null) {
            ListView listView = view.findViewById(R.id.elementListView);
            listView.setAdapter(new ElementAdapter(elements, getContext()));
            listView.setOnItemClickListener(this);
        }
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(Constant.KEY_SELECTED_ELEMENTS, elements);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }
}
