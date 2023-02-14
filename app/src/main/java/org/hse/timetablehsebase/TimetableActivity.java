package org.hse.timetablehsebase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class TimetableActivity extends AppCompatActivity {

    public Button btnStudent;
    public Button btnTeacher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable);
        btnStudent = (Button) findViewById(R.id.btnStudent);
        btnTeacher = (Button) findViewById(R.id.btnTeacher);
        btnStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showStudent();
            }
        });
        btnTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTeacher();
            }
        });

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

    private void showStudent(){
        Intent intent = new Intent(this, StudentActivity.class);
        startActivity(intent);
    }
    private void showTeacher(){
        Intent intent = new Intent(this, TeacherActivity.class);
        startActivity(intent);
    }
}