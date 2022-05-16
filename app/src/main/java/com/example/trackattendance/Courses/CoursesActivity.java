package com.example.trackattendance.Courses;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.trackattendance.R;
import com.example.trackattendance.databinding.ActivityCoursesBinding;

public class CoursesActivity extends AppCompatActivity {

    private ActivityCoursesBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCoursesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.floatingAddCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CoursesActivity.this, AddCourseActivity.class);
                startActivity(intent);
            }
        });

    }
}