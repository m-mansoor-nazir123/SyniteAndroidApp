package com.example.firebasechatapp.loginRegistration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.firebasechatapp.Adapter.RecyclerViewAdapter;
import com.example.firebasechatapp.AddActivity;
import com.example.firebasechatapp.AddData.AddData;
import com.example.firebasechatapp.MainActivity;
import com.example.firebasechatapp.R;
import com.example.firebasechatapp.ThemeActivity;
import com.example.firebasechatapp.excel_file.ExcelWriter;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class DashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    Button addNew;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    NavigationView navigationView;
    DrawerLayout mDrawerLayout;
   // RecyclerView recyclerView;



    private ArrayList<String> mNames = new ArrayList<String>();
    private ArrayList<String> mImageUrls = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        addNew = findViewById(R.id.addbutton);
       // recyclerView = findViewById(R.id.recyclerv_view);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);


        setNavigationViewListener();

        initImageBitmaps();


        addNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashboardActivity.this, AddActivity.class));

            }
        });

    }

    private void initImageBitmaps(){

       // initRecyclerView();

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("data");

       /* String id=databaseReference.push().getKey();
        DatabaseReference databaseReferenceChild= databaseReference.child(id);
*/
        // DatabaseReference databaseReferenceSubChild=databaseReferenceChild.child("nameData");
        //databaseReference=databaseReference.child(id);

        RecyclerView recyclerView = findViewById(R.id.recyclerv_view);

        final RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, mNames, mImageUrls);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        databaseReference.child("").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    //AddData addData=new AddData();
                    String id=ds.getKey();
                   Map<Object, String> map = (Map<Object, String>) dataSnapshot.child(id).getValue();
                  String string= map.get("nameData");
                  // String user_name =dataSnapshot.child(id).getValue(String.class);

                  // String value= (String) dataSnapshot.getValue();

                   // AddData addData= dataSnapshot.child(id).getValue(AddData.class);

                    mNames.add((string));
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


       // mNames.add("White Sands Desert");
       // mNames.add("Austrailia");
       // mNames.add("Washington");


    }

    private void initRecyclerView(){
       // Log.d(TAG, "initRecyclerView: init recyclerview.");
        RecyclerView recyclerView = findViewById(R.id.recyclerv_view);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, mNames, mImageUrls);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {

            case R.id.GenerateExcel: {

                //Intent intent
                startActivity(new Intent(DashboardActivity.this, ExcelWriter.class));

                break;
            }
        }
        //close navigation drawer
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setNavigationViewListener() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }
}
