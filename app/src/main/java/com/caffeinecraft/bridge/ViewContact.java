package com.caffeinecraft.bridge;


import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;

public class ViewContact extends Activity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.contacts_list);

        Bundle bundle = getIntent().getExtras();
        String stuff = bundle.getString("row number");

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
                //You're already in ContactList, silly
                break;
        }
    }
}
