package com.example.sennevervaecke.crossexperience.model.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by sennevervaecke on 3/16/2018.
 */

@Entity(tableName = "course")
public class CourseEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "level")
    private String level;
    @ColumnInfo(name = "distance")
    private double distance;
    @ColumnInfo(name = "speed")
    private int speed;
    @ColumnInfo(name = "competitionId")
    private int competitionId;

    public CourseEntity() {
    }

    public CourseEntity(int id, String level, double distance, int speed, int competitionId) {
        this.id = id;
        this.level = level;
        this.distance = distance;
        this.speed = speed;
        this.competitionId = competitionId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public int getCompetitionId() {
        return competitionId;
    }

    public void setCompetitionId(int competitionId) {
        this.competitionId = competitionId;
    }
}
