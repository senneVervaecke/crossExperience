package com.example.sennevervaecke.crossexperience.model.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import com.example.sennevervaecke.crossexperience.model.Element;
import com.example.sennevervaecke.crossexperience.model.FileGroup;

/**
 * Created by sennevervaecke on 10/29/2018.
 */

@Entity(tableName = "element")
public class ElementEntity {
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "fileGroupId")
    private int fileGroupId;
    @ColumnInfo(name = "courseId")
    private int courseId;

    @Ignore
    public ElementEntity(int id, String name, int fileGroupId, int courseId) {
        this.id = id;
        this.name = name;
        this.fileGroupId = fileGroupId;
        this.courseId = courseId;
    }

    @Ignore
    public ElementEntity(Element element, int courseId){
        this.id = element.getId();
        this.name = element.getName();
        this.fileGroupId = element.getFiles().getId();
        this.courseId = courseId;
    }

    public ElementEntity() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFileGroupId() {
        return fileGroupId;
    }

    public void setFileGroupId(int fileGroupId) {
        this.fileGroupId = fileGroupId;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ElementEntity that = (ElementEntity) o;

        if (id != that.id) return false;
        if (fileGroupId != that.fileGroupId) return false;
        if (courseId != that.courseId) return false;
        return name != null ? name.equals(that.name) : that.name == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + fileGroupId;
        result = 31 * result + courseId;
        return result;
    }
}
