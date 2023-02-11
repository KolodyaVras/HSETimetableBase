package org.hse.timetablehsebase;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class TimetableActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable);
    }

    public void btnStudentOnClick(View view) {
        Toast toast = Toast.makeText(getApplicationContext(),
                "Расписание для студентов", Toast.LENGTH_SHORT);
        toast.show();
    }

    public void btnTeacherOnClick(View view) {
        Toast toast = Toast.makeText(getApplicationContext(),
                "Расписание для преподавателей", Toast.LENGTH_SHORT);
        toast.show();
    }
}