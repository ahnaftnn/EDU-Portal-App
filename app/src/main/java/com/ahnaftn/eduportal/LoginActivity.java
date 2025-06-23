package com.ahnaftn.eduportal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    EditText loginUsername, loginPasword;
    Button loginButton;
    TextView signupText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginUsername= findViewById(R.id.username_edit);
        loginPasword= findViewById(R.id.password_edit_text);
        loginButton = findViewById(R.id.login_btn);
        signupText = findViewById(R.id.create_account_btn);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validateUsername() | !validatePassword()){

                }else {
                    checkUser();
                }
            }
        });
        signupText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });
    }

    public boolean validateUsername(){
        String val = loginUsername.getText().toString();

        if(val.isEmpty()){
            loginUsername.setError("");
            return false;
        }else{
            loginUsername.setError(null);
            return true;
        }
    }

    public boolean validatePassword(){
        String val = loginPasword.getText().toString();

        if(val.isEmpty()){
            loginPasword.setError("");
            return false;
        }else{
            loginPasword.setError(null);
            return true;
        }
    }

    public void checkUser(){
        String userUsername = loginUsername.getText().toString().trim();
        String userPassword = loginPasword.getText().toString().trim();

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("users");
        Query checkUserDatabase = reference.orderByChild("username").equalTo(userUsername);

        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                   loginUsername.setError(null);
                   String PasswordfromDB = snapshot.child(userUsername).child("password").getValue(String.class);

                   if(PasswordfromDB.equals(userPassword)){
                       loginUsername.setError(null);

                       String sidfromDB = snapshot.child(userUsername).child("sid").getValue(String.class);

                       Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                       intent.putExtra("sid",sidfromDB);
                       startActivity(intent);
                       finish();


                   }else {
                       loginPasword.setError("Invalid Password");
                       loginPasword.requestFocus();
                   }
                }else {
                    loginUsername.setError("User does not Exist");
                    loginUsername.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}