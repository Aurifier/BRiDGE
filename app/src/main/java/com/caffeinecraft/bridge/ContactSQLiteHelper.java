package com.caffeinecraft.bridge;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by drew on 11/7/14.
 */
public class ContactSQLiteHelper extends SQLiteOpenHelper {
    public static final String TABLE_CONTACTS = "contacts";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_FN = "first_name";
    public static final String COLUMN_LN = "last_name";

    private static final String DATABASE_NAME = "BRiDGE.db";
    private static final int DATABASE_VERSION = 1;

    //Database creation string
    private static final String DATABASE_CREATE =
            "CREATE TABLE " + TABLE_CONTACTS + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_FN + " TEXT NOT NULL, "
            + COLUMN_LN + " TEXT"
            + ");";

    public ContactSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        Log.w(ContactSQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                + newVersion + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
        onCreate(database);
    }
}
