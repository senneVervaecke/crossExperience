package com.example.sennevervaecke.crossexperience.model.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.Calendar;
import java.util.List;

/**
 * Created by sennevervaecke on 3/16/2018.
 */

@Dao
public interface CompetitionDAO {

    @Query("SELECT * FROM competition WHERE id = :id;")
    public CompetitionEntity get(int id);

    @Query("SELECT * FROM competition;")
    public List<CompetitionEntity> getAll();

    @Query("SELECT * FROM competition WHERE endDate BETWEEN :startDate AND :endDate;")
    public List<CompetitionEntity> getBetween(long startDate, long endDate);

    @Query("DELETE FROM competition")
    public void nuke();

    @Insert
    public void insert(CompetitionEntity...competitionEntities);
}
