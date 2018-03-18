package com.example.sennevervaecke.crossexperience.model.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by sennevervaecke on 3/16/2018.
 */

@Dao
public interface CourseDAO {

    @Query("SELECT * FROM course WHERE id = :id;")
    public CourseEntity get(int id);

    @Query("SELECT * FROM course;")
    public List<CourseEntity> getAll();

    @Query("SELECT * FROM course WHERE competitionId = :competitionId;")
    public List<CourseEntity> getByCompetitionId(int competitionId);

    @Query("DELETE FROM course")
    public void nuke();

    @Insert
    public void insert(CourseEntity...courseEntities);
}
