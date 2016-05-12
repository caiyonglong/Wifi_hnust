package com.cyl.wifi_hnust;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.cyl.wifi_hnust.utils.BackgroundTask;
import com.cyl.wifi_hnust.utils.Network;
import com.cyl.wifi_hnust.utils.PasswordHandler;
import com.cyl.wifi_hnust.utils.SystemStatusManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
        , ServiceConnection {

    View mView;
    BackgroundTask.MsgBinder mbinder;
    boolean connect;
    static TextView connect_status;
    static Button operateButton;
    static ImageView login_info;
    static Switch auto_switch;
    static int operat_status = 0;
    SwipeRefreshLayout sf;
    Network network;
    CardView wifi_container;

    private Switch my_switch;
    PasswordHandler passwordHandler;
    WifiManager wifimanager;
    List<ScanResult> wifiScanResult = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTranslucentStatus();
        setContentView(R.layout.activity_main);

        network = new Network(this);

        passwordHandler = new PasswordHandler(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        startService(new Intent(MainActivity.this, BackgroundTask.class));

        wifimanager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);

        initView();
        initConnect();

        init();


    }

    private void initView() {

        login_info = (ImageView) findViewById(R.id.login_info);
        my_switch = (Switch) findViewById(R.id.my_switch);
        connect_status = (TextView) findViewById(R.id.connect_status);
        wifi_container = (CardView) findViewById(R.id.wifi_container);

        operateButton = (Button) findViewById(R.id.operate);
        operateButton.setOnClickListener(this);
        sf = (SwipeRefreshLayout) findViewById(R.id.container);
        sf.setColorSchemeResources(R.color.setting_blue,
                R.color.accountmanager_button_backgound_oringe_nomal,
                R.color.messagenatification_green);
        sf.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                init();

                sf.setRefreshing(false);

            }
        });

    }

    private void initConnect() {
        if (wifimanager.isWifiEnabled()) {
            my_switch.setChecked(true);
        } else {

            my_switch.setChecked(false);
        }


        my_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    wifimanager.setWifiEnabled(true);

                } else {
                    wifimanager.setWifiEnabled(false);
                }
            }
        });


    }


    private void init() {
        wifimanager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
        wifiScanResult = wifimanager.getScanResults();
        if (wifiScanResult == null) {
            connect_status.setText("wifi未打开");
        } else {
            Boolean result = false;
            WifiInfo wifiInfo = wifimanager.getConnectionInfo();
            Log.e("wifiInfo============", wifiInfo.toString());
            Log.e("SSID===========", wifiInfo.getSSID());
            if ((wifiInfo.getSSID().equals("HNUST") && (wifiInfo.getSSID().equals("\"HNUST\"")))) {
                result = true;
            }
            if (result) {
                login_info.setImageResource(R.mipmap.icon_wifi);
            } else {

                login_info.setImageResource(R.mipmap.icon_nowifi);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_internet) {
            Intent intent = new Intent(this, Main2Activity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // 需要setContentView之前调用
    private void setTranslucentStatus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 透明状态栏
            getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 透明导航栏
            getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            SystemStatusManager tintManager = new SystemStatusManager(this);
            tintManager.setStatusBarTintEnabled(true);
            // 设置状态栏的颜色
            tintManager.setStatusBarTintResource(R.color.setting_blue);
            getWindow().getDecorView().setFitsSystemWindows(true);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.operate:
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
//获取初始开关状态
                boolean tmp = prefs.getBoolean("auto_conn", false);
                //避免冲突操作
                operat_status = 1;

                String username = prefs.getString("number", "");
                String password = prefs.getString("password", "");

                if (username != null && password != null
                        && !username.trim().equals("") && !password.trim().equals("")) {
                    if (operateButton.getText().equals("手动上线")) {
                        Log.e("====", username);
                        Log.e("====", password);
                        network.connect(username, password);
                        operateButton.setText("请稍后");
                        operateButton.setEnabled(false);
                    } else if (operateButton.getText().equals("手动下线")) {
                        network.disconnect();
                        operateButton.setText("请稍后");
                        operateButton.setEnabled(false);
                    }
                } else {
                    Log.e("info", "本地无账户");
                    Toast.makeText(getApplicationContext(), "请先设置校园网账户！", Toast.LENGTH_LONG).show();
                }
                operat_status = 0;
                break;
//            case R.id.wifi_container:
//                AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                builder.setTitle("WIFI连接");
//                builder.setMessage("连接HNUST");
//                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//                builder.create();
//                builder.show();
//                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        mbinder = (BackgroundTask.MsgBinder) iBinder;
        BackgroundTask background = mbinder.getService();
        connect = background.getConnect();


    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }


    public class BroadCast extends BroadcastReceiver {
        Context rootcontext;

        public BroadCast(Context context) {
            this.rootcontext = context;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("intent", intent.getAction().toString());
            if (operat_status == 1) {
                return;
            }
            switch (intent.getAction()) {
                case "msg":
                    if (intent.getBooleanExtra("isconnect", false)) {
                        try {
                            operateButton.setEnabled(true);
                            connect_status.setText("已连接");

                            login_info.setImageResource(R.mipmap.icon_wifi);
                            operateButton.setBackgroundResource(R.color.setting_blue);
                            operateButton.setText("手动下线");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if (!intent.getBooleanExtra("connectHNUST", false)) {
                        try {
                            operateButton.setEnabled(true);
                            connect_status.setText("未连接");
                            operateButton.setText("请先连接校园无线网");
                            operateButton.setEnabled(false);
                            login_info.setImageResource(R.mipmap.icon_nowifi);
                            operateButton.setBackgroundResource(R.color.dark_gray);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            login_info.setImageResource(R.mipmap.icon_wifi);
                            operateButton.setEnabled(true);
                            connect_status.setText("未登录");
                            operateButton.setBackgroundResource(R.color.setting_blue);
                            operateButton.setText("手动上线");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

            }
        }
    }

}
