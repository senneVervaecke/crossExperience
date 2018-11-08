package com.example.sennevervaecke.crossexperience.database;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.example.sennevervaecke.crossexperience.model.database.Database;
import com.example.sennevervaecke.crossexperience.model.database.FileGroupEntity;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;

/**
 * Created by sennevervaecke on 11/7/2018.
 */

@RunWith(AndroidJUnit4.class)
public class FileGroupDaoTest {

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
    public void insertGetFileGroup(){
        db.fileGroupDao().insert(DataHelper.getFileGroupEntity1());
        FileGroupEntity result = db.fileGroupDao().get(1);
        Assert.assertEquals(DataHelper.getFileGroupEntity1(), result);
    }

    @Test
    public void insertThreeGetAll(){
        db.fileGroupDao().insert(DataHelper.getFileGroupEntity1(), DataHelper.getFileGroupEntity2(), DataHelper.getFileGroupEntity3());
        List<FileGroupEntity> results = db.fileGroupDao().getAll();
        Assert.assertEquals(3, results.size());
        Assert.assertEquals(DataHelper.getFileGroupEntity1(), results.get(0));
        Assert.assertEquals(DataHelper.getFileGroupEntity2(), results.get(1));
        Assert.assertEquals(DataHelper.getFileGroupEntity3(), results.get(2));
    }
}
