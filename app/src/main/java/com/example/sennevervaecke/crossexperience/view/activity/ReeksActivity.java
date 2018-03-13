package com.example.sennevervaecke.crossexperience.view.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.sennevervaecke.crossexperience.R;
import com.example.sennevervaecke.crossexperience.model.LocalDB;
import com.example.sennevervaecke.crossexperience.model.Reeks;
import com.example.sennevervaecke.crossexperience.model.Wedstrijd;

public class ReeksActivity extends AppCompatActivity {

    Wedstrijd wedstrijd = null;
    String cacheFilename = "reeksActivityCache";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        int r1 = LocalDB.wedstrijdPos; int r2 = LocalDB.reeksPos;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reeks);

        wedstrijd = getWedstrijd();

        ListView listView = findViewById(R.id.reeksListview2);
        ReeksAdapter adapter = new ReeksAdapter(this, wedstrijd.getReeksen());
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                Reeks reeks = ((Reeks)adapterView.getItemAtPosition(i));

                LocalDB.reeksPos = reeks.getId();

                Intent intent = new Intent(view.getContext(), PlayerActivity.class);
                intent.putExtra("reeks", reeks);
                intent.putExtra("wedstrijdNaam", wedstrijd.getNaam());
                startActivity(intent);


            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(wedstrijd.getNaam());
    }

    public Wedstrijd getWedstrijd(){
        Wedstrijd wedstrijd;
        Intent intent = getIntent();
        wedstrijd = (Wedstrijd) intent.getSerializableExtra("wedstrijd");
        if(wedstrijd == null){
            if(LocalDB.wedstrijdPos != 0){
                wedstrijd = LocalDB.getWedstrijd(LocalDB.wedstrijdPos);
            }
            //ERROR HANDLING
        }
        return wedstrijd;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        if(item.getItemId() == android.R.id.home){
            LocalDB.wedstrijdPos = 0;
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
