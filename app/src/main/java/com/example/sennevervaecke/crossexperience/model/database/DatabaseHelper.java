package com.example.sennevervaecke.crossexperience.model.database;

import android.content.Context;

import com.example.sennevervaecke.crossexperience.model.Adress;
import com.example.sennevervaecke.crossexperience.model.Competition;
import com.example.sennevervaecke.crossexperience.model.Course;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by sennevervaecke on 3/16/2018.
 */

public class DatabaseHelper {
    private Database db;

    public DatabaseHelper(Context context){
        db = Database.getAppDatabase(context);
    }
    public void fill(){
        db.adressDAO().insert(new Adress(1, "belgie", "waregem", 4520, "koersstraat", 45));

        db.competitionDAO().insert(new CompetitionEntity(1, "waregem", 1, new Date(), new Date()),
                new CompetitionEntity(2, "nokere", 1, new Date(), new Date()));

        db.courseDAO().insert(new CourseEntity(1, "midden", 1450, 520, 1),
                new CourseEntity(2, "zwaar", 1600, 550, 1),
                new CourseEntity(3, "midden", 2100, 520, 2),
                new CourseEntity(4, "zwaar", 2300, 550, 2));
    }

    public Competition getCompetition(int id){
        CompetitionEntity competitionEntity = db.competitionDAO().get(id);
        Competition competition = new Competition(competitionEntity);
        competition.setAdress(db.adressDAO().get(competitionEntity.getAdressId()));
        List<CourseEntity> courseEntities = db.courseDAO().getByCompetitionId(competitionEntity.getId());
        ArrayList<Course> courses = new ArrayList<>();
        for(int i = 0; i < courseEntities.size(); i++){
            courses.add(new Course(courseEntities.get(i)));
        }
        competition.setCourses(courses);

        return competition;
    }

    public ArrayList<Competition> getAllCompetitions(){
        ArrayList<Competition> competitions = new ArrayList<>();
        List<CompetitionEntity> competitionEntities = db.competitionDAO().getAll();

        for (CompetitionEntity competitionEntity:competitionEntities) {
            Competition competition = new Competition(competitionEntity);
            competition.setAdress(db.adressDAO().get(competitionEntity.getAdressId()));
            List<CourseEntity> courseEntities = db.courseDAO().getByCompetitionId(competitionEntity.getId());
            ArrayList<Course> courses = new ArrayList<>();
            for(int i = 0; i < courseEntities.size(); i++){
                courses.add(new Course(courseEntities.get(i)));
            }
            competition.setCourses(courses);
            competitions.add(competition);
        }
        return competitions;
    }

}
