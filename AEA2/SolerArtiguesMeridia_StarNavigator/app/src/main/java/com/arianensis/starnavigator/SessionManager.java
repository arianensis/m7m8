package com.arianensis.starnavigator;

import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.arianensis.starnavigator.DB.StarsDBHelper;

import java.time.LocalDateTime;
import java.util.Locale;

public class SessionManager
{
    public static String loggedUsername = "";

    public static void setLocale(Activity activity, String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Resources resources = activity.getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }

    public static void loadDefaultStars() {
        // initialise the database with some "default" known stars
        //Log.i("DIVAC", "Current entries: "+Star.getAllStars().size());
        //Log.i("DIVAC", "Database reset");
        //Log.i("DIVAC", "Current entries: "+Star.getAllStars().size());
        new Star("Sol", new double[]{0,0,0}, false, "G2V", false);
        new Star("Sirius", new double[]{-1.6325474134065256, 8.54055544656844, 0.4902882897538908}, false, "A0", false);
        new Star("Canopus", new double[]{6.39919719, 52.6956614, 310}, true, "A9ii", false);
        new Star("Betelgeuse", new double[]{5.91952927, 7.407064, 568.5}, true, "M1b", false);
        new Star("Vega", new double[]{18.615649, 38.7836889, 25.04}, true, "A0Va", false);
        new Star("Polaris", new double[]{2.53030278, 89.2641111, Star.paraDist(7.54)}, true, "F6VIII", false);
        new Star("Bellatrix", new double[]{5.4188509, 6.34970328, 250}, true, "B2IV", false);
        new Star("Rigel", new double[]{5.242297806, 8.201638361, 863}, true, "B8Ia", false);
        new Star("Mintaka", new double[]{5.533444469, -0.299095111, 1200}, true, "O9", false);
        new Star("Capella", new double[]{5.278155197, 45.997991472, 42.919}, true, "G3III", false);
        new Star("Altair", new double[]{19.846388486, 8.868321194, 16.73}, true, "A7V", false);
        new Star("Spica", new double[]{13.419883056, -11.161319444, 13.06}, true, "B1V", false);
        new Star("Regulus", new double[]{10.139530833, 11.967208333, 79.3}, true, "B8IV", false);
        new Star("Alhena", new double[]{6.628530694, 16.399280417, 109}, true, "A1", false);
        new Star("Caph", new double[]{0.152968106, 59.149781111, 54.7}, true, "F2III", false);
        new Star("Alphecca", new double[]{15.57813, 26.707191667, 75}, true, "F3", false);
        new Star("Aldebaran", new double[]{4.598677519, 16.509302361, 65.3}, true, "K5", false);
    }
}