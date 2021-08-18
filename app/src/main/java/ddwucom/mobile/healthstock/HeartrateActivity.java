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
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class HeartrateActivity extends AppCompatActivity {
    boolean run_init = true;
    Thread raspberryThread;
    boolean run_rasp = true;
    TextView heartRate;
    Socket socket;
    Handler handler;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heartrate);
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        heartRate = findViewById(R.id.txt_heartrate);
        handler = new Handler();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start:
                HeartrateActivity.ServerThread thread = new HeartrateActivity.ServerThread();
                Log.d("test", "onclick is on");
                thread.start();
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
                finish();
                break;
        }
    }

    class ServerThread extends Thread {
        @Override
        public void run() {
            int port = 9999;

            try {
                ServerSocket server = new ServerSocket(port);
                Log.d("ServerThread", "Server Started.");

                while(true){
                    socket = server.accept();

                    InputStream input = socket.getInputStream();
                    if (socket.isConnected()){
                        Log.d("socket", "connect");
                        Log.d("socket", String.valueOf(input.available()));
                    }

                    byte[] byteArr = new byte[100];
                    int readByteCount = input.read(byteArr);
                    String data = new String(byteArr, 0, readByteCount, "UTF-8");
                    //결과값 띄워주기
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    heartRate.setText(data);// Todo: 여기에 UI 작업할 코드를 입력하시면 됩니다.
                                }
                            });
                        }
                    }).start();
                    Log.d("socket", "input: " + data);

                    OutputStream out = socket.getOutputStream();
                    PrintWriter writer = new PrintWriter(out, true);
                    writer.println("server send");
                    Log.d("socket", "output: " + data);
                    socket.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            socket.close();//종료시 소켓도 닫아주어야한다.
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}