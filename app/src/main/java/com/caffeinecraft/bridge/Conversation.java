package com.caffeinecraft.bridge;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by ronitkumar on 11/10/14.
 */
public class Conversation extends Activity{

    private TextView tv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conversation);

        String id = getIntent().getStringExtra("id");
        String[] projection = {"_id", "address", "date", "body"};
        String selection = "_id = ?";
        String[] selectionArgs = {id};
    }

}