package com.example.sennevervaecke.crossexperience.database;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.example.sennevervaecke.crossexperience.model.Competition;
import com.example.sennevervaecke.crossexperience.model.database.CompetitionEntity;
import com.example.sennevervaecke.crossexperience.model.database.Database;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by sennevervaecke on 10/30/2018.
 */

@RunWith(AndroidJUnit4.class)
public class CompetitionDaoTest {
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
    public void insertGetCompetition(){
        CompetitionEntity testCompetition = DataHelper.getCompetitionEntity1();
        db.competitionDAO().insert(testCompetition);
        CompetitionEntity result = db.competitionDAO().get(1);
        Assert.assertEquals(testCompetition, result);
    }

    @Test
    public void insertTwoGetAll(){
        CompetitionEntity testCompetition1 = DataHelper.getCompetitionEntity1();
        CompetitionEntity testCompetition2 = DataHelper.getCompetitionEntity2();
        db.competitionDAO().insert(testCompetition1, testCompetition2);
        List<CompetitionEntity> results = db.competitionDAO().getAll();
        Assert.assertEquals(2, results.size());
        Assert.assertEquals(testCompetition1, results.get(0));
        Assert.assertEquals(testCompetition2, results.get(1));
    }

    @Test
    public void insertTwoGetOneBetweenDate() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        CompetitionEntity testCompetition1 = DataHelper.getCompetitionEntity1();
        CompetitionEntity testCompetition2 = DataHelper.getCompetitionEntity2();
        db.competitionDAO().insert(testCompetition1, testCompetition2);
        List<CompetitionEntity> results = db.competitionDAO().getBetween(format.parse("25-07-2018").getTime(), format.parse("15-08-2018").getTime());
        Assert.assertEquals(1, results.size());
        Assert.assertEquals(testCompetition2, results.get(0));
    }

    @Test
    public void getBetweenDateWhereStartdateIsEqual() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        CompetitionEntity testCompetition1 = DataHelper.getCompetitionEntity1();
        CompetitionEntity testCompetition2 = DataHelper.getCompetitionEntity2();
        db.competitionDAO().insert(testCompetition1, testCompetition2);
        List<CompetitionEntity> results = db.competitionDAO().getBetween(format.parse("01-08-2018").getTime(), format.parse("15-08-2018").getTime());
        Assert.assertEquals(1, results.size());
        Assert.assertEquals(testCompetition2, results.get(0));
    }
    @Test
    public void getBetweenDateWhereStartdateIsBetween() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        CompetitionEntity testCompetition1 = DataHelper.getCompetitionEntity1();
        CompetitionEntity testCompetition2 = DataHelper.getCompetitionEntity2();
        db.competitionDAO().insert(testCompetition1, testCompetition2);
        List<CompetitionEntity> results = db.competitionDAO().getBetween(format.parse("02-08-2018").getTime(), format.parse("15-08-2018").getTime());
        Assert.assertEquals(1, results.size());
        Assert.assertEquals(testCompetition2, results.get(0));
    }
    @Test
    public void getBetweenDateWhereEnddateIsEqual() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        CompetitionEntity testCompetition1 = DataHelper.getCompetitionEntity1();
        CompetitionEntity testCompetition2 = DataHelper.getCompetitionEntity2();
        db.competitionDAO().insert(testCompetition1, testCompetition2);
        List<CompetitionEntity> results = db.competitionDAO().getBetween(format.parse("25-07-2018").getTime(), format.parse("3-08-2018").getTime());
        Assert.assertEquals(1, results.size());
        Assert.assertEquals(testCompetition2, results.get(0));
    }
    @Test
    public void getBetweenDateWhereEnddateIsBetween() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        CompetitionEntity testCompetition1 = DataHelper.getCompetitionEntity1();
        CompetitionEntity testCompetition2 = DataHelper.getCompetitionEntity2();
        db.competitionDAO().insert(testCompetition1, testCompetition2);
        List<CompetitionEntity> results = db.competitionDAO().getBetween(format.parse("25-07-2018").getTime(), format.parse("2-08-2018").getTime());
        Assert.assertEquals(0, results.size());
    }

}
