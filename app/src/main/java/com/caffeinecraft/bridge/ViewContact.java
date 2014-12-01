package com.caffeinecraft.bridge;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.caffeinecraft.bridge.dao.ContactsDataSource;
import com.caffeinecraft.bridge.model.Contact;
import com.caffeinecraft.bridge.model.ContactMethod;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewContact extends Activity implements View.OnClickListener{

    private Contact thiscontact;
    ImageView qrCode;

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

        //Initialize Image View for qr code display
        qrCode = (ImageView)findViewById(R.id.imageView);

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
                try {
                    generateQRCode(thiscontact.toString(), qrCode);
                } catch (WriterException e) {
                    e.printStackTrace();
                }
                break;
        }
    }


    private void generateQRCode(String data, ImageView img)throws WriterException {
        com.google.zxing.Writer writer = new QRCodeWriter();
        String finaldata = Uri.encode(data, "utf-8");

        BitMatrix bm = writer.encode(finaldata, BarcodeFormat.QR_CODE,150, 150);
        Bitmap ImageBitmap = Bitmap.createBitmap(150, 150, Bitmap.Config.ARGB_8888);

        for (int i = 0; i < 150; i++) {//width
            for (int j = 0; j < 150; j++) {//height
                ImageBitmap.setPixel(i, j, bm.get(i, j) ? Color.BLACK: Color.WHITE);
            }
        }

        if (ImageBitmap != null) {
            img.setImageBitmap(ImageBitmap);
        }

        loadPhoto(img);
    }

    private void loadPhoto(ImageView imageView) {

        ImageView tempImageView = imageView;


        AlertDialog.Builder imageDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);

        View layout = inflater.inflate(R.layout.custom_fullimage_dialog,
                (ViewGroup) findViewById(R.id.layout_root));
        ImageView image = (ImageView) layout.findViewById(R.id.fullimage);
        image.setImageDrawable(tempImageView.getDrawable());
        imageDialog.setView(layout);
        imageDialog.setPositiveButton("OK", new DialogInterface.OnClickListener(){

            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }

        });


        imageDialog.create();
        imageDialog.show();
    }


}
