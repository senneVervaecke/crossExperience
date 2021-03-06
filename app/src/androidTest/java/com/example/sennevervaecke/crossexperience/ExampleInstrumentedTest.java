package com.example.sennevervaecke.crossexperience;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.example.sennevervaecke.crossexperience.controller.UpdateDatabaseTask;
import com.example.sennevervaecke.crossexperience.model.interfaces.UpdateDatabaseCom;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        UpdateDatabaseTask task = new UpdateDatabaseTask(appContext, new UpdateDatabaseCom() {
            @Override
            public void onCompleteTask() {

            }
        });


        task.execute();

        String result = task.get();

        assertEquals("com.example.sennevervaecke.crossexperience", appContext.getPackageName());
    }
}
