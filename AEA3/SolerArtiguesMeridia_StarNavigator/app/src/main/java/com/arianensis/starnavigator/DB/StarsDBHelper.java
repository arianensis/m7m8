package com.arianensis.starnavigator.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.arianensis.starnavigator.DB.StarsContract.*;
import com.arianensis.starnavigator.Star;

import java.util.ArrayList;

// This class connects the database with the Star class
public class StarsDBHelper extends SQLiteOpenHelper {
    // Default constructor by Marta
    public StarsDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static final int DATABASE_VERSION = 13;
    public static final String DATABASE_NAME = "stars.db";
    private static final String SQL_CREATE_TABLE = "CREATE TABLE " + StarsEntry.TABLE_NAME + "(" +
            StarsEntry.PK_COLUMN_NAME + " " + StarsEntry.PK_COLUMN_DESC + StarsEntry.getCreateColumns() + ")";
    private static final String SQL_ERASE = "DELETE FROM Star";


    @Override
    // method to create the table if it's the first execution
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    // method to rebuild the table when settings change
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE Star;");
        db.execSQL(SQL_CREATE_TABLE);
    }

    // Method receives a Star object and adds the corresponding entry to the database
    public boolean insertStar(SQLiteDatabase db, Star star) {
        //Check the bd is open
        if (db.isOpen()){
            //Creation of the register for insert object with the content values
            ContentValues values = new ContentValues();

            values.put(StarsEntry.PK_COLUMN_NAME, star.getName());

            //Insert all the values in order
            for (int i=0; i<star.toTableValues().length; i++) {
                values.put(StarsEntry.COLUMN_NAMES[i], star.toTableValues()[i]);
            }

            db.insert(StarsEntry.TABLE_NAME, null, values);
            return true;
        }else{
            Log.i("SQL","Database is closed");
            return false;
        }
    }

    // Method creates an ArrayList of Star from the objects in the database
    // second argument can be for example "order by name"
    public ArrayList<Star> listStars(SQLiteDatabase db, String conditions) {
        ArrayList<Star> result = new ArrayList<>();
        String query = "SELECT * FROM Star " + conditions;
        //Check the bd is open
        if (db.isOpen()){
            Cursor cursor = db.rawQuery(query, new String[0]);
            while (cursor.moveToNext())
            {
                String c_name = cursor.getString(0);
                double c_x = cursor.getDouble(1);
                double c_y = cursor.getDouble(2);
                double c_z = cursor.getDouble(3);
                String c_spectre = cursor.getString(4);
                boolean c_is_custom = Boolean.parseBoolean(cursor.getString(5));
                result.add(Star.fromTableValues(c_name, c_x, c_y, c_z, c_spectre, c_is_custom));
            }
            cursor.close();
        }else{
            Log.i("SQL","Database is closed");
        }
        return result;
    }
    // Overload for the list method without specifying condition (infers empty condition)
    public ArrayList<Star> listStars(SQLiteDatabase db) { return listStars(db, ""); }

    // Method deletes everything in the Stars table
    public boolean erase(SQLiteDatabase db) {
        if (db.isOpen()){
            db.execSQL(SQL_ERASE);
            Star.deleteAll();
            return true;
        }else{
            Log.i("SQL","Database is closed");
            return false;
        }
    }

    // Method deletes all user-added data in the Stars table
    public boolean eraseCustom(SQLiteDatabase db) {
        if (db.isOpen()){
            db.execSQL(SQL_ERASE + " WHERE is_custom='true'");
            Star.deleteCustom();
            return true;
        }else{
            Log.i("SQL","Database is closed");
            return false;
        }
    }

    // Method deletes given Star
    public boolean erase(SQLiteDatabase db, String name)
    {
        if (!Star.exists(name)) return false;
        if (db.isOpen()){
            // executes the erase query with the WHERE clause
            db.execSQL(SQL_ERASE + " WHERE NAME='" + name + "'");
            Star.delete(Star.getStarByName(name));
            return true;
        }else{
            Log.i("SQL","Database is closed");
            return false;
        }
    }
}
