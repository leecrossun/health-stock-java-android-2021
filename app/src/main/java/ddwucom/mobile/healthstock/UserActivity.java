package ddwucom.mobile.healthstock;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import ddwucom.mobile.healthstock.dto.UserInfo;

public class UserActivity extends AppCompatActivity {
    
    UserInfo userInfo = new UserInfo();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_page);

        TextView userName = findViewById(R.id.userName1);
        userInfo.setUserName("하하하");
        userName.setText(userInfo.getUserName());
    }
}
