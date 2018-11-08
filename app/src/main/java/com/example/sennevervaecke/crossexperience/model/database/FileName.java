package com.example.sennevervaecke.crossexperience.model.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by sennevervaecke on 10/29/2018.
 */

@Entity(tableName = "fileName")
public class FileName {
    @PrimaryKey
    @ColumnInfo(name = "id")
    private int id;
    @ColumnInfo(name = "fileName")
    private String fileName;
    @ColumnInfo(name = "fileGroupId")
    private int fileGroupÎd;

    @Ignore
    public FileName(int id, String fileName, int fileGroupÎd) {
        this.id = id;
        this.fileName = fileName;
        this.fileGroupÎd = fileGroupÎd;
    }

    public FileName() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getFileGroupÎd() {
        return fileGroupÎd;
    }

    public void setFileGroupÎd(int fileGroupÎd) {
        this.fileGroupÎd = fileGroupÎd;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FileName fileName1 = (FileName) o;

        if (id != fileName1.id) return false;
        if (fileGroupÎd != fileName1.fileGroupÎd) return false;
        return fileName != null ? fileName.equals(fileName1.fileName) : fileName1.fileName == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (fileName != null ? fileName.hashCode() : 0);
        result = 31 * result + fileGroupÎd;
        return result;
    }
}
