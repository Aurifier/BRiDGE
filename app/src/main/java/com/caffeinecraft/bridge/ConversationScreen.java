package com.caffeinecraft.bridge;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.caffeinecraft.bridge.dao.ContactsDataSource;
import com.caffeinecraft.bridge.dao.ConversationDataSource;
import com.caffeinecraft.bridge.model.Contact;
import com.caffeinecraft.bridge.model.Conversation;
import com.caffeinecraft.bridge.model.Message;

import java.util.ArrayList;
import java.util.List;

/**
* Created by ronitkumar on 11/10/14.
*/
public class ConversationScreen extends Activity implements View.OnClickListener{

    private Conversation thisconversation;
    ConversationDataSource conversationDataSource;
    ContactsDataSource contactsDataSource;
    TextView message;
    ListView lv;

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
        switch(v.getId()) {
            case R.id.imageSend:
                Message m = conversationDataSource.createMessage(thisconversation.getContact(), false, message.getText().toString());
                thisconversation.addMessage(m);
                this.recreate();
                break;
        }
    }
}