package com.arianensis.starnavigator.DB;

import android.provider.BaseColumns;

import java.util.Locale;

// This class contains the information of the Star table for easy manipulation of data
public class StarsContract  {
    private StarsContract(){}
    public static class StarsEntry implements BaseColumns {
        public static final String TABLE_NAME ="Star";
        public static final String PK_COLUMN_NAME = "name";
        public static final String PK_COLUMN_DESC = "TEXT PRIMARY KEY";
        // all the columns in an array so we can easily add, delete or modify them
        public static final String[] COLUMN_NAMES = new String[]{
                "x",
                "y",
                "z",
                "spectre",
                "is_custom" };
        public static final String[] COLUMN_DESCS = new String[]{
                "NUMBER NOT NULL",
                "NUMBER NOT NULL",
                "NUMBER NOT NULL",
                "TEXT NOT NULL",
                "TEXT NOT NULL" };
        public static String getCreateColumns(){
            String columns = "";
            for (int i=0; i<COLUMN_NAMES.length; i++) {
                columns += ", " + StarsEntry.COLUMN_NAMES[i] + " " + StarsEntry.COLUMN_DESCS[i];
            }
            return columns;
        }
    }
}
