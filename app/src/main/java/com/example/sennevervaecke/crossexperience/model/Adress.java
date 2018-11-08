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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Adress adress = (Adress) o;

        if (id != adress.id) return false;
        if (postalcode != adress.postalcode) return false;
        if (number != adress.number) return false;
        if (country != null ? !country.equals(adress.country) : adress.country != null)
            return false;
        if (city != null ? !city.equals(adress.city) : adress.city != null) return false;
        return street != null ? street.equals(adress.street) : adress.street == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (country != null ? country.hashCode() : 0);
        result = 31 * result + (city != null ? city.hashCode() : 0);
        result = 31 * result + postalcode;
        result = 31 * result + (street != null ? street.hashCode() : 0);
        result = 31 * result + number;
        return result;
    }
}
