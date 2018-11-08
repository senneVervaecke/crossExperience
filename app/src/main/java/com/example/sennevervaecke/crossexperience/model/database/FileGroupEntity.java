package com.example.sennevervaecke.crossexperience.model.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import com.example.sennevervaecke.crossexperience.model.DataType;

import java.util.ArrayList;

/**
 * Created by sennevervaecke on 10/29/2018.
 */

@Entity(tableName = "fileGroup")
public class FileGroupEntity {
    @PrimaryKey
    @ColumnInfo(name = "id")
    private int id;
    @ColumnInfo(name = "datatype")
    private String dataType;

    public FileGroupEntity() {
    }
    @Ignore
    public FileGroupEntity(int id, String dataType) {
        this.id = id;
        this.dataType = dataType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FileGroupEntity that = (FileGroupEntity) o;

        if (id != that.id) return false;
        return dataType != null ? dataType.equals(that.dataType) : that.dataType == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (dataType != null ? dataType.hashCode() : 0);
        return result;
    }
}
