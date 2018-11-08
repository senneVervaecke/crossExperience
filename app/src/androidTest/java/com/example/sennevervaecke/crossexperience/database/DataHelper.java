package com.example.sennevervaecke.crossexperience.database;

import com.example.sennevervaecke.crossexperience.model.Adress;
import com.example.sennevervaecke.crossexperience.model.Competition;
import com.example.sennevervaecke.crossexperience.model.Course;
import com.example.sennevervaecke.crossexperience.model.DataType;
import com.example.sennevervaecke.crossexperience.model.Element;
import com.example.sennevervaecke.crossexperience.model.FileGroup;
import com.example.sennevervaecke.crossexperience.model.database.CompetitionEntity;
import com.example.sennevervaecke.crossexperience.model.database.CourseEntity;
import com.example.sennevervaecke.crossexperience.model.database.ElementEntity;
import com.example.sennevervaecke.crossexperience.model.database.FileGroupEntity;
import com.example.sennevervaecke.crossexperience.model.database.FileName;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by sennevervaecke on 10/30/2018.
 */

public class DataHelper {

    private static SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");

    //ENTITIES

    public static Adress getAdress1(){
        return new Adress(1, "belgie", "lennik", 1750, "straat", 12);
    }
    public static Adress getAdress2(){
        return new Adress(2, "belgie", "Brussel", 1000, "street", 99);
    }

    public static CompetitionEntity getCompetitionEntity1(){
        try {
            return new CompetitionEntity(1, "waregem", 1, format.parse("14-07-2018"), format.parse("14-07-2018"));
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static CompetitionEntity getCompetitionEntity2(){
        try {
            return new CompetitionEntity(2, "nokere", 2, format.parse("01-08-2018"), format.parse("03-08-2018"));
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static CourseEntity getCourseEntity1(){
        return new CourseEntity(1, "midden", 3450, 520, 1);
    }
    public static CourseEntity getCourseEntity2(){
        return new CourseEntity(2, "licht", 3210, 480, 1);
    }
    public static CourseEntity getCourseEntity3(){
        return new CourseEntity(3, "midden", 3812, 520, 2);
    }

    public static ElementEntity getElementEntity1(){
        return new ElementEntity(1, "videotje", 1, 1);
    }
    public static ElementEntity getElementEntity2(){
        return new ElementEntity(2, "pdf", 2, 1);
    }
    public static ElementEntity getElementEntity3(){
        return new ElementEntity(3, "360video", 3, 2);
    }

    public static FileGroupEntity getFileGroupEntity1(){
        return new FileGroupEntity(1, "video");
    }
    public static FileGroupEntity getFileGroupEntity2(){
        return new FileGroupEntity(2, "video360");
    }
    public static FileGroupEntity getFileGroupEntity3(){
        return new FileGroupEntity(3, "slideshow");
    }

    public static FileName getFileName1(){
        return new FileName(1, "video_midden_waregem", 1);
    }
    public static FileName getFileName2(){
        return new FileName(2, "notes_midden_waregem", 1);
    }
    public static FileName getFileName3(){
        return new FileName(3, "video_zwaar", 2);
    }

    //MODELS

    public static FileGroup getFileGroup1(){
        ArrayList<String> filenames = new ArrayList<>();
        filenames.add("video_midden_waregem");
        filenames.add("notes_midden_waregem");
        return new FileGroup(1, DataType.VIDEO,filenames);
    }
    public static FileGroup getFileGroup2(){
        ArrayList<String> filenames = new ArrayList<>();
        filenames.add("video_zwaar");
        return new FileGroup(2, DataType.VIDEO360,filenames);
    }
    public static FileGroup getFileGroup3(){
        return new FileGroup(3, DataType.SLIDESHOW,new ArrayList<String>());
    }

    public static Element getElement1(){
        return new Element(1, "videotje", getFileGroup1());
    }
    public static Element getElement2(){
        return new Element(2, "pdf", getFileGroup2());
    }
    public static Element getElement3(){
        return new Element(3, "360video", getFileGroup3());
    }

    public static Course getCourse1(){
        ArrayList<Element> elements = new ArrayList<>();
        elements.add(getElement1());
        elements.add(getElement2());
        return new Course(1, "midden", 3450, 520, elements);
    }
    public static Course getCourse2(){
        ArrayList<Element> elements = new ArrayList<>();
        elements.add(getElement3());
        return new Course(2, "licht", 3210, 480, elements);
    }
    public static Course getCourse3(){
        ArrayList<Element> elements = new ArrayList<>();
        return new Course(3, "midden", 3812, 520, elements);
    }

    public static Competition getCompetition1() throws ParseException {
        ArrayList<Course> courses = new ArrayList<>();
        courses.add(getCourse1());
        courses.add(getCourse2());
        Calendar startDate = Calendar.getInstance();
        startDate.setTime(format.parse("14-07-2018"));
        Calendar endDate = Calendar.getInstance();
        endDate.setTime(format.parse("14-07-2018"));
        return new Competition(1, "waregem", getAdress1(), startDate, endDate, courses);
    }
    public static Competition getCompetition2() throws ParseException {
        ArrayList<Course> courses = new ArrayList<>();
        courses.add(getCourse3());
        Calendar startDate = Calendar.getInstance();
        startDate.setTime(format.parse("01-08-2018"));
        Calendar endDate = Calendar.getInstance();
        endDate.setTime(format.parse("03-08-2018"));
        return new Competition(2, "nokere", getAdress2(), startDate, endDate, courses);
    }
}
