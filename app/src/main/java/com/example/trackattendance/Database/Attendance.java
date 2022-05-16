package com.example.trackattendance.Database;

import static androidx.room.ForeignKey.CASCADE;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;

@Entity(primaryKeys = {"regNo", "courseId"})

public class Attendance {

    @NonNull
    public String regNo;

    @NonNull
    public String courseId;

    public int attendanceNumber;

    public Attendance(String regNo, String courseId, int attendanceNumber) {
        this.regNo = regNo;
        this.courseId = courseId;
        this.attendanceNumber = attendanceNumber;
    }

}
