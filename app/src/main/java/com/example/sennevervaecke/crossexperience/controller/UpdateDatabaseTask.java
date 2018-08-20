package com.example.sennevervaecke.crossexperience.controller;

import android.content.Context;
import android.os.AsyncTask;

import com.example.sennevervaecke.crossexperience.model.Adress;
import com.example.sennevervaecke.crossexperience.model.database.CompetitionEntity;
import com.example.sennevervaecke.crossexperience.model.database.CourseEntity;
import com.example.sennevervaecke.crossexperience.model.database.DatabaseHelper;
import com.example.sennevervaecke.crossexperience.model.interfaces.UpdateDatabaseCom;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by sennevervaecke on 3/18/2018.
 */

public class UpdateDatabaseTask extends AsyncTask<Void, Void, String> {
    private final String baseUrl = "http://www.alsingen.be/cross_experience_api/api.php";
    private Context context;

    private ArrayList<CompetitionEntity> competitions;
    private ArrayList<CourseEntity> courses;
    private ArrayList<Adress> adresses;
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
        courses = new ArrayList<>();
        adresses = new ArrayList<>();

        competitions = getAllCompetitions();
        for(CompetitionEntity competition: competitions){
            for(CourseEntity course: getCourseByCompetitionId(competition.getId())){
                courses.add(course);
            }
            adresses.add(getAdress(competition.getAdressId()));
        }
        new DatabaseHelper(context).fill(competitions, courses, adresses);

        return "succes";
    }

    private ArrayList<CompetitionEntity> getAllCompetitions(){
        String result = "";
        try {
            URL urls = new URL(baseUrl + "/competition/getall");
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
                return new Gson().fromJson(result, new TypeToken<ArrayList<CompetitionEntity>>(){}.getType());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private ArrayList<CourseEntity> getCourseByCompetitionId(int competitionId){
        String result = "";
        try {
            URL urls = new URL(baseUrl + "/course/getbycompetitionid/" + String.valueOf(competitionId));
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
                return new Gson().fromJson(result, new TypeToken<ArrayList<CourseEntity>>(){}.getType());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private Adress getAdress(int id){
        String result = "";
        try {
            URL urls = new URL(baseUrl + "/adress/get/" + String.valueOf(id));
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
                return new Gson().fromJson(result, Adress.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        com.onCompleteTask();
        super.onPostExecute(s);
    }
}
