package com.caffeinecraft.bridge;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

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
    ConversationDataSource foo;
    TextView message;
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Generic initialize function calls
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conversation);
//        foo = new ConversationDataSource(this);
//        foo.open();

        //Set thiscontact to the correct contact given in the savedInstanceState
        Bundle bundle = getIntent().getExtras();
        Long messageid = bundle.getLong("messageid");
//        List<Contact> allconversations = foo.getAllConversations();
//        for(int i = 0;i<allconversations.size();i++)
//        {
//            if(allconversations.get(i).getId()==messageid)
//            {thisconversation = allconversations.get(i);}
//        }

        //Set List View
        lv = (ListView) findViewById(R.id.listView);
        List<Message> messages = thisconversation.getMessages();
        List<String> messagesTwo = new ArrayList<String>();
//        for(Message message : messagesTwo) {
//            messagesTwo.add(message.getValue());
//        }

        //Implement custom adapter here for listview chat bubbles
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                R.layout.conversation_view,
                messagesTwo);
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
            case R.id.buttonSave:
                Message m = foo.createMessage(thisconversation.getContact(), false, message.getText().toString());
                thisconversation.addMessage(m);
                this.recreate();
                break;
        }
    }
}