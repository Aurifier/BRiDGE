package com.caffeinecraft.bridge;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
    ListView lv;

    public static final int QRSCAN_REQUEST_CODE = 1;

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
        lv = (ListView) findViewById(R.id.listView);
        List<ContactMethod> contactMethods = thiscontact.getContactMethods();
        List<String> meansofcontact = new ArrayList<String>();
        for(ContactMethod contactMethod : contactMethods) {
            meansofcontact.add(contactMethod.getValue());
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_single_choice,
                meansofcontact);
        lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
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
        View btnDelete = findViewById(R.id.buttonDelete);
        btnDelete.setOnClickListener(this);
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
                int selectedItemPosition = lv.getCheckedItemPosition();
                Log.d("EditContact", "Selected: " + selectedItemPosition);
                if(selectedItemPosition>=0){
                    ContactMethod preference = thiscontact.getContactMethods().get(selectedItemPosition);
                    Log.d("EditContact", preference.getValue());
                    thiscontact.setPreferredContactMethod(preference);
                }
                foo.updateContact(thiscontact);
                this.finish();
                break;
            case R.id.buttonDelete:
                foo.deleteContact(thiscontact);
                this.finish();
                break;
            case R.id.buttonImportQRCode:
                startActivityForResult(new Intent(getApplicationContext(), QRScanner.class), QRSCAN_REQUEST_CODE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            String resultValue = data.getStringExtra("result");
            Log.d("EditContact", "result from scan: " + resultValue);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}

