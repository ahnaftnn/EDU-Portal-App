package com.ahnaftn.eduportal;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


public class EditProfileActivity extends AppCompatActivity {

    ImageView uploadImage;
    TextView editUname, editEmail;
    EditText editPass;
    Button updateBtn;
    String unameUser,emailUser, paswordUser,imageURL;
    Uri uri;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        reference = FirebaseDatabase.getInstance().getReference("users");

        uploadImage = findViewById(R.id.profile_edit);
        editPass = findViewById(R.id.password_edit);
        editUname = findViewById(R.id.username_edit);
        editEmail = findViewById(R.id.email_edit);
        updateBtn = findViewById(R.id.update_btn);

        showData();

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isPassChanged()){
                    Toast.makeText(EditProfileActivity.this, "Password Changed Successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public boolean isPassChanged(){
        if(!paswordUser.equals(editPass.getText().toString())){
            reference.child(unameUser).child("password").setValue(editPass.getText().toString());
            reference.child(unameUser).child("cpassword").setValue(editPass.getText().toString());
            paswordUser = editPass.getText().toString();
            return true;
        }else {
            return false;
        }
    }


    public void showData(){
        Intent intent = getIntent();

        unameUser = intent.getStringExtra("username");
        emailUser = intent.getStringExtra("email");
        paswordUser = intent.getStringExtra("password");

        editUname.setText(unameUser);
        editEmail.setText(emailUser);
        editPass.setText(paswordUser);

    }
}