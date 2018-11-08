package com.example.sennevervaecke.crossexperience.database;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.example.sennevervaecke.crossexperience.model.Competition;
import com.example.sennevervaecke.crossexperience.model.database.Database;
import com.example.sennevervaecke.crossexperience.model.database.DatabaseHelper;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by sennevervaecke on 11/7/2018.
 */

@RunWith(AndroidJUnit4.class)
public class DatabaseHelperTest {

    private DatabaseHelper databaseHelper;
    private Database db;
    private DateFormat format = new SimpleDateFormat("dd-MM-yyyy");

    @Before
    public void startup(){
        Context context = InstrumentationRegistry.getTargetContext();
        db = Room.inMemoryDatabaseBuilder(context, Database.class).build();
        databaseHelper = new DatabaseHelper(db);
        initDatabase();
    }

    private void initDatabase(){
        db.competitionDAO().insert(DataHelper.getCompetitionEntity1(), DataHelper.getCompetitionEntity2());
        db.courseDAO().insert(DataHelper.getCourseEntity1(), DataHelper.getCourseEntity2(), DataHelper.getCourseEntity3());
        db.adressDAO().insert(DataHelper.getAdress1(), DataHelper.getAdress2());
        db.elementDao().insert(DataHelper.getElementEntity1(), DataHelper.getElementEntity2(), DataHelper.getElementEntity3());
        db.fileGroupDao().insert(DataHelper.getFileGroupEntity1(), DataHelper.getFileGroupEntity2(), DataHelper.getFileGroupEntity3());
        db.fileNameDao().insert(DataHelper.getFileName1(), DataHelper.getFileName2(), DataHelper.getFileName3());
    }

    @Test
    public void getCompetitionById1() throws ParseException {
        Competition result = databaseHelper.getCompetition(1);
        Assert.assertEquals(DataHelper.getCompetition1(), result);
    }

    @Test
    public void getCompetitionByNonExistingId() throws ParseException{
        Competition result = databaseHelper.getCompetition(3);
        Assert.assertNull(result);
    }

    @Test
    public void getAllCompetitions() throws ParseException {
        ArrayList<Competition> results = databaseHelper.getAllCompetitions();
        Assert.assertEquals(2, results.size());
        Assert.assertEquals(DataHelper.getCompetition1(), results.get(0));
        Assert.assertEquals(DataHelper.getCompetition2(), results.get(1));
    }

    @Test
    public void getCompetitionWithId2BetweenDate() throws ParseException {
        Calendar startDate = Calendar.getInstance();
        startDate.setTime(format.parse("28-07-2018"));
        Calendar endDate = Calendar.getInstance();
        endDate.setTime(format.parse("28-08-2018"));
        ArrayList<Competition> results = databaseHelper.getCompetitionsBetween(startDate, endDate);
        Assert.assertEquals(1, results.size());
        Assert.assertEquals(DataHelper.getCompetition2(), results.get(0));
    }

    @Test
    public void getCompetitionWithId2BetweenDateWhereStartDatesAreEqual() throws ParseException {
        Calendar startDate = Calendar.getInstance();
        startDate.setTime(format.parse("1-08-2018"));
        Calendar endDate = Calendar.getInstance();
        endDate.setTime(format.parse("28-08-2018"));
        ArrayList<Competition> results = databaseHelper.getCompetitionsBetween(startDate, endDate);
        Assert.assertEquals(1, results.size());
        Assert.assertEquals(DataHelper.getCompetition2(), results.get(0));
    }

    @Test
    public void getCompetitionWithId2BetweenDateWhereStartDateIsBetween() throws ParseException {
        Calendar startDate = Calendar.getInstance();
        startDate.setTime(format.parse("2-08-2018"));
        Calendar endDate = Calendar.getInstance();
        endDate.setTime(format.parse("28-08-2018"));
        ArrayList<Competition> results = databaseHelper.getCompetitionsBetween(startDate, endDate);
        Assert.assertEquals(1, results.size());
        Assert.assertEquals(DataHelper.getCompetition2(), results.get(0));
    }

    @Test
    public void getCompetitionWithId2BetweenDateWhereEndDatesAreEqual() throws ParseException {
        Calendar startDate = Calendar.getInstance();
        startDate.setTime(format.parse("28-07-2018"));
        Calendar endDate = Calendar.getInstance();
        endDate.setTime(format.parse("3-08-2018"));
        ArrayList<Competition> results = databaseHelper.getCompetitionsBetween(startDate, endDate);
        Assert.assertEquals(1, results.size());
        Assert.assertEquals(DataHelper.getCompetition2(), results.get(0));
    }

    @Test
    public void getCompetitionWithId2BetweenDateWhereEndDateIsBetween() throws ParseException {
        Calendar startDate = Calendar.getInstance();
        startDate.setTime(format.parse("28-07-2018"));
        Calendar endDate = Calendar.getInstance();
        endDate.setTime(format.parse("2-08-2018"));
        ArrayList<Competition> results = databaseHelper.getCompetitionsBetween(startDate, endDate);
        Assert.assertEquals(0, results.size());
    }
}
