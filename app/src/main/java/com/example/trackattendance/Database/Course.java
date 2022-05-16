package com.example.trackattendance.Database;

import static androidx.room.ForeignKey.CASCADE;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.google.firebase.database.annotations.NotNull;

@Entity(foreignKeys = {@ForeignKey(entity = Course.class, parentColumns = "courseId", childColumns = "courseId", onDelete = CASCADE)})
public class Course {

    @PrimaryKey
    @NotNull
    public String courseId;

    public String courseName;
    public String year;
    public int numberOfClasses;

    public String courseCode;

    public Course(String courseId, String courseName, String year, int numberOfClasses, String courseCode) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.year = year;
        this.numberOfClasses = numberOfClasses;
        this.courseCode = courseCode;
    }
}
