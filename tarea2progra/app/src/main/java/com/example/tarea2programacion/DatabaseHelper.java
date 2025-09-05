package com.example.tarea2programacion;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "fingerprint.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_RECORDS = "records";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NATIONALITY = "nationality";
    public static final String COLUMN_STATUS = "status";
    public static final String COLUMN_REGISTRATION_TIME = "registration_time";

    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_RECORDS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NATIONALITY + " TEXT, " +
                    COLUMN_STATUS + " TEXT, " +
                    COLUMN_REGISTRATION_TIME + " REAL);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECORDS);
        onCreate(db);
    }

    public void addRecord(String nationality, String status, double time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NATIONALITY, nationality);
        values.put(COLUMN_STATUS, status);
        values.put(COLUMN_REGISTRATION_TIME, time);
        db.insert(TABLE_RECORDS, null, values);
        db.close();
    }

    public Cursor getRecords(String nationality, double minTime, double maxTime) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = null;
        List<String> selectionArgs = new ArrayList<>();

        StringBuilder selectionBuilder = new StringBuilder();

        if (nationality != null && !nationality.isEmpty() && !"Todas".equalsIgnoreCase(nationality)) {
            selectionBuilder.append(COLUMN_NATIONALITY + " = ?");
            selectionArgs.add(nationality);
        }

        if (minTime >= 0) {
            if (selectionBuilder.length() > 0) {
                selectionBuilder.append(" AND ");
            }
            selectionBuilder.append(COLUMN_REGISTRATION_TIME + " >= ?");
            selectionArgs.add(String.valueOf(minTime));
        }

        if (maxTime >= 0) {
            if (selectionBuilder.length() > 0) {
                selectionBuilder.append(" AND ");
            }
            selectionBuilder.append(COLUMN_REGISTRATION_TIME + " <= ?");
            selectionArgs.add(String.valueOf(maxTime));
        }

        if (selectionBuilder.length() > 0) {
            selection = selectionBuilder.toString();
        }

        String[] args = selectionArgs.toArray(new String[0]);

        return db.query(TABLE_RECORDS, null, selection, args, null, null, COLUMN_ID + " DESC");
    }
}


