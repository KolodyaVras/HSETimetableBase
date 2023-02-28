package org.hse.timetablehsebase;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SettingsActivity extends AppCompatActivity implements SensorEventListener {
    private static final int REQUEST_IMAGE_CAPTURE = 0;
    private SensorManager sensorManager;
    private static final int REQUEST_TAKE_PHOTO = 1;
    private ImageView imageView;
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    String FILE_AUTHORITY = "org.hse.timetablehsebase";

    private String folderToSave; // папка с кешем приложения
    private final String imgSaveName = "myImage.jpg";
    private final String inputNameInSettings = "inputName";

    private EditText nameInput;
    private ImageView photoView;
    private SharedPreferences preferences;

    private Sensor lightSensor;
    private TextView lightOutput;
    private Sensor gyroSensor;
    private TextView gyroOutput;
    private Sensor accelerometerSensor;
    private TextView accelerometerOutput;
    private Sensor motionSensor;
    private TextView motionOutput;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        folderToSave = this.getCacheDir().toString();
        preferences = getSharedPreferences("settings", Context.MODE_PRIVATE);

        photoView = findViewById(R.id.photoView);

        LoadImage(photoView, folderToSave, imgSaveName);

        initSensors();

        Button takePhoto = findViewById(R.id.takePhotoBtn);
        takePhoto.setOnClickListener(this::onTakePhotoClicked);

        nameInput = findViewById(R.id.nameTextBox);

        Button saveButton = findViewById(R.id.saveBtn);
        saveButton.setOnClickListener((view) -> {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(inputNameInSettings, nameInput.getText().toString()).apply();
        });

        SensorManager mgr;
        Sensor sensor;

        mgr = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        sensor = mgr.getDefaultSensor(Sensor.TYPE_LIGHT);
        List<Sensor> deviceSensors = mgr.getSensorList(Sensor.TYPE_ALL);

        Spinner spinner = (Spinner) findViewById(R.id.sensorslist);
        ArrayAdapter<?> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, deviceSensors);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        /*List<Sensor> sensorList = mgr.getSensorList(Sensor.TYPE_ALL);
        StringBuilder strBuilder = new StringBuilder();

        for(Sensor s: sensorList){
            strBuilder.append(s.getName()+"\n");
        }

        txtList.setVisibility(View.VISIBLE);
        txtList.setText(strBuilder);*/
    }

    public void onTakePhotoClicked(View v) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
        }
        else {
            takePhoto();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_REQUEST_CODE) {
            takePhoto();
        }
    }

    private void takePhoto() {
        // Используем стандартный системный Intent для использования камеры:
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, GetUriForPath(folderToSave, imgSaveName));

        try {
            openSomeActivityForResult(takePhotoIntent);
        } catch (Exception e) {
            Log.d("myerror", e.getMessage());
        }
    }

    private Uri GetUriForPath(String pathPart1, String pathPart2) {
        File file = new File(pathPart1, pathPart2);
        return FileProvider.getUriForFile(Objects.requireNonNull(getApplicationContext()),
                BuildConfig.APPLICATION_ID + ".provider", file);
    }

    public void openSomeActivityForResult(Intent intent) {
        someActivityResultLauncher.launch(intent);
    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        photoView.setImageURI(GetUriForPath(folderToSave, imgSaveName));
                    }
                }
            });

    private void dispatchTakenPictureIntent(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null){
            File photoFile = null;
            photoFile = createImageFile();
            if (photoFile != null){
                Uri photoURI = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                try{
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                } catch (ActivityNotFoundException e){
                    Log.e(TAG, "Start activity", e);
                }
            }
        }
    }

    private void LoadImage(ImageView iv, String folder, String fileName) {
        File file = new File(folder, fileName);

        if(file.exists()) {
            Uri uri = Uri.parse(file.getAbsolutePath());
            iv.setImageURI(uri);
        }
    }

    // камера
    public void capturePhoto() {
        Intent intent = new Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_TAKE_PHOTO);
        }
    }
    private void showExplanation(String title,
                                 String message,
                                 final String[] permission,
                                 final int permissionRequestCode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    public void onClick(DialogInterface dialog, int id) {
                        requestPermissions(permission, permissionRequestCode);
                    }
                });
        builder.create().show();
    }

    private File createImageFile() {
    File file = new File("C:\\My folder\\HSE\\Mobile dev\\Projects\\Project3");
    return file;}
    private Uri generateFileUri() {

        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
            return null;

        File path = new File (Environment.getExternalStorageDirectory(), "CameraTest");
        if (! path.exists()){
            if (! path.mkdirs()){
                return null;
            }
        }

        String timeStamp = String.valueOf(System.currentTimeMillis());
        File newFile = new File(path.getAbsolutePath() + File.separator + timeStamp + ".jpg");
        return Uri.fromFile(newFile);
    }
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_LIGHT) {
            float lux = sensorEvent.values[0];
            lightOutput.setText(lux + " lux");
        }
        else if (sensorEvent.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            gyroOutput.setText(String.valueOf(sensorEvent.values[0]));
        }
        else if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            accelerometerOutput.setText(String.valueOf(sensorEvent.values[0]));
        }
        else if (sensorEvent.sensor.getType() == Sensor.TYPE_MOTION_DETECT) {
            motionOutput.setText(String.valueOf(sensorEvent.values[0]));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    protected void onResume(){
        super.onResume();

        sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, gyroSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, motionSensor, SensorManager.SENSOR_DELAY_NORMAL);

        if(preferences.contains(inputNameInSettings)){
            String text = preferences.getString(inputNameInSettings, "");

            nameInput.setText(text);
        }
    }
    @Override
    protected void onPause(){
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    private void initSensors()
    {
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        lightOutput = findViewById(R.id.brightnessText);
        gyroOutput = findViewById(R.id.gyroscopeText);
        accelerometerOutput = findViewById(R.id.accelerometerText);
        motionOutput = findViewById(R.id.motionText);

        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        gyroSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        motionSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MOTION_DETECT);
    }
}