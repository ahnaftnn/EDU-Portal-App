package com.ahnaftn.eduportal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class StudentRoutineActivity extends AppCompatActivity {
    String sid;
    RecyclerView routineRecycler;
    List<RoutineItem> routineList;
    RoutineAdapter adapter;
    String dbPath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_routine);

        sid = getIntent().getStringExtra("sid");
        routineRecycler = findViewById(R.id.routineResultRecycler);
        routineRecycler.setLayoutManager(new LinearLayoutManager(this));

        routineList = new ArrayList<>();
        adapter = new RoutineAdapter(this, routineList);
        routineRecycler.setAdapter(adapter);

        dbPath = getDatabasePath("edu_portal.db").getPath();
        Toast.makeText(this, sid, Toast.LENGTH_SHORT).show();
        copyDatabaseIfNeeded();

        fetchCourse();
    }

    private void copyDatabaseIfNeeded() {
        //git push er jonno
        File dbFile = new File(dbPath);
        if (!dbFile.exists()) {
            try {
                Log.d("DB_COPY", "Copying database from assets...");
                InputStream is = getAssets().open("edu_portal.db");
                FileOutputStream fos = new FileOutputStream(dbPath);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, length);
                }
                fos.flush();
                fos.close();
                is.close();
                Log.d("DB_COPY", "Database copied successfully.");
            } catch (IOException e) {
                Log.e("DB_COPY", "Copy failed: " + e.getMessage());
            }
        } else {
            Log.d("DB_COPY", "Database already exists.");
        }
    }

    private void fetchCourse() {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("students");

        dbRef.child(sid).child("r_course").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> runningCourses = new ArrayList<>();
                for (DataSnapshot courseSnap : snapshot.getChildren()) {
                    String course = courseSnap.getValue(String.class);
                    runningCourses.add(course);
                }

                fetchRoutineFromSQLite(runningCourses);
            }

            private void fetchRoutineFromSQLite(List<String> courses) {
                routineList.clear();

                SQLiteDatabase db = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READONLY);

                for (String courseCode : courses) {
                    Cursor cursor = db.rawQuery("SELECT course, day, time, room, faculty FROM routine WHERE course = ?", new String[]{courseCode});

                    while (cursor.moveToNext()) {
                        routineList.add(new RoutineItem(
                                cursor.getString(0),
                                cursor.getString(1),
                                cursor.getString(2),
                                cursor.getString(3),
                                cursor.getString(4)
                        ));
                    }
                    cursor.close();
                }

                db.close();

                runOnUiThread(() -> adapter.notifyDataSetChanged());
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(StudentRoutineActivity.this, "Failed to fetch courses", Toast.LENGTH_SHORT).show();
            }
        });

    }


    public String userData() {
        Intent intent = getIntent();
        String sid= (intent.getStringExtra("sid"));
        return sid;
    }
}