package ddwucom.mobile.healthstock;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class PositionActivity extends AppCompatActivity {

    Intent intent;
    ToggleButton toggleButton;
    TextView textView;
    Socket socket;
    Handler handler;

    private SQLiteDatabase db;
    private HealthStocksDBHelper helper;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_position);
        //socket 통신
        toggleButton = findViewById(R.id.togglePosition);
        textView = findViewById(R.id.data);
        handler = new Handler();

        helper = new HealthStocksDBHelper(PositionActivity.this);
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
                                    textView.setText(data);// Todo: 여기에 UI 작업할 코드를 입력하시면 됩니다.
                                }
                            });
                        }
                    }).start();
                    Log.d("socket", "input: " + data);

                    saveOrUpdate(data);

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

    protected void saveOrUpdate(String data) {
        db = helper.getWritableDatabase();
        //stocks에 오늘 거가 있는지 보고 있으면 바로 id return 없으면 생성 후에 id return
        //id를 가지고 health에 있는지 보고 있으면 update, 없으면 생성 (position인지 exercise인지도 봐야함)
//        cursor = db.rawQuery("select * from " + HealthStocksDBHelper.TABLE_STOCKS )
        int id = getStocksId();
    }

    protected int getStocksId() {
        int id = 0;
        return id;
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
    public void onClick(View v) {
        boolean on = ((ToggleButton) v).isChecked();
        if (on) {
            ServerThread thread = new ServerThread();
            Log.d("test", "onclick is on");
            thread.start();
            Toast.makeText(this, "측정 시작", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "측정 종료", Toast.LENGTH_SHORT).show();
            // 결과 다이얼로그 띄우기
//            AlertDialog.Builder dlg = new AlertDialog.Builder(PositionActivity.this);
//            intent = new Intent(this, MainActivity.class);
//            startActivity(intent);
            finish();
        }
    }

}