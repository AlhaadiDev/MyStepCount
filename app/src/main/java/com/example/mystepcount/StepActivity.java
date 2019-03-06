package com.example.mystepcount;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class StepActivity extends AppCompatActivity implements SensorEventListener {
    private long steps = 0;
    SensorManager sManager;
    Sensor stepSensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        stepSensor = sManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        ((TextView)findViewById(R.id.txt_hello)).setText(String.valueOf(getDistanceRun(steps)));
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;
        float[] values = event.values;
        int value = -1;
        if (values.length > 0) {
            value = (int) values[0];
        }

        if (sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {
            steps++;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        sManager.registerListener(this,stepSensor,SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    protected void onStop() {
        super.onStop();
        sManager.unregisterListener(this,stepSensor);
    }

    //function to determine the distance run in kilometers using average step length for men and number of steps
    public float getDistanceRun(long steps){
        float distance = (float)(steps*78)/(float)100000;
        return distance;
    }
}
