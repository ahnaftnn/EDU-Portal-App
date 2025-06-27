package com.ahnaftn.eduportal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class SearchRoutineActivity extends AppCompatActivity {

    EditText courseCodeInput;
    Button searchBtn;
    RecyclerView routineRecycler;
    List<RoutineItem> routineList;
    RoutineAdapter adapter;
    String dbPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_routine);

        courseCodeInput = findViewById(R.id.courseCodeInput);
        searchBtn = findViewById(R.id.searchBtn);
        routineRecycler = findViewById(R.id.routineResultRecycler);
        routineRecycler.setLayoutManager(new LinearLayoutManager(this));

        routineList = new ArrayList<>();
        adapter = new RoutineAdapter(this, routineList);
        routineRecycler.setAdapter(adapter);

        dbPath = getDatabasePath("edu_portal_db.db").getPath();
        copyDatabaseIfNeeded();

        searchBtn.setOnClickListener(v -> {
            String courseCode = courseCodeInput.getText().toString().trim();
            if (!courseCode.isEmpty()) {
                fetchRoutine(courseCode);
            }
        });
    }

    private void copyDatabaseIfNeeded() {
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

    @SuppressLint("Range")
    private void fetchRoutine(String courseCode) {
       // sqlite use korle

       routineList.clear();
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READONLY);
        Cursor cursor = db.rawQuery("SELECT day, time, room, faculty FROM routine WHERE course LIKE ?",
                new String[]{"%" + courseCode + "%"});

        if (cursor.getCount() == 0) {
            Toast.makeText(this, "❌ Table 'routine' not found!", Toast.LENGTH_LONG).show();
            Log.e("DB_CHECK", "routine table not found or has no columns.");
        } else {
            Toast.makeText(this, "✅ Table 'routine' found.", Toast.LENGTH_SHORT).show();
            while (cursor.moveToNext()) {
                routineList.add(new RoutineItem(
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3)
                ));
            }
        }

        cursor.close();
        db.close();
        adapter.notifyDataSetChanged();
        //Toast.makeText(this, routineList.size(), Toast.LENGTH_SHORT).show();
    }
}