package com.example.trackattendance.Database.DAO;

import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.trackattendance.Database.Course;

import java.util.List;

public interface CourseDao {

    @Query("SELECT * FROM Course")
    List<Course> getAll();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAll(Course... courses);

    @Query("SELECT COUNT(*) from Course")
    int countCourses();

    @Query("SELECT numberOfClasses from Course where courseId = :courseId")
    int getNumberOfClasses(String courseId);

    @Query("SELECT courseName from Course where courseId = :courseId")
    String getCourseNameById(String courseId);

    @Query("DELETE from Course where courseId=:courseId")
    void deleteByCourseId(String courseId);
}
