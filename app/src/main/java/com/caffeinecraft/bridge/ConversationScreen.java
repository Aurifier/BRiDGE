package com.caffeinecraft.bridge;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.caffeinecraft.bridge.dao.ContactsDataSource;
import com.caffeinecraft.bridge.dao.ConversationDataSource;
import com.caffeinecraft.bridge.model.Contact;
import com.caffeinecraft.bridge.model.ContactMethod;
import com.caffeinecraft.bridge.model.Conversation;
import com.caffeinecraft.bridge.model.Message;

import java.util.ArrayList;
import java.util.List;

/**
* Created by ronitkumar on 11/10/14.
*/
public class ConversationScreen extends Activity implements View.OnClickListener{
    private static String SENT = "SMS_SENT";
    private static String DELIVERED = "SMS_DELIVERED";
    private static int MAX_SMS_MESSAGE_LENGTH = 160;
    private static String TAG = "ConversationScreen";

    private Conversation thisconversation;
    ConversationDataSource conversationDataSource;
    ContactsDataSource contactsDataSource;
    TextView message;
    ListView lv;
    private static Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Generic initialize function calls
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conversation);
        conversationDataSource = new ConversationDataSource(this);
        contactsDataSource = new ContactsDataSource(this);
        conversationDataSource.open();
        contactsDataSource.open();

        //Set thiscontact to the correct contact given in the savedInstanceState
        Bundle bundle = getIntent().getExtras();
        Long contactId = bundle.getLong("contact_id");
        Contact contact = contactsDataSource.getContact(contactId);
        thisconversation = conversationDataSource.getConversation(contact);
        List<Conversation> allconversations = conversationDataSource.getAllConversations();

        //Set List View
        lv = (ListView) findViewById(R.id.listview);
        List<Message> messages = thisconversation.getMessages();
        List<String> messagesTwo = new ArrayList<String>();
        for(Message message : messages) {
            messagesTwo.add(message.getText());
        }

        //Implement custom adapter here for listview chat bubbles
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                R.layout.conversation_view,
                R.id.firstLine,
                messagesTwo);
        if(lv == null) {
            Log.e("ConversationScreen", "List view is null! NOOOOOOOO!");
        }
        lv.setAdapter(arrayAdapter);

        //Set EditTexts
        message=(TextView)findViewById(R.id.messageText);

        //Set Button OnclickListeners
        View sendButton = findViewById(R.id.imageSend);
        sendButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        mContext = this;
        switch(v.getId()) {
            case R.id.imageSend:
                Contact contact = thisconversation.getContact();
                Message m = conversationDataSource.createMessage(contact, false, message.getText().toString());
                thisconversation.addMessage(m);
                sendMessage(contact, m);
                this.recreate();
                break;
        }
    }

    private static void sendMessage(Contact contact, Message message) {
        ContactMethod method = contact.getPreferredContactMethod();
        if(method == null) {
            //TODO: Offer a toast saying you haven't set up a preferred method for this contact
            Log.e(TAG, "No preferred method set up for this contact!");
            return;
        }

        switch (method.getType()) {
            case SMS:
                sendSMS(method.getValue(), message.getText());
                break;
            case EMAIL:
                sendEmail(method.getValue(), message.getText());
                break;
            default:
                Log.e(TAG, "Unknown contact method type.");
                break;
        }
    }

    private static void sendEmail(String address, String message) {
        Log.w(TAG, "STUB: Should be sending '" + message + "' to " + address);
    }

    private static void sendSMS(String phoneNumber, String message) {
        PendingIntent piSent = PendingIntent.getBroadcast(mContext, 0, new Intent(SENT), 0);
        PendingIntent piDelivered = PendingIntent.getBroadcast(mContext, 0,new Intent(DELIVERED), 0);
        SmsManager smsManager = SmsManager.getDefault();

        int length = message.length();
        if(length > MAX_SMS_MESSAGE_LENGTH) {
            ArrayList<String> messageList = smsManager.divideMessage(message);
            smsManager.sendMultipartTextMessage(phoneNumber, null, messageList, null, null);
        } else {
            smsManager.sendTextMessage(phoneNumber, null, message, piSent, piDelivered);
        }
    }
}