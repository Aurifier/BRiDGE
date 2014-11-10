package com.caffeinecraft.bridge;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.caffeinecraft.bridge.dao.ContactsDataSource;
import com.caffeinecraft.bridge.model.Contact;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EditContact extends Activity implements View.OnClickListener{

    private Contact thiscontact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);

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

        TextView editTextFirstName=(TextView)findViewById(R.id.editTextFirstName);
        editTextFirstName.setText(thiscontact.getFirstName());
        TextView editTextLastName=(TextView)findViewById(R.id.editTextLastName);
        editTextLastName.setText(thiscontact.getLastName());

        //TODO: Make listView of means of contact

        View btnConversationList = findViewById(R.id.buttonConversationList);
        btnConversationList.setOnClickListener(this);
        View btnContactList = findViewById(R.id.buttonContactList);
        btnContactList.setOnClickListener(this);
        View btnMergeContact = findViewById(R.id.buttonMergeContact);
        btnMergeContact.setOnClickListener(this);
        View btnImportQRCode = findViewById(R.id.buttonImportQRCode);
        btnImportQRCode.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.buttonConversationList:
                //startActivity(new Intent(this, ConversationList.class));
                break;
            case R.id.buttonContactList:
                startActivity(new Intent(this, ContactList.class));
                break;
            case R.id.buttonMergeContact:
                Intent intent = new Intent(getApplicationContext(), MergeContact.class);
                Bundle bundle = new Bundle();
                bundle.putLong("contactid",thiscontact.getId());
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.buttonImportQRCode:
                //startActivity(new Intent(this, ImportQRCode.class));
                break;
        }
    }
}

