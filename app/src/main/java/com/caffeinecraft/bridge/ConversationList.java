package com.caffeinecraft.bridge;

import android.app.ActionBar;
import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleCursorAdapter;

/**
 * Created by ronitkumar on 11/10/14.
 */
public class ConversationList extends ListActivity{
    String[] contactName = new String[] { "Contact One", "Contact Two", "Contact Three",
            "Contact Four", "Contact Five", "Contact Six", "Contact Seven", "Contact Eight",
            "Contact Nine", "Contact Ten" };

    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        // use your custom layout
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.conversation_view, R.id.firstLine, contactName);
        setListAdapter(adapter);
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Intent intent = new Intent(this, Conversation.class);
        intent.putExtra("id", String.valueOf(id));
        intent.putExtra("Contact", contactName[position]);
        startActivity(intent);
    }
}