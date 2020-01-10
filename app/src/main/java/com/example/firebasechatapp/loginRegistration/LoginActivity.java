package com.example.firebasechatapp.loginRegistration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.firebasechatapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {
    EditText email,password;
    TextView notHaveAcount;
    Button loginButton;
    private FirebaseAuth mAuth;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

       ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.hide();


        mAuth = FirebaseAuth.getInstance();


        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();
                        Log.d("Token Api",token);
                    }

                        // Log and toast}
                });

        //ActionBar actionBar=getSupportActionBar();
        //actionBar.setTitle("Login");

        //actionBar.setDisplayHomeAsUpEnabled(true);
        //actionBar.setDisplayShowHomeEnabled(true);


        loginButton=findViewById(R.id.Loginbutton);
        notHaveAcount=findViewById(R.id.notalreadyHaveAccount);
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email_=email.getText().toString().trim();
                String password_=password.getText().toString().trim();

                if(email_.isEmpty()){
                    email.setError("Email is required");
                    email.requestFocus();
                    return;
                }
                else {
                    if (!Patterns.EMAIL_ADDRESS.matcher(email_).matches()) {

                        email.setError("Enter a valid email");
                        email.requestFocus();
                        return;
                    } else {
                        if (password_.isEmpty()) {
                            password.setError("password required");
                            password.requestFocus();
                            return;
                        } else {
                            if (password_.length() < 6) {
                                password.setError("password should be atleast 6 character long");
                                password.requestFocus();
                                return;
                            }
                            else{
                                loginruser(email_,password_);
                            }
                        }
                    }
                }

            }
        });

        notHaveAcount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LoginActivity.this,"Not Available now",Toast.LENGTH_SHORT).show();
               /* startActivity(new Intent(LoginActivity.this, RegisterActivity.class));*/
            }
        });

        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("logging user");

    }

    private void loginruser(String email_, String password_) {
progressDialog.show();
        mAuth.signInWithEmailAndPassword(email_, password_)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            progressDialog.dismiss();
                            FirebaseUser user = mAuth.getCurrentUser();
                            String email=user.getEmail();
                            String uid=user.getUid();


                            HashMap<Object,String> hashMap=new HashMap<>();
                            hashMap.put("email",email);
                            hashMap.put("uid",uid);

                            FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
                            DatabaseReference databaseReference=firebaseDatabase.getReference("users");
                            databaseReference.child(uid).setValue(hashMap);


                            startActivity(new Intent(LoginActivity.this,DashboardActivity.class));

                        } else {

                            progressDialog.dismiss();
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(LoginActivity.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
