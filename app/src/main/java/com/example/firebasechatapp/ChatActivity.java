package com.example.firebasechatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.firebasechatapp.Notification.APIService;
import com.example.firebasechatapp.Notification.Client;
import com.example.firebasechatapp.Notification.Data;
import com.example.firebasechatapp.Notification.Response;
import com.example.firebasechatapp.Notification.Sender;
import com.example.firebasechatapp.Notification.Token;
import com.google.android.gms.common.api.Api;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;

public class ChatActivity extends AppCompatActivity {


    EditText messagetext;
    ImageButton sendbtn;
    String hisUid;

    Boolean notify=false;
    private APIService APIService;
    private String myUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        messagetext = findViewById(R.id.messageEt);
        sendbtn = findViewById(R.id.sendbtn);


        APIService= Client.getRetrofit("https://fcm.googleapis.com/").create(APIService.class);

        Intent intent=getIntent();
         hisUid = intent.getStringExtra("hisUid");

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseDatabase firebaseDatabase= FirebaseDatabase.getInstance();
        DatabaseReference userDbref=firebaseDatabase.getReference("Users");


        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notify=true;
                String message=messagetext.getText().toString().trim();
                sendMessage(message);
            }
        });

    }

    private void sendMessage(final String message) {

        String msg=message;
        DatabaseReference Database=FirebaseDatabase.getInstance().getReference("Users").child(myUid);
        Database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ModelUsers users=dataSnapshot.getValue(ModelUsers.class);
                if(notify)
                {
                    sendNotification(hisUid, users.email,message);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void sendNotification(final String hisUid, final String email, final String message) {
        DatabaseReference alltToken=FirebaseDatabase.getInstance().getReference("Tokens");
        Query query=alltToken.orderByKey().equalTo(hisUid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot ds:dataSnapshot.getChildren()){


                    Token token=ds.getValue(Token.class);
                    Data data=new Data(myUid,email,message,"new Message",hisUid,R.drawable.chaticon);

                    Sender sender=new Sender(data,token.getToken());
                    APIService.sendNotification(sender).enqueue(new Callback<Response>() {
                        @Override
                        public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                            Toast.makeText(ChatActivity.this,""+response.message(),Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Call<Response> call, Throwable t) {

                        }
                    });
                }




            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
