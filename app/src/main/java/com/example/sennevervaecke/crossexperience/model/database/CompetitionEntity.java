package com.example.sennevervaecke.crossexperience.model.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverter;
import android.arch.persistence.room.TypeConverters;

import com.example.sennevervaecke.crossexperience.model.Adress;
import com.example.sennevervaecke.crossexperience.model.Competition;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by sennevervaecke on 3/16/2018.
 */
@Entity(tableName = "competition")
@ForeignKey(entity = Adress.class, parentColumns = "id", childColumns = "adressId")
@TypeConverters(DateTypeConverter.class)
public class CompetitionEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "naam")
    private String name;
    @ColumnInfo(name = "adressId")
    private int adressId;
    @ColumnInfo(name = "startDate")
    private Date startDate;
    @ColumnInfo(name = "endDate")
    private Date endDate;

    public CompetitionEntity() {
    }

    @Ignore
    public CompetitionEntity(int id, String name, int adressId, Date startDate, Date endDate) {
        this.id = id;
        this.name = name;
        this.adressId = adressId;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Ignore
    public CompetitionEntity(Competition competition){
        this.id = competition.getId();
        this.name = competition.getName();
        this.adressId = competition.getAdress().getId();
        this.startDate = competition.getStartDate().getTime();
        this.endDate = competition.getEndDate().getTime();
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

    public int getAdressId() {
        return adressId;
    }

    public void setAdressId(int adressId) {
        this.adressId = adressId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public static CompetitionEntity fromJson(String json){
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CompetitionEntity that = (CompetitionEntity) o;

        if (id != that.id) return false;
        if (adressId != that.adressId) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (startDate != null ? !startDate.equals(that.startDate) : that.startDate != null)
            return false;
        return endDate != null ? endDate.equals(that.endDate) : that.endDate == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + adressId;
        result = 31 * result + (startDate != null ? startDate.hashCode() : 0);
        result = 31 * result + (endDate != null ? endDate.hashCode() : 0);
        return result;
    }
}
