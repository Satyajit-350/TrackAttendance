package com.example.trackattendance.Database;

import static androidx.room.ForeignKey.CASCADE;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices={@Index(value="regNo", unique=true)},
        foreignKeys = {@ForeignKey(entity = Student.class, parentColumns = "regNo", childColumns = "regNo", onDelete = CASCADE)})
public class Student {

    @PrimaryKey
    @NonNull
    public String studentId; //personID

    public String courseId; //personGroupID

    public String studentName;

    @NonNull
    public String regNo;

    //also includes faces
    public String faceArrayJson;

    public Student(String studentId, String courseId, String studentName, @NonNull String regNo, String faceArrayJson) {
        this.studentId = studentId;
        this.courseId = courseId;
        this.studentName = studentName;
        this.regNo = regNo;
        this.faceArrayJson = faceArrayJson;
    }

    /**
     * we use hashcode() method to get the hashcode value of an object
     * equal() method is used for checking  the equality of objects
     */

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        //if the object is not the instance of the class then return false
        if (!(obj instanceof Student)) {
            return false;
        }
        Student student = (Student) obj;
        return this.studentId.equals(student.studentId);
    }

    @Override
    public int hashCode() {
        //return the integer value of the given objects
        return studentId.hashCode();
    }
}
