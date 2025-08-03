package com.ahnaftn.eduportal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.content.SharedPreferences;
import android.content.Context;
import android.widget.Toast;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    TextView sidText,nameText,deptText,cgText;
    ImageView cgpaButton,scheduleButton,menuButton;
    String name,dept;
    Double grade,credits;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sidText = findViewById(R.id.sid);
        nameText =findViewById(R.id.name_edit);
        deptText= findViewById(R.id.dept_edit);
        cgpaButton= findViewById(R.id.cgpa_button);
        scheduleButton = findViewById(R.id.schedule_button);
        menuButton = findViewById(R.id.menu_button);
        cgText = findViewById(R.id.cg_edit);

        String sid = userData();
        sidText.setText("ID: "+sid);

        String username = userName();
        Toast.makeText(this, username, Toast.LENGTH_SHORT).show();

        fetchData(sid);
        calculateCg(sid);

        menuButton.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(MainActivity.this, menuButton );
            popupMenu.getMenu().add("Classes");
            popupMenu.getMenu().add("Reports");
            popupMenu.getMenu().add("Finance");
            popupMenu.getMenu().add("Schedules");
            popupMenu.getMenu().add("Edit Profile");
            popupMenu.getMenu().add("Logout");
            popupMenu.show();
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if(item.getTitle()=="Logout"){

                        SharedPreferences preferences = getSharedPreferences("user_session", MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.clear();
                        editor.apply();

                        Toast.makeText(MainActivity.this, "Logged Out", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                    if(item.getTitle()=="Edit Profile"){

                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(username);

                        reference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists()) {
                                        String usernameDB = snapshot.child("username").getValue(String.class);
                                        String emailDB = snapshot.child("email").getValue(String.class);
                                        String passwordDB = snapshot.child("password").getValue(String.class);

                                        Intent intent = new Intent(MainActivity.this, EditProfileActivity.class);
                                        intent.putExtra("username", usernameDB);
                                        intent.putExtra("email", emailDB);
                                        intent.putExtra("password", passwordDB);
                                        startActivity(intent);

                                } else {
                                    Toast.makeText(MainActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                    return false;
                }
            });
        });

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
                Intent intent = new Intent(MainActivity.this, StudentRoutineActivity.class);
                intent.putExtra("sid",sid);
                startActivity(intent);
            }
        });

    }

    private void calculateCg(String sid) {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("students").child(sid).child("c_course");

        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                double totalCredits = 0;
                double totalGradePoints = 0;

                for (DataSnapshot courseSnap : snapshot.getChildren()) {
                    grade = courseSnap.child("grade").getValue(Double.class);
                    credits = courseSnap.child("credits").getValue(Double.class);

                    if (grade != null && credits != null) {
                        totalGradePoints += grade * credits;
                        totalCredits += credits;
                    }
                }

                double cgpa = totalCredits > 0 ? totalGradePoints / totalCredits : 0;
                String cg = String.format("%.2f", cgpa);

                if (cgText != null) {
                    cgText.setText(cg);
                } else {
                    Log.e("CGPA", "cgText view not found!");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Failed to fetch courses", Toast.LENGTH_SHORT).show();
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

    public String userName()
    {
        Intent intent = getIntent();
        String username= (intent.getStringExtra("username"));
        return username;
    }

}