package com.example.sennevervaecke.crossexperience.controller;

import android.content.Context;
import android.os.AsyncTask;

import com.example.sennevervaecke.crossexperience.model.Adress;
import com.example.sennevervaecke.crossexperience.model.Competition;
import com.example.sennevervaecke.crossexperience.model.database.CompetitionEntity;
import com.example.sennevervaecke.crossexperience.model.database.CourseEntity;
import com.example.sennevervaecke.crossexperience.model.database.DatabaseHelper;
import com.example.sennevervaecke.crossexperience.model.interfaces.UpdateDatabaseCom;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by sennevervaecke on 3/18/2018.
 */

public class UpdateDatabaseTask extends AsyncTask<Void, Void, String> {
    private final String baseUrl = "http://www.alsingen.be/cross_experience_api/api.php";
    private Context context;
    private UpdateDatabaseCom com;

    public UpdateDatabaseTask(Context context, UpdateDatabaseCom com){
        this.context = context;
        this.com = com;
    }

    @Override
    protected void onPreExecute() {
        if(!Helper.isInternetConnected(context)){
            this.cancel(true);
        }
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Void... voids) {
        String result = "";
        try {
            URL urls = new URL(baseUrl + "/competition/getallfull");
            HttpURLConnection conn = (HttpURLConnection) urls.openConnection();
            conn.setReadTimeout(150000); //milliseconds
            conn.setConnectTimeout(15000); // milliseconds
            conn.setRequestMethod("GET");

            conn.connect();

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                result = sb.toString();
                Gson gson = new GsonBuilder().registerTypeAdapter(Calendar.class, new TypeAdapter<Calendar>() {
                    @Override
                    public void write(JsonWriter out, Calendar value) throws IOException {

                    }

                    @Override
                    public Calendar read(JsonReader in) throws IOException {
                        String result = in.nextString();
                        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        try {
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(format.parse(result));
                            return calendar;
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                }).create();
                ArrayList<Competition> competitions = gson.fromJson(result, new TypeToken<ArrayList<Competition>>(){}.getType());
                DatabaseHelper db = new DatabaseHelper(context);
                db.deleteAndInsertAll(competitions);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        if(com != null)
            com.onCompleteTask();
        super.onPostExecute(s);
    }
}
