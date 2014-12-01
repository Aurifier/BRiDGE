package com.caffeinecraft.bridge;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.caffeinecraft.bridge.dao.ContactsDataSource;
import com.caffeinecraft.bridge.model.Contact;
import com.caffeinecraft.bridge.model.ContactMethod;

import java.util.ArrayList;
import java.util.List;

public class MergeContact extends Activity implements View.OnClickListener{

    private Contact thiscontact;
    private List<Contact> allcontacts;
    private ContactsDataSource foo;
    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Generic initialize function calls
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merge_contact);
        foo = new ContactsDataSource(this);
        foo.open();

        //Set thiscontact to the correct contact given in the savedInstanceState
        Bundle bundle = getIntent().getExtras();
        Long contactid = bundle.getLong("contactid");
        allcontacts = foo.getAllContacts();
        for(int i = 0;i<allcontacts.size();i++)
        {
            if(allcontacts.get(i).getId()==contactid)
            {thiscontact = allcontacts.get(i);}
        }

        //Make sure this contact is not in the list of mergeable contacts
        allcontacts.remove(thiscontact);

        //Set Text View
        TextView txtName=(TextView)findViewById(R.id.textName);
        txtName.setText(thiscontact.toString());

        //Set up Listview
        List<String> nameList = new ArrayList<String>();
        for(int i = 0;i<allcontacts.size();i++)
        {
            Contact temp = allcontacts.get(i);
            nameList.add(temp.toString());
        }

        lv = (ListView) findViewById(R.id.listView);
        lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_multiple_choice,
                nameList);
        lv.setAdapter(arrayAdapter);

        //Set what the buttons do
        View btnConversationList = findViewById(R.id.buttonConversationList);
        btnConversationList.setOnClickListener(this);
        View btnContactList = findViewById(R.id.buttonContactList);
        btnContactList.setOnClickListener(this);
        View btnMerge = findViewById(R.id.buttonMerge);
        btnMerge.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.buttonConversationList:
                startActivity(new Intent(this, ConversationList.class));
                break;
            case R.id.buttonContactList:
                startActivity(new Intent(this, ContactList.class));
                break;
            case R.id.buttonMerge:
                List<ContactMethod> meansofcontact = thiscontact.getContactMethods();
                int len = lv.getCount();
                SparseBooleanArray checked = lv.getCheckedItemPositions();
                for (int i = 0; i < len; i++)
                    if (checked.get(i)) {
                        Log.v("bridge", "" + i);
                    }
                for (int i = 0; i < len; i++)
                    if (checked.get(i)) {
                        List<ContactMethod> checkedContactsContactMethods = allcontacts.get(i).getContactMethods();
                        for(ContactMethod contactMethod : checkedContactsContactMethods)
                        {
                            thiscontact.addContactMethod(contactMethod);
                        }
                        foo.deleteContact(allcontacts.get(i));
                    }
                foo.updateContact(thiscontact);
                this.finish();
                break;
        }
    }
}
