package com.caffeinecraft.bridge.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.caffeinecraft.bridge.model.Contact;
import com.caffeinecraft.bridge.BridgeSQLiteHelper;

import java.util.ArrayList;
import java.util.List;

public class ContactsDataSource {
    //Database fields
    private SQLiteDatabase database;
    private BridgeSQLiteHelper dbHelper;
    private String[] allColumns = {
        BridgeSQLiteHelper.ContactTable.COLUMN_ID,
        BridgeSQLiteHelper.ContactTable.COLUMN_FN,
        BridgeSQLiteHelper.ContactTable.COLUMN_LN
    };

    public ContactsDataSource(Context context) {
        dbHelper = new BridgeSQLiteHelper(context);
    }

    public void open() throws SQLiteException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Contact createContact(String firstName, String lastName) {
        ContentValues values = new ContentValues();
        values.put(BridgeSQLiteHelper.ContactTable.COLUMN_FN, firstName);
        values.put(BridgeSQLiteHelper.ContactTable.COLUMN_LN, lastName);
        long insertId = database.insert(BridgeSQLiteHelper.ContactTable.name, null, values);
        Cursor cursor = database.query(BridgeSQLiteHelper.ContactTable.name, allColumns,
            BridgeSQLiteHelper.ContactTable.COLUMN_ID + " = ?", new String[]{Long.toString(insertId)},
            null, null, null);
        cursor.moveToFirst();
        Contact newContact = cursorToContact(cursor);
        cursor.close();
        return newContact;
    }

    public void deleteContact(Contact contact) {
        long id = contact.getId();
        //Delete emails for this contact
        database.delete(BridgeSQLiteHelper.ContactEmailTable.name,
            BridgeSQLiteHelper.ContactEmailTable.COLUMN_CONTACT + " = ?",
            new String[]{Long.toString(id)});
        //Delete the contact
        database.delete(BridgeSQLiteHelper.ContactTable.name,
            BridgeSQLiteHelper.ContactTable.COLUMN_ID + " = ?", new String[]{Long.toString(id)});
    }

    public List<Contact> getAllContacts() {
        List<Contact> contacts = new ArrayList<Contact>();

        Cursor cursor = database.query(BridgeSQLiteHelper.ContactTable.name, allColumns,
            null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Contact contact = cursorToContact(cursor);
            Cursor emailCursor = database.rawQuery(
                "SELECT "
                    + "e." + BridgeSQLiteHelper.ContactEmailTable.COLUMN_EMAIL
                + " FROM "
                    + BridgeSQLiteHelper.ContactTable.name + " AS c "
                + "LEFT JOIN "
                    + BridgeSQLiteHelper.ContactEmailTable.name + " AS e "
                + "ON "
                    + "c." + BridgeSQLiteHelper.ContactTable.COLUMN_ID + " = "
                    + " e." + BridgeSQLiteHelper.ContactEmailTable.COLUMN_CONTACT
                + " WHERE "
                    + "c." + BridgeSQLiteHelper.ContactTable.COLUMN_ID + " = ?",
                new String[]{Long.toString(contact.getId())}
            );
            emailCursor.moveToFirst();
            while(!emailCursor.isAfterLast()) {
                contact.addEmail(emailCursor.getString(0));
            }
            emailCursor.close();
            contacts.add(contact);
            cursor.moveToNext();
        }
        cursor.close();
        return contacts;
    }

    private Contact cursorToContact(Cursor cursor) {
        Contact contact = new Contact();
        contact.setId(cursor.getLong(0));
        contact.setFirstName(cursor.getString(1));
        contact.setLastName(cursor.getString(2));

        return contact;
    }
}
