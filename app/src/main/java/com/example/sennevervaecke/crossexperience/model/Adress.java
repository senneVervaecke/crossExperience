package com.example.sennevervaecke.crossexperience.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

/**
 * Created by sennevervaecke on 11/21/2017.
 */

@Entity(tableName = "adress")
public class Adress implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "country")
    private String country;
    @ColumnInfo(name = "city")
    private String city;
    @ColumnInfo(name = "postalcode")
    private int postalcode;
    @ColumnInfo(name = "street")
    private String street;
    @ColumnInfo(name = "number")
    private int number;

    public Adress(int id, String country, String city, int postalcode, String street, int number) {
        this.id = id;
        this.country = country;
        this.city = city;
        this.postalcode = postalcode;
        this.street = street;
        this.number = number;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getPostalcode() {
        return postalcode;
    }

    public void setPostalcode(int postalcode) {
        this.postalcode = postalcode;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
