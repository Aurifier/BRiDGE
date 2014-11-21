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
        BridgeSQLiteHelper.ContactTable.COLUMN_LN,
        BridgeSQLiteHelper.ContactTable.COLUMN_PREFERRED_METHOD
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

    public Contact getContact(long id) {
        Contact contact = null;
        Cursor cursor = database.query(BridgeSQLiteHelper.ContactTable.name, allColumns,
            BridgeSQLiteHelper.ContactTable.COLUMN_ID + " =?", new String[]{Long.toString(id)},
            null, null, null);

        cursor.moveToFirst();
        if(!cursor.isAfterLast()) {
            contact = cursorToContact(cursor);
            addContactMethods(contact);
        }
        cursor.close();

        return contact;
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

        //Set preferred method
        if(contact.getPreferredContactMethod() != null) {
            ContentValues preference = new ContentValues();
            preference.put(BridgeSQLiteHelper.ContactTable.COLUMN_PREFERRED_METHOD, contact.getPreferredContactMethod().getValue());
            database.update(BridgeSQLiteHelper.ContactTable.name, preference,
                    BridgeSQLiteHelper.ContactTable.COLUMN_ID + " =?", new String[]{Long.toString(id)});
        }
    }

    public List<Contact> getAllContacts() {
        List<Contact> contacts = new ArrayList<Contact>();

        Cursor cursor = database.query(BridgeSQLiteHelper.ContactTable.name, allColumns,
            null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Contact contact = cursorToContact(cursor);
            addContactMethods(contact);
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
        contact.setPreferredContactMethod(getMethodByValue(cursor.getString(3)));

        return contact;
    }

    private ContactMethod getMethodByValue(String value) {
        if(value == null)
            return null;

        Log.d(TAG, "Looking for contact method: " + value);
        Cursor methodCursor = database.rawQuery(
            "SELECT "
                + BridgeSQLiteHelper.ContactMethodTable.COLUMN_VALUE + ", "
                + BridgeSQLiteHelper.ContactMethodTable.COLUMN_TYPE
            + " FROM "
                + BridgeSQLiteHelper.ContactMethodTable.name
            + " WHERE "
                + BridgeSQLiteHelper.ContactMethodTable.COLUMN_VALUE + " = ?",
            new String[]{value}
        );

        methodCursor.moveToFirst();
        if(methodCursor.isAfterLast()) {
            Log.e(TAG, "Missing contact method with value '" + value + "'. Bad juju.");
        }
        return cursorToMethod(methodCursor);
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

    private void addContactMethods(Contact contact) {
        Cursor methodCursor = database.rawQuery(
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
        methodCursor.moveToFirst();
        while(!methodCursor.isAfterLast()) {
            ContactMethod method = cursorToMethod(methodCursor);
            if(method != null)
                contact.addContactMethod(method);
            methodCursor.moveToNext();
        }
        methodCursor.close();
    }
}
