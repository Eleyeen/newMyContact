package com.example.mycontacts.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mycontacts.R;
import com.example.mycontacts.adapters.ContactAdapter;
import com.example.mycontacts.dataBase.ContactCurd;
import com.example.mycontacts.ui.fragments.AddContactFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CustomNewMemberActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.addNewMemberBtn)
    TextView addToContact;
    @BindView(R.id.newMemberName)
    EditText newMemberName;
    @BindView(R.id.newMemberNumber)
    EditText newMemberNum;
    ContactCurd contactCurd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_new_member);
        initUI();
    }

    private void initUI() {
        ButterKnife.bind(this);
        contactCurd = new ContactCurd(this);
        addToContact.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.addNewMemberBtn:

                SharedPreferences sharedPreferences = getSharedPreferences("abcd", Context.MODE_PRIVATE);
                String strGroupName = sharedPreferences.getString("ide", "");



                if (strGroupName.equals("") ) {
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                } else  {


                   String strContactName = newMemberName.getText().toString();
                   String strContactNumber = newMemberNum.getText().toString();

                   if(strContactName.equals("")&&strContactNumber.equals("")){
                       Toast.makeText(this, "Enter The Number And Name", Toast.LENGTH_SHORT).show();
                   }else if(strContactName.equals("")){
                       Toast.makeText(this, "Enter The Name", Toast.LENGTH_SHORT).show();
                   }else if(strContactNumber.equals("")){
                       Toast.makeText(this, "Enter The Number", Toast.LENGTH_SHORT).show();
                   }else {
                       contactCurd.insertContact(this, strGroupName, strContactName, strContactNumber);
                       Log.d("insertContact", strContactName);
                       Log.d("insertContact", strGroupName);
                       Intent intent = new Intent(CustomNewMemberActivity.this, GroupContactActivity.class);
                       startActivity(intent);
                       Toast.makeText(this, strContactName, Toast.LENGTH_SHORT).show();
                   }

                }
                break;
        }


    }
}
