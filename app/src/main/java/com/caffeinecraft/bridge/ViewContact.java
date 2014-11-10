package com.caffeinecraft.bridge;


import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.caffeinecraft.bridge.dao.ContactsDataSource;
import com.caffeinecraft.bridge.model.Contact;

import java.util.ArrayList;
import java.util.List;

public class ViewContact extends Activity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_contact);

        ContactsDataSource foo = new ContactsDataSource(this);
        foo.open();

        Bundle bundle = getIntent().getExtras();
        Long contactid = bundle.getLong("contactid");
        Log.d("BRiDGE", "ACTUAL CONTACTID IS " + contactid);

        List<Contact> allcontacts = foo.getAllContacts();
        List<Contact> thiscontact = new ArrayList<Contact>();

        Log.d("BRiDGE", "POINT ONE");

        for(int i = 0;i<allcontacts.size();i++)
        {
            if(allcontacts.get(i).getId()==contactid) thiscontact.add(allcontacts.get(i));
        }

        TextView txtName=(TextView)findViewById(R.id.textName);

        Log.d("BRiDGE", "POINT TWO");

        Log.d("BRiDGE", "ACTUAL CONTACTID IS " + contactid);
        for(int i = 0; i< allcontacts.size(); i++)
        {
            Log.d("BRiDGE", "CONTACT " + i + ", NAMED " + allcontacts.get(i).getLastName() + "'S ID IS " + allcontacts.get(i).getId());
        }

        txtName.setText(thiscontact.get(0).getFirstName()+" "+thiscontact.get(0).getLastName());

        Log.d("BRiDGE", "POINT THREE");

        View btnConversationList = findViewById(R.id.buttonConversationList);
        btnConversationList.setOnClickListener(this);
        View btnContactList = findViewById(R.id.buttonContactList);
        btnContactList.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.buttonConversationList:
                startActivity(new Intent(this, ImportContacts.class));
                break;
            case R.id.buttonContactList:
                startActivity(new Intent(this, ContactList.class));
                break;
        }
    }
}
