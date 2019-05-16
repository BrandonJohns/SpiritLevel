package com.brandonjohns.spiritlevel;

import android.content.Context;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements SensorEventListener{

    private SpiritLevelView spiritLevelView;
    private SensorManager sensorManager;
    private Sensor sensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spiritLevelView = findViewById(R.id.spiritLevelView);
    }

    @Override
    protected void onResume() {
        super.onResume();

        //Sensor
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager != null ? sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) : null;
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    public float filterCoordinateHorizontally(float x) {
        Rect horizontalRect = spiritLevelView.getHorizontalRect();

        if (x > horizontalRect.right - spiritLevelView.getCircleRadius()) {
            x = horizontalRect.right - spiritLevelView.getCircleRadius();
        } else if (x < horizontalRect.left + spiritLevelView.getCircleRadius()){
            x = horizontalRect.left + spiritLevelView.getCircleRadius();
        }
        return x;
    }

    public float filterCoordinateVertically(float y) {
        Rect verticalRect = spiritLevelView.getVerticalRect();

        if (y > verticalRect.bottom - spiritLevelView.getCircleRadius()) {
            y = verticalRect.bottom - spiritLevelView.getCircleRadius();
        } else if (y < verticalRect.top + spiritLevelView.getCircleRadius()) {
            y = verticalRect.top + spiritLevelView.getCircleRadius();
        }
        return y;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float x, y;

        if (event.values[0] > 0) {  //tilt device right
            x = spiritLevelView.getCx() + 2;
            x = filterCoordinateHorizontally(x);
            spiritLevelView.setCx(x);
        } else if (event.values[0] < 0) { //tilt device left
            x = spiritLevelView.getCx() - 2;
            x = filterCoordinateHorizontally(x);
            spiritLevelView.setCx(x);
        }

        if (event.values[1] < 0) {  //up
            y = spiritLevelView.getCy() + 2;
            y = filterCoordinateVertically(y);
            spiritLevelView.setCy(y);
        } else if (event.values[1] > 0) {
            y = spiritLevelView.getCy() - 2;
            y = filterCoordinateVertically(y);
            spiritLevelView.setCy(y);
        }

        spiritLevelView.invalidate(); //redraw canvas
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
