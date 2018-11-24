package com.example.sennevervaecke.crossexperience.model.database;

import android.content.Context;

import com.example.sennevervaecke.crossexperience.model.Adress;
import com.example.sennevervaecke.crossexperience.model.Competition;
import com.example.sennevervaecke.crossexperience.model.Course;
import com.example.sennevervaecke.crossexperience.model.Element;

import java.util.ArrayList;
import java.util.Calendar;
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

    public void deleteAndInsertAll(ArrayList<Competition> competitions){
        nukeAll();
        insertAll(competitions);
    }

    private void nukeAll(){
        db.adressDAO().nuke();
        db.fileNameDao().nuke();
        db.fileGroupDao().nuke();
        db.elementDao().nuke();
        db.courseDAO().nuke();
        db.competitionDAO().nuke();
    }

    public void insertAll(ArrayList<Competition> competitions){
        ArrayList<CompetitionEntity> competitionEntities = new ArrayList<>();
        ArrayList<CourseEntity> courseEntities = new ArrayList<>();
        ArrayList<Adress> addresses = new ArrayList<>();
        ArrayList<ElementEntity> elementEntities = new ArrayList<>();
        ArrayList<FileGroupEntity> fileGroupEntities = new ArrayList<>();
        ArrayList<FileName> fileNames = new ArrayList<>();
        int count = 1;

        for(Competition competition : competitions){
            competitionEntities.add(new CompetitionEntity(competition));
            addresses.add(competition.getAdress());
            for(Course course : competition.getCourses()){
                courseEntities.add(new CourseEntity(course, competition.getId()));
                for(Element element : course.getElements()){
                    elementEntities.add(new ElementEntity(element, course.getId()));
                    fileGroupEntities.add(new FileGroupEntity(element.getFiles()));
                    for(String fileName : element.getFiles().getFilenames()){
                        fileNames.add(new FileName(count, fileName, element.getFiles().getId()));
                        count++;
                    }
                }
            }
        }
        insertAll(competitionEntities, courseEntities, addresses, elementEntities, fileGroupEntities, fileNames);
    }
    public void insertAll(ArrayList<CompetitionEntity> competitionEntities, ArrayList<CourseEntity> courseEntities, ArrayList<Adress> adresses, ArrayList<ElementEntity> elementEntities, ArrayList<FileGroupEntity> fileGroupEntities, ArrayList<FileName> fileNames){
        db.adressDAO().insert(adresses.toArray(new Adress[]{}));
        db.competitionDAO().insert(competitionEntities.toArray(new CompetitionEntity[]{}));
        db.courseDAO().insert(courseEntities.toArray(new CourseEntity[]{}));
        db.fileGroupDao().insert(fileGroupEntities.toArray(new FileGroupEntity[]{}));
        db.elementDao().insert(elementEntities.toArray(new ElementEntity[]{}));
        db.fileNameDao().insert(fileNames.toArray(new FileName[]{}));
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
