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
import com.caffeinecraft.bridge.model.ContactMethod;

public class ContactList extends Activity implements View.OnClickListener{

    ContactsDataSource foo;
    List<Contact> allcontacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Generic initialize function calls
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        foo = new ContactsDataSource(this);
        foo.open();

        //Set up Listview
        ListView lv = (ListView) findViewById(R.id.listView);
        allcontacts = foo.getAllContacts();
        List<String> nameList = new ArrayList<String>();
        for(int i = 0;i<allcontacts.size();i++) {
            Contact temp = allcontacts.get(i);
            if (temp.getFirstName() != null || temp.getLastName() != null) {
                    if (temp.getFirstName() == null) {
                        nameList.add(temp.getLastName());
                        temp.setFirstName("");
                        allcontacts.remove(i);
                        allcontacts.add(i, temp);
                    } else if (temp.getLastName() == null) {
                        nameList.add(temp.getFirstName());
                        temp.setLastName("");
                        allcontacts.remove(i);
                        allcontacts.add(i, temp);
                    } else if (temp.getFirstName().equals("")  && temp.getLastName().equals("") ){
                        foo.deleteContact(temp);
                    } else {
                        nameList.add(temp.getFirstName() + " " + temp.getLastName());
                    }
                } else {
                    foo.deleteContact(temp);
                }
            }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                nameList);
        lv.setAdapter(arrayAdapter);

        //Set what happens when you click on something in listview
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

        //Set up buttons
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
                Contact a = foo.createContact("","");
                Intent intent = new Intent(getApplicationContext(), EditContact.class);
                Bundle bundle = new Bundle();
                bundle.putLong("contactid",a.getId());
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.buttonImportContact:
                startActivity(new Intent(this, ImportContacts.class));
                break;
            case R.id.buttonConversationList:
                startActivity(new Intent(this, ConversationList.class));
                break;
        }
    }
}
