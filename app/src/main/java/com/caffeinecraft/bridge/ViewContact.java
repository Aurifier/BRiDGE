package com.caffeinecraft.bridge;


import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.caffeinecraft.bridge.dao.ContactsDataSource;
import com.caffeinecraft.bridge.model.Contact;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ViewContact extends Activity implements View.OnClickListener{

    private Contact thiscontact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_contact);

        ContactsDataSource foo = new ContactsDataSource(this);
        foo.open();

        Bundle bundle = getIntent().getExtras();
        Long contactid = bundle.getLong("contactid");

        List<Contact> allcontacts = foo.getAllContacts();

        for(int i = 0;i<allcontacts.size();i++)
        {
            if(allcontacts.get(i).getId()==contactid)
            {thiscontact = allcontacts.get(i);}
        }

        TextView txtName=(TextView)findViewById(R.id.textName);

        txtName.setText(thiscontact.toString());

        ListView lv = (ListView) findViewById(R.id.listView);
        String[] emails = thiscontact.getEmails();
        ArrayList<String> meansofcontact = new ArrayList<String>(Arrays.asList(thiscontact.getEmails()));
        meansofcontact.add("HELLO");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                meansofcontact);
        //THIS LINE CAUSES ERRORS AND I DON'T KNOW WHY!
        //lv.setAdapter(arrayAdapter);

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
