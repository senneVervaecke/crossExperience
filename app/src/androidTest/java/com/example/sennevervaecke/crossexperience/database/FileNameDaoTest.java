package com.example.sennevervaecke.crossexperience.database;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.example.sennevervaecke.crossexperience.model.database.Database;
import com.example.sennevervaecke.crossexperience.model.database.FileName;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;

/**
 * Created by sennevervaecke on 11/7/2018.
 */

@RunWith(AndroidJUnit4.class)
public class FileNameDaoTest {

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
    public void insertGetFileName(){
        db.fileNameDao().insert(DataHelper.getFileName1());
        FileName result = db.fileNameDao().get(1);
        Assert.assertEquals(DataHelper.getFileName1(), result);
    }

    @Test
    public void insertThreeGetAll(){
        db.fileNameDao().insert(DataHelper.getFileName1(), DataHelper.getFileName2(), DataHelper.getFileName3());
        List<FileName> results = db.fileNameDao().getAll();
        Assert.assertEquals(3, results.size());
        Assert.assertEquals(DataHelper.getFileName1(), results.get(0));
        Assert.assertEquals(DataHelper.getFileName2(), results.get(1));
        Assert.assertEquals(DataHelper.getFileName3(), results.get(2));
    }

    @Test
    public void insertThreeGetTwoByFileGroupId(){
        db.fileNameDao().insert(DataHelper.getFileName1(), DataHelper.getFileName2(), DataHelper.getFileName3());
        List<FileName> results = db.fileNameDao().getByFileGroupId(1);
        Assert.assertEquals(2, results.size());
        Assert.assertEquals(DataHelper.getFileName1(), results.get(0));
        Assert.assertEquals(DataHelper.getFileName2(), results.get(1));
    }
}
