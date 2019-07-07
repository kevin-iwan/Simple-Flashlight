package com.example.shakemotionflashlight;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.camera2.CameraAccessException;
import android.os.Build;
import android.os.IBinder;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;

public class ShakeDetection extends Service implements SensorEventListener {

    public final int MIN_TIME_BETWEEN_SHAKE = 1;
    SensorManager sensorManager = null;
    Vibrator vibrator = null;
    private long lastShakeTime = 0;
    private boolean isFlashLightOn = false;
    private Float shakeThreshold = 10.0f;
    Utility utility;

    public ShakeDetection(){

    }

    @Override
    public void onCreate() {
        super.onCreate();
        vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        utility = new Utility(this);

        if(sensorManager != null){
            Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            long currentTime = System.currentTimeMillis();
            if( (currentTime - lastShakeTime) > MIN_TIME_BETWEEN_SHAKE){
                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];

                double acceleration = Math.sqrt(Math.pow(x,2)) + Math.pow(y,2) + Math.pow(z,2) - SensorManager.GRAVITY_EARTH;

                if(acceleration > shakeThreshold){
                    lastShakeTime = currentTime;
                    if(!isFlashLightOn){
                        try {
                            isFlashLightOn = utility.torchToggle("on");
                        } catch (CameraAccessException e) {
                            e.printStackTrace();
                        }
                    }
                    else{
                        try {
                            isFlashLightOn = utility.torchToggle("off");
                        } catch (CameraAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
