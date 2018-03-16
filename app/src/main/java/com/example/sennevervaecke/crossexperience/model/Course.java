package com.example.sennevervaecke.crossexperience.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.example.sennevervaecke.crossexperience.model.database.CourseEntity;

import java.io.Serializable;

/**
 * Created by sennevervaecke on 11/21/2017.
 */

public class Course implements Serializable {

    private int id;
    private String level;
    private double distance;
    private int speed;
    private boolean readyState;

    public Course(int id, String level, double distance, int speed, boolean readyState) {
        this.id = id;
        this.level = level;
        this.distance = distance;
        this.speed = speed;
        this.readyState = readyState;
    }

    public Course(CourseEntity entity){
        this.id = entity.getId();
        this.level = entity.getLevel();
        this.distance = entity.getDistance();
        this.speed = entity.getSpeed();
        this.readyState = false;
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

    public boolean isReadyState() {
        return readyState;
    }

    public void setReadyState(boolean readyState) {
        this.readyState = readyState;
    }
}
