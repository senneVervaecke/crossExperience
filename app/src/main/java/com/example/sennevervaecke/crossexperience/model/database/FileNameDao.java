package com.example.sennevervaecke.crossexperience.model.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by sennevervaecke on 11/3/2018.
 */

@Dao
public interface FileNameDao {
    @Query("SELECT * FROM fileName WHERE id = :id;")
    public FileName get(int id);

    @Query("SELECT * FROM fileName;")
    public List<FileName> getAll();

    @Query("SELECT * FROM fileName WHERE fileGroupId = :fileGroupId;")
    public List<FileName> getByFileGroupId(int fileGroupId);
    @Insert
    public void insert(FileName...fileNames);

    @Query("DELETE FROM fileName")
    public void nuke();
}
