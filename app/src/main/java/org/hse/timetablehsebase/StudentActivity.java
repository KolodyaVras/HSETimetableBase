package org.hse.timetablehsebase;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.sql.Time;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class StudentActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_student);

        final Spinner spinner = findViewById(R.id.groupList);

        List<Group> groups = new ArrayList<>();
        initGroupList(groups);

        ArrayAdapter<?> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, groups);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View itemSelected, int selectedItemPosition, long selectedId) {
                Object item = adapter.getItem(selectedItemPosition);
                Log.d(TAG, "selectedItem" + item);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        time=findViewById(R.id.time);
        initTime();

        status = findViewById(R.id.status);
        subject = findViewById(R.id.subject);
        cabinet = findViewById(R.id.cabinet);
        corp = findViewById(R.id.corp);
        teacher = findViewById(R.id.teacher);

        initData();
    }
    static class Group{
        private Integer id;
        private String name;

        public Group(Integer id, String name){
            this.id = id;
            this.name = name;
        }

        public Integer getId(){
            return id;
        }

        public void setId(Integer id){
            this.id = id;
        }
        @Override
        public String toString(){
            return name;
        }

        public String getName(){
            return name;
        }

        public void setName(String name){
            this.name = name;
        }
    }
    private void initGroupList(List<Group> groups){
        String[] direction = new String[4]; //Направление
        direction[0] = "ПИ-";
        direction[1] = "БИ-";
        direction[2] = "И-";
        direction[3] = "Э-";

        String[] Year = new String[4];//Год
        Year[0] = "18-";
        Year[1] = "19-";
        Year[2] = "20-";
        Year[3] = "21-";

        String[] Group = new String[3];//Группа
        Group[0] = "1";
        Group[1] = "2";
        Group[2] = "3";
        for (Integer i = 0; i<direction.length;i++){

            for (Integer r = 0;r<Year.length;r++){
                for(Integer t = 0; t<Group.length;t++){
                    groups.add(new Group(i+3,direction[i].concat(Year[r]).concat(Group[t])));
                }
            }
        }
        groups.add(new Group(1, "ПИ-20-1"));
        groups.add(new Group(2, "ПИ-20-2"));
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