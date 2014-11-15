package com.caffeinecraft.bridge;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.caffeinecraft.bridge.dao.ConversationDataSource;
import com.caffeinecraft.bridge.model.Conversation;

import java.util.List;


/**
 * Created by ronitkumar on 11/10/14.
 */
public class ConversationList extends Activity implements View.OnClickListener {
    String[] contactName = new String[]{"Contact One", "Contact Two", "Contact Three",
            "Contact Four", "Contact Five", "Contact Six", "Contact Seven", "Contact Eight",
            "Contact Nine", "Contact Ten"};
    private ConversationDataSource foo;
    private List<Conversation> conversationList;

    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        // use your custom layout
        setContentView(R.layout.activity_conversation_list);
        foo = new ConversationDataSource(this);
        foo.open();
        ListView lv = (ListView) findViewById(R.id.listView);
        conversationList = foo.getAllConversations();
        ArrayAdapter<Conversation> adapter = new ArrayAdapter<Conversation>(this,
                R.layout.conversation_view, R.id.firstLine, conversationList);
        lv.setAdapter(adapter);


        //Set what happens when you click on something in listview
        lv.setOnItemClickListener(new ListView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> a, View v, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), ConversationScreen.class);
                intent.putExtra("id", String.valueOf(l));
                intent.putExtra("Contact", contactName[i]);
                intent.putExtra("contact_id", conversationList.get(i).getContact().getId());
                startActivity(intent);
            }
        });

        View btnNewMessage = findViewById(R.id.buttonNewMessage);
        btnNewMessage.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonNewMessage:
                Intent intent = new Intent(getApplicationContext(), ConversationScreen.class);
                startActivity(intent);
                break;
        }
    }
}