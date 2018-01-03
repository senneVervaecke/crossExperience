package com.example.sennevervaecke.crossexperience;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class ReeksActivity extends AppCompatActivity {

    Wedstrijd wedstrijd = null;
    String cacheFilename = "reeksActivityCache";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        int r1 = LocalDB.wedstrijdPos; int r2 = LocalDB.reeksPos;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reeks);

        wedstrijd = getWedstrijd();

        ListView listView = findViewById(R.id.reeksListview);
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
            /*
            try {
                FileInputStream fis = new FileInputStream(new File(getCacheDir(), cacheFilename));
                ObjectInputStream ois = new ObjectInputStream(fis);
                wedstrijd  = (Wedstrijd) ois.readObject();
                ois.close();
                fis.close();
            }
            catch (IOException e){ e.printStackTrace();}
            catch (ClassNotFoundException e){}
            this.deleteFile(cacheFilename);
            */
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

    @Override
    public void onStop(){
        super.onStop();
        /*
        Log.i("onstop", "is called");
        FileOutputStream outputStream;
        ObjectOutputStream objectOutputStream;
        try {
            File file = new File(getCacheDir(), cacheFilename);
            outputStream = new FileOutputStream(file);
            objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(wedstrijd);
            objectOutputStream.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        */
    }
    @Override protected void onDestroy(){
        /*
        Log.e("ondestroy", "is called");
        super.onDestroy();
        this.deleteFile(cacheFilename);
        */
        super.onDestroy();
    }
}
