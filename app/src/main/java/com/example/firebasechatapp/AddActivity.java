package com.example.firebasechatapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.firebasechatapp.AddData.AddData;
import com.example.firebasechatapp.loginRegistration.DashboardActivity;
import com.example.firebasechatapp.loginRegistration.LoginActivity;
import com.firebase.client.Firebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class AddActivity extends AppCompatActivity {
Button viewTheme;
Button save;
Firebase mref;

DatabaseReference databaseReference;

EditText CompanyNameee;
EditText Url;
EditText Themeselected;
EditText Notes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        //mref=new Firebase("https://fir-chatapp-75cf6.firebaseio.com/");

        databaseReference= FirebaseDatabase.getInstance().getReference("data");


        viewTheme=findViewById(R.id.viewtheme);
        CompanyNameee=findViewById(R.id.CompanyNamee);
        Themeselected=findViewById(R.id.ThemeSelected);
        Notes=findViewById(R.id.webUrl);
        Url=findViewById(R.id.Notes);
        save=findViewById(R.id.Save);

        viewTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(AddActivity.this, ThemeActivity.class));
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addData();
                startActivity(new Intent(AddActivity.this, DashboardActivity.class));

            }
        });
    }


    private void addData() {

        String nameData=CompanyNameee.getText().toString().trim();
        String ThemeData=Themeselected.getText().toString().trim();
        String notesData=Notes.getText().toString().trim();
        String UrlData=Url.getText().toString().trim();


        String id=databaseReference.push().getKey();


       /* AddData addData=new AddData(nameData,ThemeData,notesData,UrlData);
        databaseReference.child(id).setValue(addData);
        Toast.makeText(this,"Data Added",Toast.LENGTH_SHORT).show();
*/

        HashMap<Object,String> hashMap=new HashMap<>();
        hashMap.put("nameData",nameData);
        hashMap.put("ThemeData",ThemeData);
        hashMap.put("notesData",notesData);
        hashMap.put("UrlData",UrlData);
        //hashMap.put("uid",uid);

        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();

        databaseReference.child(id).setValue(hashMap);

                      

    }
}
