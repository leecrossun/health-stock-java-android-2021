package ddwucom.mobile.healthstock;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.widget.ToggleButton;

public class PositionActivity extends AppCompatActivity {

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_position);
    }

    public void onClick(View v) {
        boolean on = ((ToggleButton) v).isChecked();
        
        if (on) {
            Toast.makeText(this, "측정 시작", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "측정 종료", Toast.LENGTH_SHORT).show();
//            intent = new Intent(this, .class);
            startActivity(intent);
            finish();
        }
    }
}