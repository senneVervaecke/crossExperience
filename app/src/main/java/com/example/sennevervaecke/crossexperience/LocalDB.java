package com.example.sennevervaecke.crossexperience;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by sennevervaecke on 12/25/2017.
 */

public class LocalDB {
    private static ArrayList<Wedstrijd> wedstrijden = new ArrayList<Wedstrijd>();
    public static int wedstrijdPos = 0;
    public static int reeksPos = 0;
    public static boolean startup = true;

    LocalDB(){}

    public static ArrayList<Wedstrijd> getWedstrijden() {
        return wedstrijden;
    }
    public static void addWedstrijd(Wedstrijd wedstrijd){
        wedstrijden.add(wedstrijd);
    }
    public static Wedstrijd getWedstrijd(int wedstrijdId){
        for(int i = 0; i < wedstrijden.size(); i++){
            if(wedstrijden.get(i).getId() == wedstrijdId){
                return wedstrijden.get(i);
            }
        }
        return null;
    }
    public static Wedstrijd getWedstrijd(String naam){
        for(int i = 0; i < wedstrijden.size(); i++){
            if(wedstrijden.get(i).getNaam() == naam){
                return wedstrijden.get(i);
            }
        }

        return null;
    }
    public static void setReadyState(int wedstrijdId, int reeksId, boolean readyState){
        for (int i = 0; i < wedstrijden.size(); i++){
            if(wedstrijden.get(i).getId() == wedstrijdId){
                for (int j = 0; j < wedstrijden.get(i).getReeksen().size(); j++){
                    if(wedstrijden.get(i).getReeksen().get(j).getId() == reeksId){
                        wedstrijden.get(i).getReeksen().get(j).setReadyState(readyState);
                        return;
                    }
                }
            }
        }
    }

    public static void fill(){
        Adres adres = new Adres(1, "belgie", "waregem", 4520, "koersstraat", 45);
        ArrayList<Reeks> reeksen1 = new ArrayList<Reeks>();
        reeksen1.add(new Reeks(1, "midden", false));
        reeksen1.add(new Reeks(2, "zwaar", false));

        ArrayList<Reeks> reeksen2 = new ArrayList<Reeks>();
        reeksen2.add(new Reeks(1, "midden", false));
        reeksen2.add(new Reeks(2, "zwaar", false));

        wedstrijden.add(new Wedstrijd(1, "waregem", adres, new GregorianCalendar(2017, Calendar.APRIL, 14), new GregorianCalendar(2017, Calendar.APRIL, 17), reeksen1));
        wedstrijden.add(new Wedstrijd(2, "nokere", adres, new GregorianCalendar(2017, Calendar.APRIL, 21), new GregorianCalendar(2017, Calendar.APRIL, 22), reeksen2));
    }

}
