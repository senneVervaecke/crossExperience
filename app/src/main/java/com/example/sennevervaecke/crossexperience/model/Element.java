package com.example.sennevervaecke.crossexperience.model;

import com.example.sennevervaecke.crossexperience.model.database.ElementEntity;
import com.example.sennevervaecke.crossexperience.model.database.FileGroupEntity;

import java.io.Serializable;

/**
 * Created by sennevervaecke on 10/29/2018.
 */

public class Element implements Serializable {
    private int id;
    private String name;
    private FileGroup files;

    public Element(int id, String name, FileGroup files) {
        this.id = id;
        this.name = name;
        this.files = files;
    }

    public Element(ElementEntity elementEntity, FileGroupEntity fileGroupEntity){
        this.id = elementEntity.getId();
        this.name = elementEntity.getName();
        this.files = new FileGroup(fileGroupEntity);
    }

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

    public FileGroup getFiles() {
        return files;
    }

    public void setFiles(FileGroup files) {
        this.files = files;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Element element = (Element) o;

        if (id != element.id) return false;
        if (name != null ? !name.equals(element.name) : element.name != null) return false;
        return files != null ? files.equals(element.files) : element.files == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (files != null ? files.hashCode() : 0);
        return result;
    }
}
