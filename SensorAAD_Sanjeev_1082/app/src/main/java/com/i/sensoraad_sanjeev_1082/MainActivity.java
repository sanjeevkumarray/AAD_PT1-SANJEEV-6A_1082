package com.i.sensoraad_sanjeev_1082;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor sensor1, sensor2;
    private float[] mAccelerometerData = new float[3];
    private float[] mMagnetometerData = new float[3];
    TextView azimuthTV, pitchTV, fallTV;
    private float[] rotationMatrix = new float[9];
    private float orientationValues[] = new float[3];
    private boolean rotationOK;

    float azimuth, pitch,roll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor1 = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensor2 = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        azimuthTV = (TextView) findViewById(R.id.azimuth);
        pitchTV = (TextView) findViewById(R.id.pitch);
        fallTV = (TextView) findViewById(R.id.fall);
    }

    public void addFragment(View view){
        InfoFragment infoFragment = InfoFragment.newInstance();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.infoFrame,infoFragment);
        fragmentTransaction.commit();
    }

    public void removeFragment(View view){
        FragmentManager fragmentManager = getSupportFragmentManager();
        InfoFragment infoFragment = (InfoFragment) fragmentManager.findFragmentById(R.id.infoFrame);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.remove(infoFragment);
        fragmentTransaction.commit();
    }



    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        int sensorType = sensorEvent.sensor.getType();

        switch (sensorType) {
            case Sensor.TYPE_ACCELEROMETER:
                mAccelerometerData = sensorEvent.values.clone();
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                mMagnetometerData = sensorEvent.values.clone();
                break;
            default:
                return;
        }


        rotationOK = SensorManager.getRotationMatrix(rotationMatrix,null, mAccelerometerData, mMagnetometerData);
        if (rotationOK) {
            SensorManager.getOrientation(rotationMatrix, orientationValues);
        }
        azimuth = orientationValues[0];
        pitch = orientationValues[1];
        roll = orientationValues[2];

        azimuthTV.setText(String.valueOf(azimuth));
        pitchTV.setText(String.valueOf( pitch));
        fallTV.setText(String.valueOf(roll));

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    // register sensor type to listener
    @Override
    protected void onStart() {
        super.onStart();
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_UI);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),SensorManager.SENSOR_DELAY_UI);
    }

    // unregister sensor type to listener
    @Override
    protected void onStop() {
        super.onStop();
        sensorManager.unregisterListener(this);
    }
}