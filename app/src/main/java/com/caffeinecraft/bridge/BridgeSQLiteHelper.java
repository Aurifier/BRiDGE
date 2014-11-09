package com.caffeinecraft.bridge;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class BridgeSQLiteHelper extends SQLiteOpenHelper {
    public interface ContactTable {
        public static final String name = "contacts";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_FN = "first_name";
        public static final String COLUMN_LN = "last_name";

        public static final String TABLE_CREATE =
            "CREATE TABLE " + name + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_FN + " TEXT NOT NULL, "
                + COLUMN_LN + " TEXT"
            + ");";
    }

    public interface ContactEmailTable {
        public static final String name = "contact_emails";
        public static final String COLUMN_CONTACT = "contact";
        public static final String COLUMN_EMAIL = "email";

        public static final String TABLE_CREATE =
            "CREATE TABLE " + name + "("
                + COLUMN_CONTACT + " INTEGER PRIMARY KEY, "
                + COLUMN_EMAIL + " TEXT NOT NULL, "
                + "FOREIGN KEY(" + COLUMN_CONTACT + ") REFERENCES "
                + ContactTable.name + "(" + ContactTable.COLUMN_ID + ")"
            + ");";
    }

    private static final String DATABASE_NAME = "BRiDGE.db";
    private static final int DATABASE_VERSION = 1;

    public BridgeSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(ContactTable.TABLE_CREATE);
        database.execSQL(ContactEmailTable.TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        Log.w(BridgeSQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                + newVersion + ", which will destroy all old data");

        //While we're testing, just drop everything and rebuild
        if(oldVersion < 5) {
            database.execSQL("DROP TABLE IF EXISTS " + ContactTable.name);
            database.execSQL("DROP TABLE IF EXISTS " + ContactEmailTable.name);
        }
        onCreate(database);
    }
}
