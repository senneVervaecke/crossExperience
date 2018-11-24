package com.example.sennevervaecke.crossexperience.model.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by sennevervaecke on 11/3/2018.
 */

@Dao
public interface FileGroupDao {
    @Query("SELECT * FROM fileGroup WHERE id = :id;")
    public FileGroupEntity get(int id);

    @Query("SELECT * FROM fileGroup;")
    public List<FileGroupEntity> getAll();

    @Insert
    public void insert(FileGroupEntity...fileGroupEntities);

    @Query("DELETE FROM fileGroup")
    public void nuke();
}
