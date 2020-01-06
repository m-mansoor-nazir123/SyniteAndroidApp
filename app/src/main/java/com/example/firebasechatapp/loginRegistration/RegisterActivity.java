package com.example.firebasechatapp.loginRegistration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    EditText email;
    EditText password;
    Button registerbutton;
    ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    TextView haveAccount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("Create Account");

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        registerbutton=findViewById(R.id.Loginbutton);
        haveAccount=findViewById(R.id.notalreadyHaveAccount);

        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Registering User");

        registerbutton.setOnClickListener(new View.OnClickListener() {
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
                                registeruser(email_,password_);
                            }
                        }
                    }
                }
            }
        });

        haveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));

            }
        });
    }

    private void registeruser(String email_, String password_) {

        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(email_, password_)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            FirebaseUser user=mAuth.getCurrentUser();
                            String email=user.getEmail();
                            String uid=user.getUid();


                            HashMap<Object,String> hashMap=new HashMap<>();
                            hashMap.put("email",email);
                            hashMap.put("uid",uid);

                            FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
                            DatabaseReference databaseReference=firebaseDatabase.getReference("users");
                            databaseReference.child(uid).setValue(hashMap);
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser userr = mAuth.getCurrentUser();
                            Toast.makeText(RegisterActivity.this,"Registered...\n"+userr.getEmail(),Toast.LENGTH_SHORT).show();
                         /*   startActivity(new Intent(RegisterActivity.this,ProfileActivity.class));*/
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            progressDialog.dismiss();
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }


                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(RegisterActivity.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
