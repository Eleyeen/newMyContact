package com.example.mycontacts.ui.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mycontacts.R;
import com.example.mycontacts.adapters.AllContactAdapter;
import com.example.mycontacts.dataBase.ContactCurd;
import com.example.mycontacts.dataModel.GroupDataModel;
import com.example.mycontacts.ui.fragments.GroupFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GroupMessageActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    private boolean validGroup = false;
    private String msgGroup;
    @BindView(R.id.sentMessageGroup)
    TextView sendMessageGroup;
    @BindView(R.id.edit_TextGroup)
    EditText msgTextGroup;
    ContactCurd contactCurd;
    List<GroupDataModel> groupDataModels = new ArrayList<>();
    private String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_message);
        ButterKnife.bind(this);
        contactCurd = new ContactCurd(this);

        sharedPreferences = getSharedPreferences("abcd", Context.MODE_PRIVATE);
        String groupName = sharedPreferences.getString("ide", "");

        groupDataModels = contactCurd.getGroupContact(groupName, this);



        sendMessageGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isValidGroup();
                phone = groupDataModels.get(0).getGroupContactNum();

                sentdSMSGroup(phone, msgGroup);
            }
        });
        customActionBar();
    }

    private boolean isValidGroup() {
        validGroup = true;
        msgGroup = msgTextGroup.getText().toString();
        if (msgGroup.isEmpty()) {
            msgTextGroup.setError("ERROR");
        } else {
            msgTextGroup.setError(null);
        }
        return validGroup;
    }

    private void sentdSMSGroup(String phone, String msg) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {


                try {
                    SmsManager smsMgrVar = SmsManager.getDefault();
                    smsMgrVar.sendTextMessage(phone, null, msg, null, null);
                    Toast.makeText(getApplicationContext(), "Message Sent",
                            Toast.LENGTH_LONG).show();

                    Toast.makeText(this, phone, Toast.LENGTH_SHORT).show();

                } catch (Exception ErrVar) {
                    Toast.makeText(getApplicationContext(), "ERROR",
                            Toast.LENGTH_LONG).show();
                    ErrVar.printStackTrace();
                }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.SEND_SMS}, 10);
            }
        }
    }

    private void sendSMS(String phoneNumber, String message)
    {
        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";

        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0,
                new Intent(SENT), 0);

        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
                new Intent(DELIVERED), 0);

        //---when the SMS has been sent---
        registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS sent",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(getBaseContext(), "Generic failure",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(getBaseContext(), "No service",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(getBaseContext(), "Null PDU",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(getBaseContext(), "Radio off",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(SENT));


        registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(getBaseContext(), "SMS not delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(DELIVERED));

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
    }
    public void customActionBar() {
        sharedPreferences = getSharedPreferences("abcd", Context.MODE_PRIVATE);
        String groupName = sharedPreferences.getString("ide", "");

        ActionBar mActionBar = ((AppCompatActivity) GroupMessageActivity.this).getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(true);
        mActionBar.setDisplayHomeAsUpEnabled(false);
        mActionBar.setTitle(groupName);
        mActionBar.setElevation(0);

        mActionBar.show();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.deleteGroupMenu:

                sharedPreferences = getSharedPreferences("abcd", Context.MODE_PRIVATE);
                String groupName = sharedPreferences.getString("ide", "");
                contactCurd.deleteGroup(groupName);
                finish();
                break;
            case R.id.contactsMenu:
                Intent i = new Intent(GroupMessageActivity.this, GroupContactActivity.class);
                startActivity(i);
                break;
        }
        return false;
    }

}
