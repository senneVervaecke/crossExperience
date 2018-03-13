package com.example.sennevervaecke.crossexperience;

import com.example.sennevervaecke.crossexperience.model.Adres;
import com.example.sennevervaecke.crossexperience.model.LocalDB;
import com.example.sennevervaecke.crossexperience.model.Reeks;
import com.example.sennevervaecke.crossexperience.model.Wedstrijd;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.junit.Assert.*;

/**
 * Created by sennevervaecke on 1/29/2018.
 */
public class LocalDBTest {
    @Test
    public void TestAddGetSet() throws Exception {
        Adres adres = new Adres(1, "belgie", "waregem", 4520, "koersstraat", 45);
        ArrayList<Reeks> reeksen1 = new ArrayList<Reeks>();
        reeksen1.add(new Reeks(1, "midden", false));
        reeksen1.add(new Reeks(2, "zwaar", false));

        ArrayList<Reeks> reeksen2 = new ArrayList<Reeks>();
        reeksen2.add(new Reeks(1, "midden", false));
        reeksen2.add(new Reeks(2, "zwaar", false));

        Wedstrijd wedstrijd1 = new Wedstrijd(1, "waregem", adres, new GregorianCalendar(2017, Calendar.APRIL, 14), new GregorianCalendar(2017, Calendar.APRIL, 17), reeksen1);
        Wedstrijd wedstrijd2 = new Wedstrijd(2, "nokere", adres, new GregorianCalendar(2017, Calendar.APRIL, 21), new GregorianCalendar(2017, Calendar.APRIL, 22), reeksen2);

        LocalDB.addWedstrijd(wedstrijd1);
        LocalDB.addWedstrijd(wedstrijd2);

        assertEquals(LocalDB.getWedstrijd(wedstrijd1.getNaam()), wedstrijd1);
        assertEquals(LocalDB.getWedstrijd(wedstrijd1.getId()), wedstrijd1);

        assertEquals(LocalDB.getWedstrijd(wedstrijd2.getNaam()), wedstrijd2);
        assertEquals(LocalDB.getWedstrijd(wedstrijd2.getId()), wedstrijd2);

        ArrayList<Wedstrijd> wedstrijden = new ArrayList<Wedstrijd>();
        wedstrijden.add(wedstrijd1);
        wedstrijden.add(wedstrijd2);

        assertEquals(wedstrijden, LocalDB.getWedstrijden());

        LocalDB.setReadyState(1, 2, true);
        assertTrue(LocalDB.getWedstrijd(1).getReeksen().get(1).isReadyState());
    }

}