package com.example.sennevervaecke.crossexperience.view.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.sennevervaecke.crossexperience.R;
import com.example.sennevervaecke.crossexperience.controller.DownloadManager;
import com.example.sennevervaecke.crossexperience.model.LocalDB;
import com.example.sennevervaecke.crossexperience.model.Wedstrijd;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //private ArrayList<Wedstrijd> wedstrijden;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(this, DownloadManager.class);
        startService(intent);

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
