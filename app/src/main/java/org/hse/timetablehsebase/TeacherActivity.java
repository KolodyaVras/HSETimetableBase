package org.hse.timetablehsebase;

import static android.service.controls.ControlsProviderService.TAG;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Locale;

public class TeacherActivity extends AppCompatActivity {
    private TextView teacher;
    private TextView  status;
    private TextView  subject;
    private TextView  corp;
    private TextView  cabinet;
    private TextView time;
    private Date currentTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);
        final Spinner spinner = findViewById(R.id.groupList);
        List<StudentActivity.Group> groups = new ArrayList<>();
        initGroupList(groups);

        ArrayAdapter<?> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, groups);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View itemSelected, int selectedItemPosition, long selectedID) {
                Object item = adapter.getItem(selectedItemPosition);
                Log.d(TAG,"selectedItem" + item);
            }


            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        time = findViewById(R.id.time);
        initTime();

        status = findViewById(R.id.status);
        subject = findViewById(R.id.subject);
        cabinet = findViewById(R.id.cabinet);
        corp = findViewById(R.id.corp);
        teacher = findViewById(R.id.teacher);

        initData();
    }
    private void initGroupList(List<StudentActivity.Group> groups){
        groups.add(new StudentActivity.Group(1, "Преподаватель 1"));
        groups.add(new StudentActivity.Group(2, "Преподаватель 2"));
    }
    private void initTime(){
        currentTime = new Date();
        Locale locale = new Locale("ru", "RU");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM HH:mm",locale);
        time.setText(getDayStringOld(currentTime,locale).substring(0, 1).toUpperCase() + getDayStringOld(currentTime,locale).substring(1) + " " + simpleDateFormat.format(currentTime));
    }
    private void initData(){
        status.setText("Нет пар");
        subject.setText("Дисциплина");
        cabinet.setText("Кабинет");
        corp.setText("Корпус");
        teacher.setText("Преподаватель");
    }
    public static String getDayStringOld(Date date, Locale locale) {
        DateFormat formatter = new SimpleDateFormat("EEEE", locale);
        return formatter.format(date);
    }
}