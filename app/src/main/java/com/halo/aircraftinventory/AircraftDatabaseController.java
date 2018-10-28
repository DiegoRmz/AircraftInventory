package com.halo.aircraftinventory;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class AircraftDatabaseController {
    AircraftDBHelper dbHelper;
    SQLiteDatabase database;

    public AircraftDatabaseController(Context context){
        dbHelper = new AircraftDBHelper(context);
        database = dbHelper.getWritableDatabase();
    }

    public void close(){
        database.close();
    }

    public long insert(Aircraft fsh){
        ContentValues values = new ContentValues();
        values.put(Aircraft.COLUMN_PRODUCER, fsh.getProducer());
        values.put(Aircraft.COLUMN_MODEL, fsh.getModel());
        values.put(Aircraft.COLUMN_STATUS, fsh.getCurrentStatus());
        values.put(Aircraft.COLUMN_PILOT,fsh.getPilot());

        return database.insert(Aircraft.TABLE_NAME, null, values);
    }

    public Cursor selectAircraft(String selection, String[] selectionArgs){
        String columns[] = {
                Aircraft.COLUMN_ID,
             Aircraft.COLUMN_PRODUCER,
            Aircraft.COLUMN_MODEL,
            Aircraft.COLUMN_STATUS,
            Aircraft.COLUMN_PILOT
        };
        return database.query(Aircraft.TABLE_NAME,columns,selection,selectionArgs,null,null,null);
    }

    public long delete(Aircraft fsh){
        String where = Aircraft.COLUMN_ID+"=?";
        String[] whereArgs = {
                ""+fsh.getId()
        };
        return database.delete(Aircraft.TABLE_NAME,where,whereArgs);
    }

    public long update(Aircraft fsh){
        ContentValues values = new ContentValues();

        values.put(Aircraft.COLUMN_STATUS, fsh.getCurrentStatus());
        values.put(Aircraft.COLUMN_PILOT,fsh.getPilot());

        String where = Aircraft.COLUMN_ID+"=?";
        String[] whereArgs = {
                ""+fsh.getId()
        };

        return database.update(Aircraft.TABLE_NAME,values,where,whereArgs);
    }
}
