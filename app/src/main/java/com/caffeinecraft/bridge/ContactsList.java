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
import android.widget.Button;
import com.caffeinecraft.bridge.dao.ContactsDataSource;

public class ContactsList extends Activity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contacts_list);

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
                startActivity(new Intent(this, TestActivity.class));
                break;
            case R.id.buttonImportContact:
                startActivity(new Intent(this, ImportContacts.class));
                break;
            case R.id.buttonConversationList:
                startActivity(new Intent(this, TestActivity.class));
                break;
            case R.id.buttonContactList:
                //You're already in ContactList, silly
                break;
        }
    }
}
