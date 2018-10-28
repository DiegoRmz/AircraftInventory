package com.halo.aircraftinventory;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AircraftDBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "AircraftDatabase";

    private static final String DESTROY = "DROP TABLE IF EXISTS "+Aircraft.TABLE_NAME;

    public static final String SQL_CREATE = "CREATE TABLE " + Aircraft.TABLE_NAME + " ("
            + Aircraft.COLUMN_ID + " INTEGER PRIMARY KEY, "
            + Aircraft.COLUMN_PRODUCER+ " TEXT, "
            + Aircraft.COLUMN_MODEL + " INTEGER, "
            + Aircraft.COLUMN_STATUS + " INTEGER, "
            + Aircraft.COLUMN_PILOT + " TEXT )";

    private static int DATABASE_VERSION = 2;

    public AircraftDBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DESTROY);
        onCreate(db);
    }
}
