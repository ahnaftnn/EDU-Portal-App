package com.ahnaftn.eduportal;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class CgpaCalculator extends AppCompatActivity {

    Spinner gradeSpn;
    LinearLayout resultContainer;
    Button addmoreBtn, submitBtn;
    LayoutInflater inflater;
    EditText cgEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cgpa_calculator);

        gradeSpn= findViewById(R.id.grade_spn);
        resultContainer = findViewById(R.id.result_container);
        addmoreBtn = findViewById(R.id.add_more_btn);
        inflater = LayoutInflater.from(this);
        submitBtn = findViewById(R.id.submit_btn);
        cgEdit = findViewById(R.id.cg_edit);

        //spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.grades_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gradeSpn.setAdapter(adapter);

        addmoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCourseRow();
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> courseCodes = new ArrayList<>();
                List<Double> grades = new ArrayList<>();
                List<Double> credits = new ArrayList<>();
                double grcr_sum = 0, cr_sum = 0;
                double cgpa;

                for (int i = 0; i < resultContainer.getChildCount(); i++) {
                    View rowView = resultContainer.getChildAt(i);

                    if (rowView instanceof LinearLayout) {
                        LinearLayout row = (LinearLayout) rowView;

                        EditText courseCodeField = (EditText) row.getChildAt(0);
                        Spinner gradeSpinner = (Spinner) row.getChildAt(1);
                        EditText creditField = (EditText) row.getChildAt(2);

                        String courseCode = courseCodeField.getText().toString().trim();
                        String gradeStr = gradeSpinner.getSelectedItem().toString().trim().substring(0,4);
                        String creditStr = creditField.getText().toString().trim();

                        if (creditStr.isEmpty()) {
                            Toast.makeText(CgpaCalculator.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        try {
                            double credit = Double.parseDouble(creditStr);
                            double grade = Double.parseDouble(gradeStr);

                            courseCodes.add(courseCode);
                            grades.add(grade);
                            credits.add(credit);
                        } catch (NumberFormatException e) {
                            Toast.makeText(CgpaCalculator.this, "Invalid credit input", Toast.LENGTH_SHORT).show();
                            return;
                        }

                    }
                }

                for (int iter = 0; iter < courseCodes.size(); iter++) {
                    double credit = credits.get(iter);
                    double gradeValue = grades.get(iter);

                    cr_sum += credit;
                    grcr_sum += (gradeValue * credit);
                }

                cgpa = (cr_sum == 0) ? 0 : (grcr_sum / cr_sum);
                cgEdit.setTextColor(Color.parseColor("#6A0032"));
                cgEdit.setText("Total CGPA : "+new DecimalFormat("##.##").format(cgpa));
            }
        });

    }

    private void addCourseRow(){
        // Create a new horizontal layout
        LinearLayout newRow = new LinearLayout(this);
        newRow.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        newRow.setOrientation(LinearLayout.HORIZONTAL);
        newRow.setGravity(Gravity.CENTER);
        newRow.setWeightSum(4);
        newRow.setPadding(0, 0, 0, 20);

        // Create Course Code EditText
        EditText courseCode = new EditText(this);
        courseCode.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT,1));
        courseCode.setHint("Course Code");
        courseCode.setBackground(getResources().getDrawable(R.drawable.input_field_bg));
        courseCode.setPadding(10, 10, 10, 10);
        courseCode.setTextColor(Color.parseColor("#6A0032"));
        courseCode.setHintTextColor(Color.parseColor("#6A0032"));

        // Create Grade Spinner
        Spinner gradeSpinner = new Spinner(this);
        LinearLayout.LayoutParams spinnerParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT,1);
        spinnerParams.setMargins(8, 0, 8, 0);
        gradeSpinner.setPadding(10,10,10,10);
        gradeSpinner.setLayoutParams(spinnerParams);
        gradeSpinner.setBackground(getResources().getDrawable(R.drawable.input_field_bg));

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.grades_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gradeSpinner.setAdapter(adapter);

        // Create Credit EditText
        EditText credit = new EditText(this);
        credit.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT,1));
        credit.setHint("Credit");
        credit.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        credit.setBackground(getResources().getDrawable(R.drawable.input_field_bg));
        credit.setPadding(10, 10, 10, 10);
        credit.setTextColor(R.color.maroon);
        credit.setHintTextColor(R.color.maroon);

        Button deleteBtn = new Button(this);
        LinearLayout.LayoutParams deleteParams = new LinearLayout.LayoutParams(0,ViewGroup.LayoutParams.WRAP_CONTENT,1);
        deleteBtn.setLayoutParams(deleteParams);
        deleteBtn.setText("❌");
        deleteBtn.setTextSize(14);
        deleteBtn.setBackgroundColor(Color.TRANSPARENT);
        deleteBtn.setTextColor(Color.RED);

        // Delete row on click
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resultContainer.removeView(newRow);
            }
            });

        // Add all views to the new row
        newRow.addView(courseCode);
        newRow.addView(gradeSpinner);
        newRow.addView(credit);
        newRow.addView(deleteBtn);

        // Add the new row to the course container
        resultContainer.addView(newRow);
    }

}