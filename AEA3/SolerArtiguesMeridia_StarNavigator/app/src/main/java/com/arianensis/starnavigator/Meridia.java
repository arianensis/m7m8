package com.arianensis.starnavigator;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.arianensis.starnavigator.DB.StarsDBHelper;

import java.time.LocalDateTime;
import java.util.Locale;

// A special class to store other useful methods and stuff
public class Meridia
{
    public static byte lastAnswer = -1;

    // creates a reference to the shared preferences to use from any class
    static SharedPreferences sharedPrefs;
    public static void loadSharedPrefs(SharedPreferences prefs) {
        sharedPrefs = prefs;
    }

    // saves the current settings in Java variables for easier access
    static boolean changingSettings = false;

    // methods to modify the settings in the shared preferences
    //// user session
    public static void login(String username) {
        // saves the current user name to the shared preferences
        SharedPreferences.Editor prefsEdit = sharedPrefs.edit();
        prefsEdit.putString("loggedUsername", username);
        prefsEdit.commit();
        // when the user is logged in, they will appear in the "home" fragment, not in settings
        changingSettings = false;
    }
    public static void logout() {
        // deletes the username field frm the shared preferences
        SharedPreferences.Editor prefsEdit = sharedPrefs.edit();
        prefsEdit.remove("loggedUsername");
        prefsEdit.commit();
    }
    public static String getLoggedUsername() { return sharedPrefs.getString("loggedUsername", ""); }

    //// application settings
    public static void setLanguage(String locale, Context context) {
        SharedPreferences.Editor prefsEdit = sharedPrefs.edit();
        prefsEdit.remove("language");
        prefsEdit.putString("language", locale);

        // default process for setting the language
        Resources res = context.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration config = res.getConfiguration();
        config.setLocale(new Locale(locale));
        res.updateConfiguration(config, dm);

        // if we get here, the change is successful
        prefsEdit.commit();
    }
    public static String getLanguage() { return sharedPrefs.getString("language", ""); }

    public static void setDecimalPrecision(int decimalPrecision) {
        SharedPreferences.Editor prefsEdit = sharedPrefs.edit();
        prefsEdit.remove("decimalPrecision");
        prefsEdit.putInt("decimalPrecision", decimalPrecision);
        prefsEdit.commit();
    }
    public static int getDecimalPrecision() { return sharedPrefs.getInt("decimalPrecision", 4); }

    public static void deleteAllPreferences() {
        SharedPreferences.Editor prefsEdit = sharedPrefs.edit();
        prefsEdit.clear();
        prefsEdit.commit();
    }

    // Custom math methods
    public static String toDecimalString(double value, int decimals) {
        // show all the numbers as strings with the same decimal positions
        // first we round the number to de desired precision
        value = Math.round(value * Math.pow(10.0, decimals)) / Math.pow(10.0, decimals);

        // transform the number to a String and add zeros (for example 1.0 will be 1.000000 and then we will cut it)
        String numberString = ""+value+"00000";
        // search where the decimal separator is
        int separatorPosition = numberString.indexOf(".");
        // cut the number X positions after the decimal separator
        return numberString.substring(0,separatorPosition+1+decimals);
    }

    public static double modulus(double[] vector) {
        // adds al the components squared
        double squared = 0.0;
        for (double component : vector) squared += Math.pow(component, 2);
        // returns the square root of the sum
        return Math.sqrt(squared);
    }
    // Overloads for easily calculating 2D and 3D vectors
    public static double modulus(double x, double y) { return modulus(new double[] { x, y }); }
    public static double modulus(double x, double y, double z) { return modulus(new double[] { x, y, z }); }


    // Database initialisator for first launch
    public static void loadDefaultStars() {
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