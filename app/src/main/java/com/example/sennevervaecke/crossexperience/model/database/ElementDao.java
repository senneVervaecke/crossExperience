package com.example.sennevervaecke.crossexperience.model.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by sennevervaecke on 11/3/2018.
 */

@Dao
public interface ElementDao {
    @Query("SELECT * FROM element WHERE id = :id;")
    public ElementEntity get(int id);

    @Query("SELECT * FROM element;")
    public List<ElementEntity> getAll();

    @Query("SELECT * FROM element WHERE courseId = :courseId;")
    public List<ElementEntity> getByCourseId(int courseId);

    @Insert
    public void insert(ElementEntity...elementEntities);
}
