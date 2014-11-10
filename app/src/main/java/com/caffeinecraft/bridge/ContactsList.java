package com.caffeinecraft.bridge;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import com.caffeinecraft.bridge.model.Contact;
import java.util.ArrayList;
import java.util.List;
import android.widget.AdapterView.OnItemClickListener;
import com.caffeinecraft.bridge.ViewContact;

import com.caffeinecraft.bridge.dao.ContactsDataSource;

public class ContactsList extends Activity implements View.OnClickListener{

    private ListView lv;
    List<Contact> contactlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.contacts_list);

        lv = (ListView) findViewById(R.id.listView);

        ContactsDataSource foo = new ContactsDataSource(this);
        foo.open();
        contactlist = foo.getAllContacts();

        List<String> nameList = new ArrayList<String>();

        for(int i = 0;i<contactlist.size();i++)
        {
            Contact temp = contactlist.get(i);
            nameList.add(temp.getFirstName() + " " + temp.getLastName());
        }

        //Dummy data, until we can get real contacts.
        for(int i = 0; i< 20; i++)
        {
            nameList.add("Player " + i);
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                nameList);

        lv.setAdapter(arrayAdapter);

        lv.setOnItemClickListener(new ListView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> a, View v, int i, long l) {
                //Create the intent
                Intent intent = new Intent(getApplicationContext(), ViewContact.class);

                //Create the bundle
                Bundle bundle = new Bundle();
                //Add your data to bundle
                bundle.putString("row number",""+l);
                //Add the bundle to the intent
                intent.putExtras(bundle);

                //Fire that second activity
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
                startActivity(new Intent(this, ImportContacts.class));
                break;
            case R.id.buttonImportContact:
                startActivity(new Intent(this, ImportContacts.class));
                break;
            case R.id.buttonConversationList:
                startActivity(new Intent(this, ImportContacts.class));
                break;
            case R.id.buttonContactList:
                //You're already in ContactList, silly
                break;
        }
    }
}
