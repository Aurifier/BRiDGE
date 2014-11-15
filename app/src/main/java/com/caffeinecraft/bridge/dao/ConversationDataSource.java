package com.caffeinecraft.bridge.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.caffeinecraft.bridge.BridgeSQLiteHelper;
import com.caffeinecraft.bridge.model.Contact;
import com.caffeinecraft.bridge.model.Conversation;
import com.caffeinecraft.bridge.model.Message;

import java.util.ArrayList;
import java.util.List;

public class ConversationDataSource {
    private ContactsDataSource contactsDS;
    private SQLiteDatabase database;
    private BridgeSQLiteHelper dbHelper;
    private String[] allColumns = {
        BridgeSQLiteHelper.ConversationTable.COLUMN_ID,
        BridgeSQLiteHelper.ConversationTable.COLUMN_RECEIVED,
        BridgeSQLiteHelper.ConversationTable.COLUMN_CONTACT,
        BridgeSQLiteHelper.ConversationTable.COLUMN_MESSAGE,
        BridgeSQLiteHelper.ConversationTable.COLUMN_TIMESTAMP
    };

    public ConversationDataSource(Context context) {
        dbHelper = new BridgeSQLiteHelper(context);
        contactsDS = new ContactsDataSource(context);
    }

    public void open() {
        database = dbHelper.getWritableDatabase();
        contactsDS.open();
    }

    public void close() {
        dbHelper.close();
        contactsDS.close();
    }

    public Conversation getConversation(Contact contact) {
        Conversation conversation = new Conversation();
        conversation.setContact(contact);
        addMessages(conversation);
        return conversation;
    }

    public Message createMessage(Contact contact, boolean received, String message) {
        ContentValues values = new ContentValues();
        values.put(BridgeSQLiteHelper.ConversationTable.COLUMN_CONTACT, contact.getId());
        values.put(BridgeSQLiteHelper.ConversationTable.COLUMN_RECEIVED, received);
        values.put(BridgeSQLiteHelper.ConversationTable.COLUMN_MESSAGE, message);
        values.put(BridgeSQLiteHelper.ConversationTable.COLUMN_TIMESTAMP, System.currentTimeMillis()/1000L);
        long insertId = database.insert(BridgeSQLiteHelper.ConversationTable.name, null, values);
        Cursor cursor = database.query(BridgeSQLiteHelper.ConversationTable.name, allColumns,
            BridgeSQLiteHelper.ConversationTable.COLUMN_ID + " = ?", new String[]{Long.toString(insertId)},
            null, null, null);
        cursor.moveToFirst();
        Message newMessage = cursorToMessage(cursor);
        cursor.close();
        return newMessage;
    }

    //NOTE: If this becomes too slow, we will update it to not fetch Messages
    public List<Conversation> getAllConversations() {
        List<Conversation> conversations = new ArrayList<Conversation>();

        Cursor cursor = database.rawQuery(
            "SELECT "
                + "DISTINCT " + BridgeSQLiteHelper.ConversationTable.COLUMN_CONTACT
            + " FROM "
                + BridgeSQLiteHelper.ConversationTable.name,
            null
        );

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Contact contact = contactsDS.getContact(cursor.getLong(0));
            Conversation conversation = getConversation(contact);
            conversations.add(conversation);
            cursor.moveToNext();
        }
        cursor.close();
        return conversations;
    }

    private void addMessages(Conversation conversation) {
        Cursor messageCursor = database.rawQuery(
            "SELECT "
                + BridgeSQLiteHelper.ConversationTable.COLUMN_RECEIVED + ", "
                + BridgeSQLiteHelper.ConversationTable.COLUMN_MESSAGE + ", "
                + BridgeSQLiteHelper.ConversationTable.COLUMN_TIMESTAMP
            + " FROM "
                + BridgeSQLiteHelper.ConversationTable.name
            + " WHERE "
                + BridgeSQLiteHelper.ConversationTable.COLUMN_CONTACT + " = ?",
            new String[]{Long.toString(conversation.getContact().getId())}
        );
        messageCursor.moveToFirst();
        while(!messageCursor.isAfterLast()) {
            Message message = cursorToMessage(messageCursor);
            if(message != null)
                conversation.addMessage(message);
            messageCursor.moveToNext();
        }
        messageCursor.close();
    }

    private Message cursorToMessage(Cursor cursor) {
        Message message = new Message();
        boolean received = cursor.getInt(1) == 1;
        message.setReceived(received);
        message.setText(cursor.getString(3));
        message.setTimestamp(cursor.getLong(4));

        return message;
    }
}
