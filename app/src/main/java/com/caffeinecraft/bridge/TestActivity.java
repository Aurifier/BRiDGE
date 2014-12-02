package com.caffeinecraft.bridge;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.caffeinecraft.bridge.dao.ContactsDataSource;


public class TestActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        //TODO: Make this an AsyncTask or something because it takes forever to create the database
        ContactsDataSource foo = new ContactsDataSource(this);
        foo.open();
        foo.close();

        Log.d("BRiDGE", "Activity created. (onCreate)");
    }

    @Override
    protected void onStart() {
        super.onStart();  // Always call the superclass method first

        Log.d("BRiDGE", "Activity started. (onStart)");

    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first

        Log.d("BRiDGE", "Activity resumed. (onResume)");
    }

    @Override
    public void onPause() {
        super.onPause();  // Always call the superclass method first

        Log.d("BRiDGE", "Activity paused. (onPause)");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();  // Always call the superclass

        Log.d("BRiDGE", "Activity destroyed. (onDestroy)");
        // Stop method tracing that the activity started during onCreate()
        android.os.Debug.stopMethodTracing();
    }

    @Override
    protected void onRestart() {
        super.onRestart();  // Always call the superclass method first

        Log.d("BRiDGE", "Activity restarted. (onRestart)");
        // Activity being restarted from stopped state
    }

    @Override
    protected void onStop() {
        super.onStop();  // Always call the superclass method first

        Log.d("BRiDGE", "Activity stopped. (onStop)");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.test, menu);
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

    @Override
    public boolean onTouchEvent(MotionEvent evt) {
        if (evt.getAction() == MotionEvent.ACTION_DOWN) {
            startActivity(new Intent(this, ContactList.class));
            finish();
        }
        return false;
    }
}