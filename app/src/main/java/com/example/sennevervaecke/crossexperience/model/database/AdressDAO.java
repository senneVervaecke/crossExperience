package com.example.sennevervaecke.crossexperience.model.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.sennevervaecke.crossexperience.model.Adress;

import java.util.List;

/**
 * Created by sennevervaecke on 3/16/2018.
 */
@Dao
public interface AdressDAO {

    @Query("SELECT * FROM adress WHERE id = :id;")
    public Adress get(int id);

    @Query("SELECT * FROM adress;")
    public List<Adress> getAll();

    @Query("DELETE FROM adress")
    public void nuke();

    @Insert
    public void insert(Adress...adress);
}
