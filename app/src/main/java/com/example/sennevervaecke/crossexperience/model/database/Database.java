package com.example.sennevervaecke.crossexperience.model.database;

import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.sennevervaecke.crossexperience.model.Adress;

/**
 * Created by sennevervaecke on 3/16/2018.
 */

@android.arch.persistence.room.Database(entities = {Adress.class, CourseEntity.class, CompetitionEntity.class}, version = 2)
public abstract class Database extends RoomDatabase {
    private static Database instance;

    public abstract AdressDAO adressDAO();
    public abstract CourseDAO courseDAO();
    public abstract CompetitionDAO competitionDAO();

    public static Database getAppDatabase(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(), Database.class, "app_database")
                    .allowMainThreadQueries().fallbackToDestructiveMigration().build();
        }
        return instance;
    }
}
