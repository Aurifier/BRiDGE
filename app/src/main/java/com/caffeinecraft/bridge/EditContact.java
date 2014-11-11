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
import com.caffeinecraft.bridge.model.ContactMethod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EditContact extends Activity implements View.OnClickListener{

    private Contact thiscontact;
    ContactsDataSource foo;
    TextView editTextFirstName;
    TextView editTextLastName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Generic initialize function calls
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);
        foo = new ContactsDataSource(this);
        foo.open();

        //Set thiscontact to the correct contact given in the savedInstanceState
        Bundle bundle = getIntent().getExtras();
        Long contactid = bundle.getLong("contactid");
        List<Contact> allcontacts = foo.getAllContacts();
        for(int i = 0;i<allcontacts.size();i++)
        {
            if(allcontacts.get(i).getId()==contactid)
            {thiscontact = allcontacts.get(i);}
        }

        //Set List View
        ListView lv = (ListView) findViewById(R.id.listView);
        List<ContactMethod> contactMethods = thiscontact.getContactMethods();
        List<String> meansofcontact = new ArrayList<String>();
        for(ContactMethod contactMethod : contactMethods) {
            meansofcontact.add(contactMethod.getValue());
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                meansofcontact);
        lv.setAdapter(arrayAdapter);

        //Set EditTexts
        editTextFirstName=(TextView)findViewById(R.id.editTextFirstName);
        editTextFirstName.setText(thiscontact.getFirstName());
        editTextLastName=(TextView)findViewById(R.id.editTextLastName);
        editTextLastName.setText(thiscontact.getLastName());

        //Set Button OnclickListeners
        View btnConversationList = findViewById(R.id.buttonConversationList);
        btnConversationList.setOnClickListener(this);
        View btnContactList = findViewById(R.id.buttonContactList);
        btnContactList.setOnClickListener(this);
        View btnMergeContact = findViewById(R.id.buttonMergeContact);
        btnMergeContact.setOnClickListener(this);
        View btnImportQRCode = findViewById(R.id.buttonImportQRCode);
        btnImportQRCode.setOnClickListener(this);
        View btnSave = findViewById(R.id.buttonSave);
        btnSave.setOnClickListener(this);
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
            case R.id.buttonMergeContact:
                Intent intent = new Intent(getApplicationContext(), MergeContact.class);
                Bundle bundle = new Bundle();
                bundle.putLong("contactid",thiscontact.getId());
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.buttonSave:
                thiscontact.setFirstName(editTextFirstName.getText().toString());
                thiscontact.setLastName(editTextLastName.getText().toString());
                //TODO: ADD FUNCTIONALITY TO ADD AND REMOVE CONTACTMETHODS
                foo.updateContact(thiscontact);
                this.finish();
                break;
            case R.id.buttonImportQRCode:
                //startActivity(new Intent(this, ImportQRCode.class));
                break;
        }
    }
}

