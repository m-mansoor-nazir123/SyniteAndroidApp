package com.example.firebasechatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.Toast;

import com.evolve.backdroplibrary.BackdropContainer;
import com.example.firebasechatapp.Adapter.RecyclerViewAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

import ru.dzgeorgy.backdrop.BackdropLayout;

public class BackDrop extends AppCompatActivity {
    BackdropContainer backdropContainer;
    Toolbar toolbar;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    private ArrayList<String> mNames = new ArrayList<String>();
    private ArrayList<String> mImageUrls = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_back_drop);

        backdropContainer = findViewById(R.id.backdropcontainer);
        toolbar=findViewById(R.id.toolbar);
        backdropContainer.attachToolbar(toolbar)
                .dropInterpolator(new LinearInterpolator())
                .dropHeight(800)
                .build();

        initImageBitmaps();






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
}
