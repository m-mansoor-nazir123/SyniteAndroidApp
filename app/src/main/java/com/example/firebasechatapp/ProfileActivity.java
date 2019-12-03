package com.example.firebasechatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.firebasechatapp.Notification.Token;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {
    String Uid;
    TextView profiletv;
    FirebaseAuth firebaseAuth;

    RecyclerView recyclerView;
    String mUid;

    AdapterUser adapterUser;
    List<ModelUsers> UsersList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        firebaseAuth=FirebaseAuth.getInstance();

        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("Profile");


        profiletv=findViewById(R.id.email);
        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplication()));

        UsersList=new ArrayList<>();
        getAllUsers();



       // updateToken(FirebaseInstanceId.getInstance().getToken());

    }

    private void getAllUsers() {

        final FirebaseUser firebaseUser= firebaseAuth.getCurrentUser();
        final DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               UsersList.clear();
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    ModelUsers modelUsers=ds.getValue(ModelUsers.class);
                    if(!modelUsers.getUid().equals(firebaseUser.getUid())){
                        UsersList.add(modelUsers);
                    }
                    adapterUser=new AdapterUser(getApplicationContext(),UsersList);

                    recyclerView.setAdapter(adapterUser);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void checkUserStatus(){
        FirebaseUser user=firebaseAuth.getCurrentUser();
        if(user!=null){

            profiletv.setText(user.getEmail());
            mUid=user.getUid();
            SharedPreferences sp=getSharedPreferences("SP_User",MODE_PRIVATE);
            SharedPreferences.Editor editor=sp.edit();
            editor.putString("Current_UserID",mUid);
            editor.apply();

        }
        else
        {
            startActivity(new Intent(ProfileActivity.this,MainActivity.class));
            finish();
        }
    }
    public void updateToken(String token){
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Tokens");
        Token mtoken=new Token(token);
        ref.child(mUid).setValue(mtoken);
    }

    @Override
    protected void onStart() {
        checkUserStatus();
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id=item.getItemId();
        if(id==R.id.action_logout){
            firebaseAuth.signOut();
            checkUserStatus();
        }
        return super.onOptionsItemSelected(item);
    }
}
