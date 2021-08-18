package ddwucom.mobile.healthstock;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class HeartrateActivity extends AppCompatActivity {
    boolean run_init = true;
    Thread raspberryThread;
    boolean run_rasp = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heartrate);
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start:
                Toast.makeText(HeartrateActivity.this, "심박수 측정을 시작합니다.", Toast.LENGTH_SHORT).show();

                break;
            case R.id.btn_finish:
                String s = "3";
                AlertDialog.Builder dlg = new AlertDialog.Builder(HeartrateActivity.this);
                dlg.setTitle("운동 결과"); //제목
                dlg.setMessage("건강주식이" + s + "만큼 상승하였습니다."); // 메시지
                dlg.setIcon(R.drawable.ic_notifications_black_24dp); // 아이콘 설정
//                버튼 클릭시 동작
                dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //토스트 메시지
                        Toast.makeText(HeartrateActivity.this, "메인으로 이동합니다.", Toast.LENGTH_SHORT).show();

                        // Main Activity 이동
                        Intent intent = new Intent(HeartrateActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                });
                dlg.show();
                break;
        }
    }
}