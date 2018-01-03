package com.example.sennevervaecke.crossexperience;

import java.io.Serializable;

/**
 * Created by sennevervaecke on 11/21/2017.
 */

public class Adres implements Serializable {
    private int id;
    private String land;
    private String stad;
    private int postcode;
    private String straat;
    private int nummer;

    public Adres(int id, String land, String stad, int postcode, String straat, int nummer) {
        this.id = id;
        this.land = land;
        this.stad = stad;
        this.postcode = postcode;
        this.straat = straat;
        this.nummer = nummer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLand() {
        return land;
    }

    public void setLand(String land) {
        this.land = land;
    }

    public String getStad() {
        return stad;
    }

    public void setStad(String stad) {
        this.stad = stad;
    }

    public int getPostcode() {
        return postcode;
    }

    public void setPostcode(int postcode) {
        this.postcode = postcode;
    }

    public String getStraat() {
        return straat;
    }

    public void setStraat(String straat) {
        this.straat = straat;
    }

    public int getNummer() {
        return nummer;
    }

    public void setNummer(int nummer) {
        this.nummer = nummer;
    }
}
