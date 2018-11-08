package com.example.sennevervaecke.crossexperience.database;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.example.sennevervaecke.crossexperience.model.Adress;
import com.example.sennevervaecke.crossexperience.model.database.AdressDAO;
import com.example.sennevervaecke.crossexperience.model.database.Database;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;

/**
 * Created by sennevervaecke on 10/29/2018.
 */


@RunWith(AndroidJUnit4.class)
public class AddressDaoTest {

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
    public void insertGetAdres(){
        Adress testAdress = DataHelper.getAdress1();
        db.adressDAO().insert(testAdress);
        Assert.assertTrue(testAdress.equals(db.adressDAO().get(1)));
    }
    @Test
    public void insertTwoGetAll(){
        Adress testAdress1 = DataHelper.getAdress1();
        Adress testAdress2 = DataHelper.getAdress2();
        db.adressDAO().insert(testAdress1, testAdress2);
        List<Adress> results = db.adressDAO().getAll();
        Assert.assertEquals(2, results.size());
        Assert.assertEquals(testAdress1, results.get(0));
        Assert.assertEquals(testAdress2, results.get(1));
    }
}
