package com.example.trackattendance.Database.AppDatabase;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.trackattendance.Database.Attendance;
import com.example.trackattendance.Database.Course;
import com.example.trackattendance.Database.DAO.AttendanceDao;
import com.example.trackattendance.Database.DAO.CourseDao;
import com.example.trackattendance.Database.DAO.StudentDao;
import com.example.trackattendance.Database.Student;

@Database(entities = {Course.class, Student.class, Attendance.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    public abstract CourseDao courseDao();
    public abstract StudentDao studentDao();
    public abstract AttendanceDao attendanceDao();

    public static AppDatabase getAppDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "attendance-database")
                            .fallbackToDestructiveMigration()
                            .allowMainThreadQueries()
                            .build();
        }
        return INSTANCE;
    }
    public static void destroyInstance() {
        INSTANCE = null;
    }
}
