package com.caffeinecraft.bridge;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;
import com.caffeinecraft.bridge.model.Contact;
import com.caffeinecraft.bridge.dao.ContactsDataSource;

public class ContactList extends Activity implements View.OnClickListener{

    List<Contact> allcontacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        ListView lv = (ListView) findViewById(R.id.listView);

        ContactsDataSource foo = new ContactsDataSource(this);
        foo.open();

        /*
        //Create dummy contacts
        List<Contact> temp2 = foo.getAllContacts();
        for(int i = 0; i< temp2.size();i++)
        {
            foo.deleteContact(temp2.get(i));
        }
        */
        /*
        for(int i = 0; i< 20; i++)
        {
            foo.createContact("Player","Number " + i);
        }
        */

        allcontacts = foo.getAllContacts();

        List<String> nameList = new ArrayList<String>();

        for(int i = 0;i<allcontacts.size();i++)
        {
            Contact temp = allcontacts.get(i);
            nameList.add(temp.getFirstName() + " " + temp.getLastName());
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                nameList);

        lv.setAdapter(arrayAdapter);

        lv.setOnItemClickListener(new ListView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> a, View v, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), ViewContact.class);
                Bundle bundle = new Bundle();
                long id = allcontacts.get( (int) l).getId();
                bundle.putLong("contactid",id);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        View btnNewContact = findViewById(R.id.buttonNewContact);
        btnNewContact.setOnClickListener(this);
        View btnImportContact = findViewById(R.id.buttonImportContact);
        btnImportContact.setOnClickListener(this);
        View btnConversationList = findViewById(R.id.buttonConversationList);
        btnConversationList.setOnClickListener(this);
        View btnContactList = findViewById(R.id.buttonContactList);
        btnContactList.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.buttonNewContact:
                //TODO:Create new contact
                //startActivity(new Intent(this, EditContact.class));
                break;
            case R.id.buttonImportContact:
                startActivity(new Intent(this, ImportContacts.class));
                break;
            case R.id.buttonConversationList:
                startActivity(new Intent(this, ConversationList.class));
                break;
            case R.id.buttonContactList:
                break;
        }
    }
}
