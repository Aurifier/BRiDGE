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
import com.caffeinecraft.bridge.model.ContactMethod;

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
        //Updated the following to be not-broken with my changes. Clean up as you like. -Drew
        List<ContactMethod> contactMethods = thiscontact.getContactMethods();
        ContactMethod method = new ContactMethod();
        method.setValue("HELLO");
        contactMethods.add(method);
        List<String> meansofcontact = new ArrayList<String>();
        for(ContactMethod contactMethod : contactMethods) {
            meansofcontact.add(contactMethod.getValue());
        }

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
        View btnEditContact = findViewById(R.id.buttonEditContact);
        btnEditContact.setOnClickListener(this);
        View btnNewMessage = findViewById(R.id.buttonNewMessage);
        btnNewMessage.setOnClickListener(this);
        View btnExportQRCode = findViewById(R.id.buttonExportQRCode);
        btnExportQRCode.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.buttonConversationList:
                //startActivity(new Intent(this, ConversationList.class));
                break;
            case R.id.buttonContactList:
                //startActivity(new Intent(this, ContactList.class));
                break;
            case R.id.buttonEditContact:
                //startActivity(new Intent(this, EditContact.class));
                break;
            case R.id.buttonNewMessage:
                //startActivity(new Intent(this, NewMessage.class));
                break;
            case R.id.buttonExportQRCode:
                //startActivity(new Intent(this, ExportQRCode.class));
                break;
        }
    }
}
