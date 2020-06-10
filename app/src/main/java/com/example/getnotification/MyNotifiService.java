package com.example.getnotification;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
@SuppressLint("OverrideAbstract")
public class MyNotifiService extends NotificationListenerService {

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");

    Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            String msgString = (String) msg.obj;
            Toast.makeText(getApplicationContext(), msgString, Toast.LENGTH_LONG).show();
        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Context context = getApplicationContext();
        Log.i("KEVIN", "Service is started" + "-----" + context.getExternalFilesDir(null));
        return super.onStartCommand(intent, flags, startId);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        try {
            //debug
//            android.os.Debug.waitForDebugger();
//            super.onCreate();

            /*basic info*/
            long postTime = sbn.getPostTime();
            String notifyTime = sdf.format(new Date(postTime));
            String pkg = sbn.getPackageName();

            /*init file*/
            Log.d("note", "begin to init file");
            Context context = getApplicationContext();
            File outputfile = new File(context.getExternalFilesDir(null), "record.txt");
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(outputfile, true), "gbk");

            // check if it use tickerText or builder
            if (sbn.getNotification().tickerText != null) {
                SharedPreferences sp = getSharedPreferences("msg", MODE_PRIVATE);
                String nMessage = sbn.getNotification().tickerText.toString();
                Log.e("KEVIN", "Get Message" + "-----" + nMessage);
                sp.edit().putString("getMsg", nMessage).apply();
                Message obtain = Message.obtain();
                obtain.obj = nMessage;
                mHandler.sendMessage(obtain);

                /*write data (time: package, content)*/
                Log.d("note", "begin to write data" + nMessage);
                outputStreamWriter.write("TIME:" + notifyTime + " ,PKG: " + pkg + " ,CONTENT:" + nMessage);
                String newLine = System.getProperty("line.separator");
                assert newLine != null;
                outputStreamWriter.write(newLine);
            }
            else{
                /*get title content extra*/
                Bundle bundle = sbn.getNotification().extras;
                String contentTitle = bundle.getString(Notification.EXTRA_TITLE);
                if (contentTitle == null) {
                    contentTitle = "";
                }
                String contentText = bundle.getString(Notification.EXTRA_TEXT);
                if (contentText == null) {
                    contentText = "";
                }

                /*write data (time: package, content)*/
                Log.d("note", "begin to write data, title:" + contentTitle + " ,text: " + contentText);
                outputStreamWriter.write("TIME:" + notifyTime + " ,PKG: " + pkg + " ,TITLE:" + contentTitle + ", TEXT" + contentText);
                String newLine = System.getProperty("line.separator");
                assert newLine != null;
                outputStreamWriter.write(newLine);
            }
            /*close writer*/
            Log.d("note", "close outputStreamWriter");
            outputStreamWriter.close();

        } catch (Exception e) {
            Log.d("note", "不可解析的通知");
        }

    }

}