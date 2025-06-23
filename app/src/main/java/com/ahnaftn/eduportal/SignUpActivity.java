package com.ahnaftn.eduportal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    EditText username_edit,email_edit, password_edit, confirmPassword_edit;
    TextView loginText;
    Button signupButton;
    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        username_edit = findViewById(R.id.username_edit);
        email_edit = findViewById(R.id.email_edit);
        password_edit= findViewById(R.id.password_edit_text);
        confirmPassword_edit = findViewById(R.id.confirm_password_edit_text);
        loginText =findViewById(R.id.login_btn);
        signupButton= findViewById(R.id.create_account_btn);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                database = FirebaseDatabase.getInstance();
                reference = database.getReference("users");

                String username = username_edit.getText().toString().trim();
                String email = email_edit.getText().toString().trim();
                String password = password_edit.getText().toString();
                String cpassword = confirmPassword_edit.getText().toString();
                String sid = email_edit.getText().toString().split("@")[0];;

                if (!password.equals(cpassword)) {
                    Toast.makeText(SignUpActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (username.isEmpty() || email.isEmpty() || password.isEmpty() || cpassword.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                reference.get().addOnCompleteListener(task -> {
                   if(task.isSuccessful()){
                       boolean usernameExists = task.getResult().hasChild(username);
                       boolean emailExists = false;

                       for(DataSnapshot userSnapshot : task.getResult().getChildren()){
                           String existingEmail = userSnapshot.child("email").getValue(String.class);
                           if(email.equals(existingEmail)){
                               emailExists = true;
                               break;
                           }

                       }

                       if(usernameExists){
                           //username already exists
                           Toast.makeText(SignUpActivity.this, "Username already exists", Toast.LENGTH_SHORT).show();
                       }else if(emailExists){
                           Toast.makeText(SignUpActivity.this, "Email already Registered", Toast.LENGTH_SHORT).show();
                       }else {
                           HelperClass helperClass = new HelperClass(username, email, password, cpassword, sid);
                           reference.child(username).setValue(helperClass);


                           Toast.makeText(SignUpActivity.this, "You Have Signed Up Succesfully", Toast.LENGTH_SHORT).show();
                           startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                           finish();
                       }
                   }else {
                       Toast.makeText(SignUpActivity.this, "Please Try Again", Toast.LENGTH_SHORT).show();
                   }
                });

            }
        });

        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            }
        });
    }
}