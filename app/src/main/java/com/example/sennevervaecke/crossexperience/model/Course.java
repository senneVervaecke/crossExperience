package com.example.sennevervaecke.crossexperience.model;
import com.example.sennevervaecke.crossexperience.model.database.CourseEntity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by sennevervaecke on 11/21/2017.
 */

public class Course implements Serializable {

    private int id;
    private String level;
    private double distance;
    private int speed;
    private ArrayList<Element> elements;

    public Course(int id, String level, double distance, int speed, ArrayList<Element> elements) {
        this.id = id;
        this.level = level;
        this.distance = distance;
        this.speed = speed;
        this.elements = elements;
    }

    public Course(CourseEntity entity){
        this.id = entity.getId();
        this.level = entity.getLevel();
        this.distance = entity.getDistance();
        this.speed = entity.getSpeed();
        this.elements = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public ArrayList<Element> getElements() {
        return elements;
    }

    public void setElements(ArrayList<Element> elements) {
        this.elements = elements;
    }

    public void addElement(Element element){
        this.elements.add(element);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Course course = (Course) o;

        if (id != course.id) return false;
        if (Double.compare(course.distance, distance) != 0) return false;
        if (speed != course.speed) return false;
        if (level != null ? !level.equals(course.level) : course.level != null) return false;
        return elements != null ? elements.equals(course.elements) : course.elements == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = id;
        result = 31 * result + (level != null ? level.hashCode() : 0);
        temp = Double.doubleToLongBits(distance);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + speed;
        result = 31 * result + (elements != null ? elements.hashCode() : 0);
        return result;
    }
}
