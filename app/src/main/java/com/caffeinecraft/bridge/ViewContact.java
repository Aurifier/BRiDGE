package com.caffeinecraft.bridge;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.caffeinecraft.bridge.dao.ContactsDataSource;
import com.caffeinecraft.bridge.model.Contact;
import com.caffeinecraft.bridge.model.ContactMethod;
import java.util.ArrayList;
import java.util.List;

public class ViewContact extends Activity implements View.OnClickListener{

    private Contact thiscontact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Generic initialize function calls
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_contact);
        ContactsDataSource foo = new ContactsDataSource(this);
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

        //Set Text View
        TextView txtName=(TextView)findViewById(R.id.textName);
        txtName.setText(thiscontact.toString());

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

        //Set Button Onclicklisteners
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
                startActivity(new Intent(this, ConversationList.class));
                break;
            case R.id.buttonContactList:
                startActivity(new Intent(this, ContactList.class));
                break;
            case R.id.buttonEditContact:
                Intent intent = new Intent(getApplicationContext(), EditContact.class);
                Bundle bundle = new Bundle();
                bundle.putLong("contactid",thiscontact.getId());
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.buttonNewMessage:
                Intent intent1 = new Intent(this, ConversationScreen.class);
                intent1.putExtra("contact_id", thiscontact.getId());
                startActivity(intent1);
                break;
            case R.id.buttonExportQRCode:
                //startActivity(new Intent(this, ExportQRCode.class));
                break;
        }
    }
}
