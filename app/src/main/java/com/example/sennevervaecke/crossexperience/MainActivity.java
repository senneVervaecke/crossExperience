package com.example.sennevervaecke.crossexperience;

import android.app.ActionBar;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class MainActivity extends AppCompatActivity {

    //private ArrayList<Wedstrijd> wedstrijden;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(LocalDB.startup) {
            LocalDB.fill();
            LocalDB.startup = false;
        }
        int r1 = LocalDB.wedstrijdPos; int r2 = LocalDB.reeksPos;
        ArrayList<Wedstrijd> wedstrijden = LocalDB.getWedstrijden();

        ListView listView = findViewById(R.id.wedstrijdLijst);

        WedstrijdAdapter adapter = new WedstrijdAdapter(this, wedstrijden);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                Wedstrijd wedstrijd = ((Wedstrijd)adapterView.getItemAtPosition(i));
                LocalDB.wedstrijdPos = wedstrijd.getId();
                Intent intent = new Intent(view.getContext(), ReeksActivity.class);
                intent.putExtra("wedstrijd", wedstrijd);
                startActivity(intent);
            }
        });

    }

}
