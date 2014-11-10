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
        allcontacts = foo.getAllContacts();

        List<String> nameList = new ArrayList<String>();

        //Dummy data, until we can get real contacts.
        for(int i = 0; i< 10; i++)
        {
            Contact temp = new Contact();
            temp.setFirstName("Player");
            temp.setLastName("Number " + i);
            temp.setId( (long) i);
            allcontacts.add(temp);
        }

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

        for(int i = 0; i< allcontacts.size(); i++)
        {
            Log.d("BRiDGE", "CONTACT " + i + ", NAMED " + allcontacts.get(i).getLastName() + "'S ID IS " + allcontacts.get(i).getId());
        }

        lv.setOnItemClickListener(new ListView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> a, View v, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), ViewContact.class);
                Bundle bundle = new Bundle();
                long id = allcontacts.get( (int) l).getId();
                Log.d("BRiDGE", "ACTUAL CONTACTID IS " + id);
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
