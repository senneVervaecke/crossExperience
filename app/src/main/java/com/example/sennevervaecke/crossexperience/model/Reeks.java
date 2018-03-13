package com.example.sennevervaecke.crossexperience.model;

import java.io.Serializable;

/**
 * Created by sennevervaecke on 11/21/2017.
 */

public class Reeks implements Serializable {
    private int id;
    private String niveau;
    private boolean readyState;
    //hier komt video bestand

    public Reeks(int id, String niveau, boolean readyState) {
        this.id = id;
        this.niveau = niveau;
        this.readyState = readyState;
    }

    public boolean isReadyState() {
        return readyState;
    }

    public void setReadyState(boolean readyState) {
        this.readyState = readyState;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNiveau() {
        return niveau;
    }

    public void setNiveau(String niveau) {
        this.niveau = niveau;
    }
}
