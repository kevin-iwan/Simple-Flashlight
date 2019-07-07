package com.example.shakemotionflashlight;

import android.content.Context;
import android.content.Intent;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private boolean isSwitchedOn = false;
    private Button btn_discoSwitch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_discoSwitch = findViewById(R.id.btn_disco);
        final Intent i = new Intent(this, ShakeDetection.class);
//        startService(i);

        btn_discoSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btn_discoSwitch.getText().toString().equals("DISCO MODE")) {
                    startService(i);
                }
            }
        });
    }

   public void toggleLED(View v) throws CameraAccessException {
        Button button = (Button)v;
        if(button.getText().toString().equals("Turn On Lights")){
            button.setText("Turn Off Lights");
            toggle("on");
        }
        else{
            button.setText("Turn On Lights");
            toggle("off");
        }
   }

    private void toggle(String lightPosition) throws CameraAccessException {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            CameraManager cameraManager = (CameraManager)getSystemService(Context.CAMERA_SERVICE);
            String cameraId = null;

            if(cameraManager != null){
                cameraId = cameraManager.getCameraIdList()[0];
            }
            if(cameraManager != null){
                if(lightPosition.equals("on")){
                    cameraManager.setTorchMode(cameraId, true);
                    isSwitchedOn = true;
                }
                else{
                    cameraManager.setTorchMode(cameraId, false);
                    isSwitchedOn = false;
                }
            }
        }
    }

}
