package com.caffeinecraft.bridge;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.caffeinecraft.bridge.dao.ContactsDataSource;
import com.caffeinecraft.bridge.model.Contact;

import java.util.List;

public class MergeContact extends Activity implements View.OnClickListener{

    private Contact thiscontact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merge_contact);

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

        //TODO: Make listView of Contacts

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
                //startActivity(new Intent(this, ConversationList.class));
                break;
            case R.id.buttonContactList:
                startActivity(new Intent(this, ContactList.class));
                break;
            case R.id.buttonMerge:
                //TODO: Merge Contact
                break;
        }
    }
}
