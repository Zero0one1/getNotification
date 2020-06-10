package com.example.getnotification;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    private TextView editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = findViewById(R.id.button);
        Button button2 = findViewById(R.id.button2);
        Button button3 = findViewById(R.id.button3);
        Button button4 = findViewById(R.id.button4);
        editText = findViewById(R.id.editText);
        Intent  intent = new Intent(MainActivity.this, MyNotifiService.class);
        startService(intent);
        final SharedPreferences sp = getSharedPreferences("msg", MODE_PRIVATE);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getMsg = sp.getString("getMsg", "");
                if (!TextUtils.isEmpty(getMsg)){
                    editText.setText(getMsg);
                }
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
            @Override
            public void onClick(View v) {
                //打开监听引用消息Notification access
                Intent intent_s = new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS);
                startActivity(intent_s);
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent_p = new Intent(Settings.ACTION_APP_USAGE_SETTINGS);
                Intent intent_p = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:com.example.getnotification"));
                startActivity(intent_p);

            }
        });
    }
}
