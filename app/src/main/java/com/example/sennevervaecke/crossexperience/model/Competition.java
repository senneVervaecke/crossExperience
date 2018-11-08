package com.example.sennevervaecke.crossexperience.model;

import com.example.sennevervaecke.crossexperience.model.database.CompetitionEntity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by sennevervaecke on 11/21/2017.
 */

public class Competition implements Serializable{
    private int id;
    private String name;
    private Adress adress;
    private Calendar startDate;
    private Calendar endDate;
    private ArrayList<Course> courses;

    public Competition(int id, String name, Adress adress, Calendar startDate, Calendar endDate, ArrayList<Course> courses) {
        this.id = id;
        this.name = name;
        this.adress = adress;
        this.startDate = startDate;
        this.endDate = endDate;
        this.courses = courses;
    }

    public Competition(int id, String name, Adress adress, Calendar startDate, Calendar endDate) {
        this.id = id;
        this.name = name;
        this.adress = adress;
        this.startDate = startDate;
        this.endDate = endDate;
        this.courses = new ArrayList<>();
    }
    public Competition(CompetitionEntity entity){
        this.id = entity.getId();
        this.name = entity.getName();
        this.adress = null;
        this.startDate = Calendar.getInstance();
        startDate.setTime(entity.getStartDate());
        this.endDate = Calendar.getInstance();
        endDate.setTime(entity.getEndDate());
        this.courses = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Adress getAdress() {
        return adress;
    }

    public void setAdress(Adress adress) {
        this.adress = adress;
    }

    public Calendar getStartDate() {
        return startDate;
    }

    public void setStartDate(Calendar startDate) {
        this.startDate = startDate;
    }

    public Calendar getEndDate() {
        return endDate;
    }

    public void setEndDate(Calendar endDate) {
        this.endDate = endDate;
    }

    public ArrayList<Course> getCourses() {
        return courses;
    }

    public void setCourses(ArrayList<Course> courses) {
        this.courses = courses;
    }

    public void addCourse(Course course){
        this.courses.add(course);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Competition that = (Competition) o;

        if (id != that.id) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (adress != null ? !adress.equals(that.adress) : that.adress != null) return false;
        if (startDate != null ? !startDate.equals(that.startDate) : that.startDate != null)
            return false;
        if (endDate != null ? !endDate.equals(that.endDate) : that.endDate != null) return false;
        return courses != null ? courses.equals(that.courses) : that.courses == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (adress != null ? adress.hashCode() : 0);
        result = 31 * result + (startDate != null ? startDate.hashCode() : 0);
        result = 31 * result + (endDate != null ? endDate.hashCode() : 0);
        result = 31 * result + (courses != null ? courses.hashCode() : 0);
        return result;
    }
}
