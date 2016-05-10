package com.cyl.wifi_hnust.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.cyl.wifi_hnust.MainActivity;
import com.cyl.wifi_hnust.R;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * Created by yonglong on 2016/5/10.
 */
public class BackgroundTask extends Service{
    final String demo = "id_userName=405g14&userName=405g14&userPwd=MzEzNzcw";
    final int[] base64DecodeChars = {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, -1, 63,
            52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14,
            15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40,
            41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1, -1, -1};
    PasswordHandler passwordHandler;
    Network network;
    MainActivity.broadcast broadcast;
    Context context;
    MsgBinder msgBinder;
    boolean Isconnect;

    public BackgroundTask(){
        passwordHandler=new PasswordHandler(this);
    }


    @Override
    public void onCreate() {
        super.onCreate();

        broadcast=new MainActivity().new broadcast(this);
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction("msg");
        registerReceiver(broadcast, intentFilter);
   //     new Thread(runnable).start();

    }

    public IBinder onBind(Intent intent) {
        if (msgBinder==null){
            msgBinder=new MsgBinder();
        }
        return msgBinder;
    }
    public class MsgBinder extends Binder {
        /**
         * 获取当前Service的实例
         * @return
         */
        public BackgroundTask getService(){
            return BackgroundTask.this;
        }
    }
    public boolean isConnectHNUST() {
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (!mWifi.isConnected()) {
            return false;
        }
        WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        Log.e("wifiInfo", wifiInfo.toString());
        Log.e("SSID",wifiInfo.getSSID());

        if((!(wifiInfo.getSSID().equals("HNUST"))&&!(wifiInfo.getSSID().equals("\"HNUST\"")))) {
            return false;
        }
        return true;
    }

    public boolean getConnect(){
        return Isconnect;
    }
    /**
     * 心跳包
     */
//    Runnable runnable= new Runnable() {
//        @Override
//        public void run() {
//            while (true) {
//                boolean flag=false;
//                try {
//                    if(isConnectHNUST()){
//                        Log.e("BackgroundTask", "已连接HNUST");
//                    }else{
//                        Log.e("BackgroundTask", "未连接HNUST");
//                    }
//                    try {
//                        String path = "http://suen.pw/empty.php";
//                        //创建URL实例
//                        URL url = new URL(path);
//                        //获取HettpConnection对象
//                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//                        conn.setRequestMethod("GET");
//                        conn.setConnectTimeout(3000);
//                        conn.setReadTimeout(3000);
//                        int code = conn.getResponseCode();
//                        if (code == 200) {
//                            InputStream is = conn.getInputStream();
//                            String text =readInputStream(is);
//                            Log.e("return","get任务执行成功");
//                            if(text.equals("success")&&isConnectHNUST()){
//                                Log.e("BackgroundTask", "已连接HNUST且上线");
//                                flag=true;
//                            }else if(text.equals("success")){
//                                Log.e("BackgroundTask", "网络通畅");
//                            }else if(text.contains("<head>")){
//                                Log.e("BackgroundTask", "无法连接到互联网");
//                            }
//                        } else {
//                            Log.e("BackgroundTask", "无法连接到互联网");
//                        }
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        Log.e("BackgroundTask", "oh,no,catch了，无法连接到互联网");
//                    }
//
//
//                    Message message= new Message();
//                    message.what = 1;
//                    if(flag) {
//                        message.getData().putBoolean("result",true);
//                    }else{
//                        Map<String,String> userInfo = Utils.getUserInfo(getApplicationContext());
//                        //判断自动连接开关及本地是否存在账户
//                        if(Utils.getSwitchStautus(getApplicationContext())&&userInfo!=null&&userInfo.get("username")!=null&&userInfo.get("password")!=null
//                                &&!userInfo.get("username").trim().equals("")&&!userInfo.get("password").trim().equals("")){
//                            network.connect(userInfo.get("username"),userInfo.get("password"));
//                        }
//                        message.getData().putBoolean("result",false);
//                        if(isConnectHNUST()){
//                            message.getData().putBoolean("connectHNUST",true);
//                        }else{
//                            message.getData().putBoolean("connectHNUST",false);
//                        }
//                    }
//                    handler.sendMessage(message);// 发送消息
//
//                    Thread.sleep(2000);// 线程暂停10秒，单位毫秒
//                } catch (InterruptedException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//            }
//        }
//    };

    public static String readInputStream(InputStream is) {
        try {
            ByteArrayOutputStream baos=new ByteArrayOutputStream();
            int len=0;
            byte[] buffer=new byte[1024];
            while ((len=is.read(buffer))!=-1) {
                baos.write(buffer,0,len);
            }
            is.close();
            baos.close();
            byte[] result=baos.toByteArray();
            //试着解析result中的字符串
            String temp=new String(result);
            return temp;
        } catch (Exception e) {
            e.printStackTrace();
            return "获取失败";
        }
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

 //       network=new Network(BackgroundTask.this);
        final Handler handler = new Handler();
        return super.onStartCommand(intent, flags, startId);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            super.handleMessage(msg);
            if (msg.what == 0) {
                String getresult = msg.getData().getString("result");
                Log.e("undecode", getresult);

                Log.e("decode", passwordHandler.i_p_f_base64Decode(getresult));
                // Log.e("decode", decode(getresult));
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(BackgroundTask.this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentText(getresult);
                mBuilder.setAutoCancel(true);//自己维护通知的消失
                mBuilder.setDefaults(Notification.DEFAULT_ALL);
                mBuilder.setPriority(Notification.PRIORITY_HIGH);
                NotificationManager mNotificationManager = (NotificationManager) BackgroundTask.this.getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.notify(0, mBuilder.build());
            }else if (msg.what==1){
                //网络连接情况
                boolean result=msg.getData().getBoolean("result");
                BackgroundTask.this.Isconnect=result;
                Intent intent=new Intent();
                intent.setAction("msg");
                intent.putExtra("isconnect", result);
                //HNUST连接情况
                boolean connectHNUST=msg.getData().getBoolean("connectHNUST");
                intent.putExtra("connectHNUST", connectHNUST);
                //发送广播
                sendBroadcast(intent);
            }
        }
    };
}
