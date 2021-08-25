package ddwucom.mobile.healthstock;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class WalkActivity extends AppCompatActivity implements SensorEventListener {
  
    SensorManager sensorManager;
    Sensor stepCountSensor;
    TextView stepCountView;

    Button startButton;
    Button stopButton;

    // 현재 걸음 수
    int currentSteps = 0;

    Intent intent = getIntent();

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walk);

        stepCountView = findViewById(R.id.stepCountView);

        startButton = findViewById(R.id.btn_start);
        stopButton = findViewById(R.id.btn_stop);

        // 활동 퍼미션 체크
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_DENIED){

            requestPermissions(new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, 0);
        }

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        stepCountSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);

        // 디바이스에 걸음 센서의 존재 여부 체크
        if (stepCountSensor == null) {
            Toast.makeText(this, "No Step Sensor", Toast.LENGTH_SHORT).show();
        }

        // 시작 버튼
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(stepCountSensor !=null) {
                    Toast.makeText(WalkActivity.this,"걸음 측정 시작",Toast.LENGTH_SHORT).show();
                    sensorManager.registerListener(WalkActivity.this,stepCountSensor,SensorManager.SENSOR_DELAY_FASTEST);
                }
            }
        });

        // 스탑 버튼
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("steps", currentSteps);
                intent.putExtra("steps_to_point", StepsToPoint(currentSteps));
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // 걸음 센서 이벤트 발생시
        if(event.sensor.getType() == Sensor.TYPE_STEP_DETECTOR){

            if(event.values[0]==1.0f){
                // 센서 이벤트가 발생할때 마다 걸음수 증가
                currentSteps++;
                stepCountView.setText(String.valueOf(currentSteps));
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    //1000 보 == 100 point
    public int StepsToPoint(int currentSteps){
        return (currentSteps / 1000) * 100;
    }
}