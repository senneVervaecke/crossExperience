package com.example.sennevervaecke.crossexperience;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by sennevervaecke on 11/21/2017.
 */

public class Wedstrijd implements Serializable{
    private int id;
    private String naam;
    private Adres adres;
    private Calendar startDatum;
    private Calendar eindDatum;
    private ArrayList<Reeks> reeksen;

    public Wedstrijd(int id, String naam, Adres adres, Calendar startDatum, Calendar eindDatum, ArrayList<Reeks> reeksen) {
        this.id = id;
        this.naam = naam;
        this.adres = adres;
        this.startDatum = startDatum;
        this.eindDatum = eindDatum;
        this.reeksen = reeksen;
    }

    public Wedstrijd(int id, String naam, Adres adres, Calendar startDatum, Calendar eindDatum) {
        this.id = id;
        this.naam = naam;
        this.adres = adres;
        this.startDatum = startDatum;
        this.eindDatum = eindDatum;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNaam() {
        return naam;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }

    public Adres getAdres() {
        return adres;
    }

    public void setAdres(Adres adres) {
        this.adres = adres;
    }

    public Calendar getStartDatum() {
        return startDatum;
    }

    public void setStartDatum(Calendar startDatum) {
        this.startDatum = startDatum;
    }

    public Calendar getEindDatum() {
        return eindDatum;
    }

    public void setEindDatum(Calendar eindDatum) {
        this.eindDatum = eindDatum;
    }

    public ArrayList<Reeks> getReeksen() {
        return reeksen;
    }

    public void setReeksen(ArrayList<Reeks> reeksen) {
        this.reeksen = reeksen;
    }

    public void addReeks(Reeks reeks){
        this.reeksen.add(reeks);
    }
}
