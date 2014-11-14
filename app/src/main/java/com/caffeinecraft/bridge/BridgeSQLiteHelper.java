package com.caffeinecraft.bridge;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class BridgeSQLiteHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "BRiDGE.db";
    private static final int DATABASE_VERSION = 3;

    public interface ContactTable {
        public static final String name = "contacts";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_FN = "first_name";
        public static final String COLUMN_LN = "last_name";
        public static final String COLUMN_PREFERRED_METHOD = "preferred";

        public static final String TABLE_CREATE =
            "CREATE TABLE " + name + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_FN + " TEXT NOT NULL, "
                + COLUMN_LN + " TEXT, "
                + COLUMN_PREFERRED_METHOD + " TEXT, "
                + "FOREIGN KEY(" + COLUMN_PREFERRED_METHOD + ") REFERENCES "
                + ContactMethodTable.name + "(" + ContactMethodTable.COLUMN_VALUE + ")"
            + ");";
    }

    public interface ContactMethodTable {
        public static final String name = "contact_methods";
        public static final String COLUMN_CONTACT = "contact";
        public static final String COLUMN_TYPE = "type";
        public static final String COLUMN_VALUE = "value";

        public static final String TABLE_CREATE =
            "CREATE TABLE " + name + "("
                + COLUMN_CONTACT + " INTEGER, "
                + COLUMN_VALUE + " TEXT PRIMARY KEY, "
                + COLUMN_TYPE + " TEXT NOT NULL, "
                + "FOREIGN KEY(" + COLUMN_CONTACT + ") REFERENCES "
                + ContactTable.name + "(" + ContactTable.COLUMN_ID + ")"
            + ");";
    }

    public interface ConversationTable {
        public static final String name = "conversations";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_CONTACT = "contact";
        public static final String COLUMN_RECEIVED = "received";
        public static final String COLUMN_TIMESTAMP = "timestamp";

        public static final String TABLE_CREATE =
            "CREATE TABLE " + name + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_CONTACT + " INTEGER NOT NULL, "
                + COLUMN_RECEIVED + " INTEGER NOT NULL, "
                + COLUMN_TIMESTAMP + " INTEGER, "
                + "FOREIGN KEY(" + COLUMN_CONTACT + ") REFERENCES "
                + ContactTable.name + "(" + ContactTable.COLUMN_ID + ")"
            + ");";
    }

    public BridgeSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(ContactTable.TABLE_CREATE);
        database.execSQL(ContactMethodTable.TABLE_CREATE);
        database.execSQL(ConversationTable.TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        Log.w(BridgeSQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                + newVersion + ", which will destroy all old data");

        //While we're testing, just drop everything and rebuild
        if(oldVersion < 5) {
            database.execSQL("DROP TABLE IF EXISTS " + ContactMethodTable.name);
            database.execSQL("DROP TABLE IF EXISTS " + ContactTable.name);
            database.execSQL("DROP TABLE IF EXISTS " + ConversationTable.name);
        }
        onCreate(database);
    }
}
