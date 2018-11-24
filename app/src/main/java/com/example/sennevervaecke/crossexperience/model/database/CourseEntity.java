package com.example.sennevervaecke.crossexperience.model.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import com.example.sennevervaecke.crossexperience.model.Course;

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
    @Ignore
    public CourseEntity(int id, String level, double distance, int speed, int competitionId) {
        this.id = id;
        this.level = level;
        this.distance = distance;
        this.speed = speed;
        this.competitionId = competitionId;
    }

    @Ignore
    public CourseEntity(Course course, int competitionId){
        this.id = course.getId();
        this.level = course.getLevel();
        this.distance = course.getDistance();
        this.speed = course.getSpeed();
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CourseEntity that = (CourseEntity) o;

        if (id != that.id) return false;
        if (Double.compare(that.distance, distance) != 0) return false;
        if (speed != that.speed) return false;
        if (competitionId != that.competitionId) return false;
        return level != null ? level.equals(that.level) : that.level == null;
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
        result = 31 * result + competitionId;
        return result;
    }
}
