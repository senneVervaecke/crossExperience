package com.example.sennevervaecke.crossexperience.model.database;

import android.arch.persistence.room.TypeConverter;

import java.util.Date;

/**
 * Created by sennevervaecke on 3/16/2018.
 */

public class DateTypeConverter {
    @TypeConverter
    public Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public Long dateToTimestamp(Date date) {
        if (date == null) {
            return null;
        } else {
            return date.getTime();
        }
    }
}
