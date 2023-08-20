package com.example.jps;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "jobDatabase";
    private static final int DATABASE_VERSION = 4;
    private static final String TABLE_NAME = "jobs";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "company_name";
    private static final String KEY_JOB_TYPE = "job_type";
    private static final String KEY_Contract_TYPE = "contract";
    private static final String KEY_Address = "Address";
    private static final String KEY_Start = "Start_Day";
    private static final String KEY_End = "End_Day";
    private static final String KEY_Gyung = "Gyung";
    private static final String KEY_Hak = "Hak";
    private static final String KEY_Access = "Access";
    private static final String KEY_City = "City";
    // 필요에 따라 더 많은 컬럼을 추가...

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_NAME + " TEXT,"
                + KEY_JOB_TYPE + " TEXT,"
                + KEY_Contract_TYPE + " TEXT,"
                + KEY_Address + " TEXT,"
                + KEY_Start +" TEXT,"
                + KEY_End +" TEXT,"
                + KEY_Gyung +" TEXT,"
                + KEY_Hak +" TEXT,"
                + KEY_Access +" TEXT,"
                + KEY_City +" TEXT" + ")";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void addJob(List<String[]> dataList) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            for (String[] data : dataList) {
                ContentValues values = new ContentValues();
                values.put(KEY_NAME, data[1]);
                values.put(KEY_JOB_TYPE, data[2]);
                values.put(KEY_Contract_TYPE, data[3]);
                values.put(KEY_Address, data[11]);
                values.put(KEY_Start, data[20]);
                values.put(KEY_End, data[21]);
                values.put(KEY_Gyung, data[7]);
                values.put(KEY_Hak, data[8]);
                values.put(KEY_Access, data[16]);
                values.put(KEY_City, data[22]);
                db.insert(TABLE_NAME, null, values);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    public Cursor getAllJobs() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }
}
