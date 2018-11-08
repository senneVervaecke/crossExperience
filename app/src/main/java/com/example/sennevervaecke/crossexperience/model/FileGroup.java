package com.example.sennevervaecke.crossexperience.model;

import com.example.sennevervaecke.crossexperience.model.database.FileGroupEntity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by sennevervaecke on 10/29/2018.
 */

public class FileGroup implements Serializable {
    private int id;
    private DataType dataType;
    private ArrayList<String> filenames;

    public FileGroup(int id, DataType dataType, ArrayList<String> filenames) {
        this.id = id;
        this.dataType = dataType;
        this.filenames = filenames;
    }
    public FileGroup(FileGroupEntity fileGroupEntity){
        this.id = fileGroupEntity.getId();
        this.dataType = DataType.valueOf(fileGroupEntity.getDataType().toUpperCase());
        this.filenames = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public DataType getDataType() {
        return dataType;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }

    public ArrayList<String> getFilenames() {
        return filenames;
    }

    public void setFilenames(ArrayList<String> filenames) {
        this.filenames = filenames;
    }

    public void addFileName(String fileName){
        this.filenames.add(fileName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FileGroup fileGroup = (FileGroup) o;

        if (id != fileGroup.id) return false;
        if (dataType != fileGroup.dataType) return false;
        return filenames != null ? filenames.equals(fileGroup.filenames) : fileGroup.filenames == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (dataType != null ? dataType.hashCode() : 0);
        result = 31 * result + (filenames != null ? filenames.hashCode() : 0);
        return result;
    }
}
