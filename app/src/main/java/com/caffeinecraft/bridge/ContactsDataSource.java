package com.caffeinecraft.bridge;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by drew on 11/7/14.
 */
public class ContactsDataSource {
    //Database fields
    private SQLiteDatabase database;
    private ContactSQLiteHelper dbHelper;
    private String[] allColumns = {
            ContactSQLiteHelper.COLUMN_ID,
            ContactSQLiteHelper.COLUMN_FN,
            ContactSQLiteHelper.COLUMN_LN
    };

    public ContactsDataSource(Context context) {
        dbHelper = new ContactSQLiteHelper(context);
    }

    public void open() throws SQLiteException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    //TODO: What stuff do we need to make a contact?
    public Contact createContact(String firstName, String lastName) {
        ContentValues values = new ContentValues();
        values.put(ContactSQLiteHelper.COLUMN_FN, firstName);
        values.put(ContactSQLiteHelper.COLUMN_LN, lastName);
        long insertId = database.insert(ContactSQLiteHelper.TABLE_CONTACTS, null, values);
        Cursor cursor = database.query(ContactSQLiteHelper.TABLE_CONTACTS, allColumns,
                ContactSQLiteHelper.COLUMN_ID + " = ?", new String[]{Long.toString(insertId)},
                null, null, null);
        cursor.moveToFirst();
        Contact newContact = cursorToContact(cursor);
        cursor.close();
        return newContact;
    }

    public void deleteContact(Contact contact) {
        long id = contact.getId();
        database.delete(ContactSQLiteHelper.TABLE_CONTACTS,
                ContactSQLiteHelper.COLUMN_ID + " = ?", new String[]{Long.toString(id)});
    }

    public List<Contact> getAllContacts() {
        List<Contact> contacts = new ArrayList<Contact>();

        Cursor cursor = database.query(ContactSQLiteHelper.TABLE_CONTACTS, allColumns,
                null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Contact contact = cursorToContact(cursor);
            contacts.add(contact);
            cursor.moveToNext();
        }
        cursor.close();
        return contacts;
    }

    private Contact cursorToContact(Cursor cursor) {
        Contact contact = new Contact();
        contact.setId(cursor.getLong(0));
        //TODO: Set other fields
        return contact;
    }
}
