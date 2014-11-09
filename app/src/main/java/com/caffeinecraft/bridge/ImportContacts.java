package com.caffeinecraft.bridge;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class ImportContacts extends ActionBarActivity {
    private RadioGroup contactSourceGroup;
    private RadioButton contactSource;
    private Button dasButton;
    public static final String TAG = "ImportContactsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_contacts);
        addListenerOnButton();
    }

    public void addListenerOnButton() {
        contactSourceGroup = (RadioGroup) findViewById(R.id.importContactSource);
        dasButton = (Button) findViewById(R.id.importGo);
        dasButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (contactSourceGroup.getCheckedRadioButtonId()) {
                    case R.id.phone_contacts:
                        //TODO: import phone contacts
                        break;
                    case R.id.facebook_friends:
                        //TODO: import facebook friends
                        break;
                    default:
                        Log.e(TAG, "Somehow a non-option was selected");
                }
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.import_contacts, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
