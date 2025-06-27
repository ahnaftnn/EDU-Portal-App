package com.ahnaftn.eduportal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.SharedPreferences;
import android.content.Context;
import android.widget.Toast;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ktx.Firebase;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    TextView sidText,nameText,deptText;
    ImageView logoutButton, cgpaButton,scheduleButton;
    String name,dept;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sidText = findViewById(R.id.sid);
        logoutButton = findViewById(R.id.logout_button);
        nameText =findViewById(R.id.name_edit);
        deptText= findViewById(R.id.dept_edit);
        cgpaButton= findViewById(R.id.cgpa_button);
        scheduleButton = findViewById(R.id.schedule_button);

        String sid = userData();
        sidText.setText("ID: "+sid);

        fetchData(sid);

        cgpaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CgpaCalculator.class);
                startActivity(intent);
            }
        });

        scheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SearchRoutineActivity.class);
                startActivity(intent);
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getSharedPreferences("user_session", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear(); // remove all stored values
                editor.apply();

                Toast.makeText(MainActivity.this, "Logged Out", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });


    }

    private void fetchData(String sid) {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("students");

        String studentKey = sid;
        dbRef.child(studentKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    name = dataSnapshot.child("name").getValue(String.class);
                    dept = dataSnapshot.child("dept").getValue(String.class);

                    nameText.setText(name);
                    deptText.setText("Department of "+dept);

                } else {
                    Toast.makeText(MainActivity.this, "Student not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    public String userData()
    {
        Intent intent = getIntent();
        String sid= (intent.getStringExtra("sid"));
        return sid;
    }
}