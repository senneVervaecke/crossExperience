package com.example.sennevervaecke.crossexperience.model.database;

import android.content.Context;

import com.example.sennevervaecke.crossexperience.model.Adress;
import com.example.sennevervaecke.crossexperience.model.Competition;
import com.example.sennevervaecke.crossexperience.model.Course;
import com.example.sennevervaecke.crossexperience.model.Element;

import java.io.File;
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
    public DatabaseHelper(Database database){
        db= database;
    }

    //TODO remove
    public void fill(ArrayList<CompetitionEntity> competitionEntities, ArrayList<CourseEntity> courseEntities, ArrayList<Adress> adresses){
//        nukeAll();
//
//        db.adressDAO().insert(adresses.toArray(new Adress[]{}));
//        db.competitionDAO().insert(competitionEntities.toArray(new CompetitionEntity[]{}));
//        db.courseDAO().insert(courseEntities.toArray(new CourseEntity[]{}));
        return;
    }

    //TODO remove
    private void nukeAll(){
//        db.adressDAO().nuke();
//        db.courseDAO().nuke();
//        db.competitionDAO().nuke();
        return;
    }

    public Competition getCompetition(int id){
        CompetitionEntity competitionEntity = db.competitionDAO().get(id);
        if(competitionEntity == null){
            return null;
        }
        return getCompetitionByEntity(competitionEntity);
    }

    private Competition getCompetitionByEntity(CompetitionEntity competitionEntity){
        Competition competition = new Competition(competitionEntity);

        //add address to competition
        competition.setAdress(db.adressDAO().get(competitionEntity.getAdressId()));

        //add courses to competition
        List<CourseEntity> courseEntities = db.courseDAO().getByCompetitionId(competitionEntity.getId());
        for(int i = 0; i < courseEntities.size(); i++){
            Course course = new Course(courseEntities.get(i));
            //add elements to courses
            List<ElementEntity> elementEntities = db.elementDao().getByCourseId(course.getId());
            for(int j = 0; j < elementEntities.size(); j++){
                //add FileGroup to element
                Element element = new Element(elementEntities.get(j), db.fileGroupDao().get(elementEntities.get(j).getFileGroupId()));
                //add FileNames to FileGroup
                List<FileName> fileNames = db.fileNameDao().getByFileGroupId(element.getFiles().getId());
                for(int k = 0; k < fileNames.size(); k++){
                    element.getFiles().addFileName(fileNames.get(k).getFileName());
                }
                course.addElement(element);
            }
            competition.addCourse(course);
        }
        return competition;
    }

    public ArrayList<Competition> getAllCompetitions(){
        ArrayList<Competition> competitions = new ArrayList<>();
        List<CompetitionEntity> competitionEntities = db.competitionDAO().getAll();
        for(int i = 0; i < competitionEntities.size(); i++){
            competitions.add(getCompetitionByEntity(competitionEntities.get(i)));
        }
        return competitions;
    }
    public ArrayList<Competition> getCompetitionsBetween(Calendar startDate, Calendar endDate){
        ArrayList<Competition> competitions = new ArrayList<>();
        List<CompetitionEntity> competitionEntities = db.competitionDAO().getBetween(startDate.getTimeInMillis(), endDate.getTimeInMillis());
        for(int i = 0; i < competitionEntities.size(); i++){
            competitions.add(getCompetitionByEntity(competitionEntities.get(i)));
        }
        return competitions;
    }
    //TODO remove
    public boolean checkReadyState(Competition competition, Course course, String fileExtension){
//        File file = new File(context.getFilesDir() + "/" + competition.getName() + "_" + course.getLevel() + fileExtension);
//        boolean exist = file.exists();
//        return exist;
        return false;
    }

}
