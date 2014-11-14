package com.caffeinecraft.bridge;



import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.CursorLoader;

import android.content.Intent;

import android.content.Loader;

import android.database.Cursor;

import android.database.CursorWrapper;

import android.provider.ContactsContract;

import android.os.Bundle;

import android.util.SparseBooleanArray;

import android.view.Menu;

import android.view.MenuItem;

import android.view.View;

import android.view.ViewGroup;

import android.widget.Button;

import android.widget.ProgressBar;

import android.widget.SimpleCursorAdapter;



import com.caffeinecraft.bridge.dao.ContactsDataSource;

import com.caffeinecraft.bridge.model.Contact;

import com.caffeinecraft.bridge.model.ContactMethod;



public class ImportPhoneContacts extends ListActivity

        implements LoaderManager.LoaderCallbacks<Cursor> {



    SimpleCursorAdapter mAdaptor;

    private ContactsDataSource dataSource;



    static final String[] PROJECTION = new String[] {

        ContactsContract.Contacts._ID,

        ContactsContract.Contacts.LOOKUP_KEY,

        ContactsContract.Contacts.DISPLAY_NAME

    };



    static final String SELECTION =

        ContactsContract.Contacts.IN_VISIBLE_GROUP + " = 1";



    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_import_phone_contacts);

        addListenerOnButton();

        dataSource = new ContactsDataSource(this);

        dataSource.open();



        //Create a progress bar

        ProgressBar progressBar = new ProgressBar(this);

        progressBar.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,

            ViewGroup.LayoutParams.WRAP_CONTENT));

        progressBar.setIndeterminate(true);

        getListView().setEmptyView(progressBar);



        ViewGroup root = (ViewGroup) findViewById(android.R.id.content);

        root.addView(progressBar);



        //Specify columns for cursor adaptor

        String[] fromColumns = {ContactsContract.Contacts.DISPLAY_NAME};

        int[] toViews = {android.R.id.text1}; // The TextView in simple_list_item_1



        //Create the adaptor

        mAdaptor = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_multiple_choice, null,

            fromColumns, toViews, 0);

        setListAdapter(mAdaptor);



        getLoaderManager().initLoader(0, null, this);

    }



    @Override

    protected void onDestroy() {

        dataSource.close();

        super.onDestroy();

    }



    private void addListenerOnButton() {

        Button importButton = (Button)findViewById(R.id.importGo);

        importButton.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View view) {

                final SparseBooleanArray checkedItems = getListView().getCheckedItemPositions();



                if(checkedItems == null) {

                    return;

                }



                final int checkedElementsCount = checkedItems.size();

                for(int i = 0; i < checkedElementsCount; i++) {

                    final int position = checkedItems.keyAt(i);

                    final boolean isChecked = checkedItems.valueAt(i);

                    if(isChecked) {

                        final CursorWrapper blah = (CursorWrapper) getListAdapter().getItem(position);

                        importContactByLookupKey(blah.getString(1));

                    }

                }

            }

        });

    }



    @Override

    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.import_phone_contacts, menu);

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

    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        return new CursorLoader(this, ContactsContract.Contacts.CONTENT_URI, PROJECTION, SELECTION,

            null, null);

    }



    @Override

    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {

        mAdaptor.swapCursor(cursor);

    }



    @Override

    public void onLoaderReset(Loader<Cursor> cursorLoader) {

        mAdaptor.swapCursor(null);

    }



    private void importContactByLookupKey(String key) {

        String[] projection = new String[] {

            ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME,

            ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME

        };

        String selection =

            ContactsContract.Data.MIMETYPE + " = ? AND "

            + ContactsContract.Data.LOOKUP_KEY + " = ?";



        Cursor contactCursor = getContentResolver().query(

            ContactsContract.Data.CONTENT_URI, projection, selection, new String[]{

                ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE,

                key

            },

            ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME

        );



        contactCursor.moveToFirst();

        if(!contactCursor.isAfterLast()) {

            //Create the contact

            Contact contact = dataSource.createContact(

                contactCursor.getString(0), contactCursor.getString(1)

            );



            projection = new String[] {

                ContactsContract.CommonDataKinds.Email.ADDRESS

            };



            Cursor emailCursor = getContentResolver().query(

                ContactsContract.Data.CONTENT_URI, projection, selection, new String[] {

                    ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE,

                    key

                },

                ContactsContract.CommonDataKinds.Email.ADDRESS

            );

            emailCursor.moveToFirst();

            while (!emailCursor.isAfterLast()) {

                //Add the email to the contact

                ContactMethod email = new ContactMethod();

                email.setType(ContactMethod.Type.EMAIL);

                email.setValue(emailCursor.getString(0));

                contact.addContactMethod(email);



                //Save the contact

                dataSource.updateContact(contact);



                //Carry on

                emailCursor.moveToNext();

            }

            emailCursor.close();



            projection = new String[] {

                ContactsContract.CommonDataKinds.Phone.NUMBER

            };

            Cursor smsCursor = getContentResolver().query(

                ContactsContract.Data.CONTENT_URI, projection, selection, new String[] {

                    ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE,

                    key

                },

                ContactsContract.CommonDataKinds.Phone.NUMBER

            );

            smsCursor.moveToFirst();

            while(!smsCursor.isAfterLast()) {

                //Add the phone number to the contact

                ContactMethod phone = new ContactMethod();

                phone.setType(ContactMethod.Type.SMS);

                phone.setValue(smsCursor.getString(0));

                contact.addContactMethod(phone);



                //Save the contact

                dataSource.updateContact(contact);



                //Carry on

                smsCursor.moveToNext();

            }

            smsCursor.close();



            //Go back to the contact list

            Intent intent = new Intent(this, ContactList.class);

            startActivity(intent);

        }

    }

}
