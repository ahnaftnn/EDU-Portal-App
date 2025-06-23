package com.ahnaftn.eduportal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.content.SharedPreferences;
import android.content.Context;
import android.widget.Toast;


import com.google.firebase.ktx.Firebase;

public class MainActivity extends AppCompatActivity {
    TextView sidText;
    Button logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sidText = findViewById(R.id.sid);
        logoutButton = findViewById(R.id.logout_button);

        userData();

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getSharedPreferences("user_session", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear(); // remove all stored values
                editor.apply();

                Toast.makeText(MainActivity.this, "Logged Out", Toast.LENGTH_SHORT).show();

                // Go back to login screen and clear activity stack
                Intent intent = new Intent(MainActivity.this, CgpaCalculator.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

    }

    public void userData()
    {
        Intent intent = getIntent();
        String sid = intent.getStringExtra("sid");

        sidText.setText(sid);
    }
}