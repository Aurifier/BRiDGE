package com.caffeinecraft.bridge.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.caffeinecraft.bridge.model.Contact;
import com.caffeinecraft.bridge.BridgeSQLiteHelper;
import com.caffeinecraft.bridge.model.ContactMethod;

import java.util.ArrayList;
import java.util.List;

public class ContactsDataSource {
    private static final String TAG = "ContactsDataSource";
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
        //Delete contact methods for this contact
        database.delete(BridgeSQLiteHelper.ContactMethodTable.name,
            BridgeSQLiteHelper.ContactMethodTable.COLUMN_CONTACT + " = ?",
            new String[]{Long.toString(id)});
        //Delete the contact
        database.delete(BridgeSQLiteHelper.ContactTable.name,
            BridgeSQLiteHelper.ContactTable.COLUMN_ID + " = ?", new String[]{Long.toString(id)});
    }

    //TODO: Set preferred method
    public void updateContact(Contact contact) {
        //Stub, you can use this and pretend it works until I actually finish it
        long id = contact.getId();

        //First, update name
        ContentValues names = new ContentValues();
        names.put(BridgeSQLiteHelper.ContactTable.COLUMN_FN, contact.getFirstName());
        names.put(BridgeSQLiteHelper.ContactTable.COLUMN_LN, contact.getLastName());
        database.update(BridgeSQLiteHelper.ContactTable.name, names,
            BridgeSQLiteHelper.ContactTable.COLUMN_ID + " =?", new String[]{Long.toString(id)});

        //Then, update contact methods
        //Because it's easy, we'll nuke 'em all and put 'em back
        database.delete(BridgeSQLiteHelper.ContactMethodTable.name,
            BridgeSQLiteHelper.ContactMethodTable.COLUMN_CONTACT + " =?",
            new String[]{Long.toString(id)});
        for (ContactMethod method : contact.getContactMethods()) {
            ContentValues methodValues = new ContentValues();
            methodValues.put(BridgeSQLiteHelper.ContactMethodTable.COLUMN_CONTACT, id);
            methodValues.put(BridgeSQLiteHelper.ContactMethodTable.COLUMN_TYPE, method.getType().name());
            methodValues.put(BridgeSQLiteHelper.ContactMethodTable.COLUMN_VALUE, method.getValue());
            database.insert(BridgeSQLiteHelper.ContactMethodTable.name, null, methodValues);
        }
    }

    //TODO: Get preferred method for each contact
    public List<Contact> getAllContacts() {
        List<Contact> contacts = new ArrayList<Contact>();

        Cursor cursor = database.query(BridgeSQLiteHelper.ContactTable.name, allColumns,
            null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Contact contact = cursorToContact(cursor);
            //TODO: This fetches all methods and calls them emails
            Cursor emailCursor = database.rawQuery(
                "SELECT "
                    + "e." + BridgeSQLiteHelper.ContactMethodTable.COLUMN_VALUE
                    + ", e." + BridgeSQLiteHelper.ContactMethodTable.COLUMN_TYPE
                + " FROM "
                    + BridgeSQLiteHelper.ContactTable.name + " AS c "
                + "LEFT JOIN "
                    + BridgeSQLiteHelper.ContactMethodTable.name + " AS e "
                + "ON "
                    + "c." + BridgeSQLiteHelper.ContactTable.COLUMN_ID + " = "
                    + " e." + BridgeSQLiteHelper.ContactMethodTable.COLUMN_CONTACT
                + " WHERE "
                    + "c." + BridgeSQLiteHelper.ContactTable.COLUMN_ID + " = ?",
                new String[]{Long.toString(contact.getId())}
            );
            emailCursor.moveToFirst();
            while(!emailCursor.isAfterLast()) {
                ContactMethod method = cursorToMethod(emailCursor);
                if(method != null)
                    contact.addContactMethod(method);
                emailCursor.moveToNext();
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

    private ContactMethod cursorToMethod(Cursor cursor) {
        //I don't know why I have to make this check, but apparently an empty row counts as a result
        String typeString = cursor.getString(1);
        if(typeString == null)
            return null;

        ContactMethod method = new ContactMethod();
        method.setValue(cursor.getString(0));
        method.setType(ContactMethod.Type.valueOf(cursor.getString(1)));

        return method;
    }
}
