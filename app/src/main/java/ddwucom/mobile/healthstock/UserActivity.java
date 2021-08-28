package ddwucom.mobile.healthstock;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

import ddwucom.mobile.healthstock.dto.UserInfo;

public class UserActivity extends AppCompatActivity {
    
    UserInfo userInfo = new UserInfo();
    TextView userName;
    TextView stock;
    TextView exer1;
    TextView exer2;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_page);
        Intent intent = getIntent();

        userName = (TextView)findViewById(R.id.userName);
        userName.setText(intent.getExtras().getString("userName"));

        stock = (TextView)findViewById(R.id.asset);
        stock.setText(intent.getExtras().getString("stock"));

        exer1 = (TextView)findViewById(R.id.exer1);
        exer1.setText("운동 : " + intent.getExtras().getString("exercise"));

        exer2 = (TextView)findViewById(R.id.exer2);
        exer2.setText("자세 : " + intent.getExtras().getString("position"));

    }
}
