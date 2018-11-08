package com.example.sennevervaecke.crossexperience.database;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.example.sennevervaecke.crossexperience.model.database.Database;
import com.example.sennevervaecke.crossexperience.model.database.ElementEntity;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;

/**
 * Created by sennevervaecke on 11/3/2018.
 */

@RunWith(AndroidJUnit4.class)
public class ElementDaoTest {
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
    public void insertGetElement(){
        db.elementDao().insert(DataHelper.getElementEntity1());
        ElementEntity result = db.elementDao().get(1);
        Assert.assertEquals(DataHelper.getElementEntity1(), result);
    }
    @Test
    public void insertTwoGetAll(){
        db.elementDao().insert(DataHelper.getElementEntity1(), DataHelper.getElementEntity2());
        List<ElementEntity> result = db.elementDao().getAll();
        Assert.assertEquals(2, result.size());
        Assert.assertEquals(DataHelper.getElementEntity1(), result.get(0));
        Assert.assertEquals(DataHelper.getElementEntity2(), result.get(1));
    }
    @Test
    public void insertThreeGetByCourseId(){
        db.elementDao().insert(DataHelper.getElementEntity1(), DataHelper.getElementEntity2(), DataHelper.getElementEntity3());
        List<ElementEntity> result = db.elementDao().getByCourseId(1);
        Assert.assertEquals(2, result.size());
        Assert.assertEquals(DataHelper.getElementEntity1(), result.get(0));
        Assert.assertEquals(DataHelper.getElementEntity2(), result.get(1));
    }
}
