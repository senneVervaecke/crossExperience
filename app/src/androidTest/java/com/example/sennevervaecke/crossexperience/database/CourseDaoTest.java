package com.example.sennevervaecke.crossexperience.database;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.example.sennevervaecke.crossexperience.model.database.CourseEntity;
import com.example.sennevervaecke.crossexperience.model.database.Database;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;

/**
 * Created by sennevervaecke on 11/3/2018.
 */

@RunWith(AndroidJUnit4.class)
public class CourseDaoTest {
    private Database db;

    @Before
    public void startUp(){
        Context context = InstrumentationRegistry.getTargetContext();
        db = Room.inMemoryDatabaseBuilder(context, Database.class).build();
    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }
    @Test
    public void insertGetCourse(){
        db.courseDAO().insert(DataHelper.getCourseEntity1());
        CourseEntity result = db.courseDAO().get(DataHelper.getCourseEntity1().getId());
        Assert.assertEquals(DataHelper.getCourseEntity1(), result);
    }
    @Test
    public void insertThreeGetAll(){
        db.courseDAO().insert(DataHelper.getCourseEntity1(), DataHelper.getCourseEntity2(), DataHelper.getCourseEntity3());
        List<CourseEntity> results = db.courseDAO().getAll();
        Assert.assertEquals(3, results.size());
        Assert.assertEquals(DataHelper.getCourseEntity1(), results.get(0));
        Assert.assertEquals(DataHelper.getCourseEntity2(), results.get(1));
        Assert.assertEquals(DataHelper.getCourseEntity3(), results.get(2));
    }
    @Test
    public void insertThreeGetTwoByCompetitionId1(){
        db.courseDAO().insert(DataHelper.getCourseEntity1(), DataHelper.getCourseEntity2(), DataHelper.getCourseEntity3());
        List<CourseEntity> results = db.courseDAO().getByCompetitionId(1);
        Assert.assertEquals(2, results.size());
        Assert.assertEquals(DataHelper.getCourseEntity1(), results.get(0));
        Assert.assertEquals(DataHelper.getCourseEntity2(), results.get(1));
    }
    @Test
    public void insertThreeGetByNonExistingCompetitionId(){
        db.courseDAO().insert(DataHelper.getCourseEntity1(), DataHelper.getCourseEntity2(), DataHelper.getCourseEntity3());
        List<CourseEntity> results = db.courseDAO().getByCompetitionId(3);
        Assert.assertEquals(0, results.size());
    }
}
