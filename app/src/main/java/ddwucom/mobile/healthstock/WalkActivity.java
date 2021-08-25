package ddwucom.mobile.healthstock;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class WalkActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor stepCountSensor;
    TextView tvStepCount;
    float count = 0;
    static String TAG = "test";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walk);

        tvStepCount = (TextView)findViewById(R.id.tvStepCount);
        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        stepCountSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        if(stepCountSensor == null) {
            Log.d(TAG,"oncreate if문 안");
            Toast.makeText(this, "No Step Detect Sensor", Toast.LENGTH_SHORT).show();
        }
        Log.d(TAG,"oncreate if문 밖");
    }


    public void onClick(View v){
        switch (v.getId()){
            case R.id.btnStart:
                sensorManager.registerListener(this, stepCountSensor, SensorManager.SENSOR_DELAY_NORMAL);
                Log.d(TAG,"onclick 내부");
                Toast.makeText(this,"걸음 측정 시작",Toast.LENGTH_SHORT).show();
                break;
        }

    }

    @Override
    protected void onResume() {
        Log.d(TAG,"onresume 안");
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG,"onpause 안");
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
        Log.d(TAG,"onSensorChanged if문 안");
        float val = event.values[0];
        Toast.makeText(this, String.valueOf(event.values[0]) + "-" + String.valueOf(count),Toast.LENGTH_SHORT).show();
        if (count == 0) {
            count = val;
            Log.d(TAG,"count == 0 if문");
            tvStepCount.setText("Step Count : " + String.valueOf(val-count+1));
        }
        else {
            Log.d(TAG,"count != 0 if문");
            tvStepCount.setText("Step Count : " + String.valueOf(val-count+1));
        }
        }
        Log.d(TAG,String.valueOf(event.values[0])+" 값, "+event.sensor.getName()+" 센서");
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.d(TAG,"onAccuracyChanged 안");
    }

}
