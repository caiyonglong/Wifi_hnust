package com.cyl.wifi_hnust;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.cyl.wifi_hnust.utils.BackgroundTask;
import com.cyl.wifi_hnust.utils.Network;
import com.cyl.wifi_hnust.utils.SystemStatusManager;
import com.cyl.wifi_hnust.utils.Utils;

import java.util.Map;

public class MainActivity extends AppCompatActivity {

    View mView;
    BackgroundTask.MsgBinder mbinder;
    boolean connect;
    static TextView connect_status;
    static Button operateButton;
    static Switch auto_switch;
    static int operat_status=0;
    Network network;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTranslucentStatus();
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initConnect();



    }

    private void initConnect() {

        connect_status=(TextView) findViewById(R.id.connect_status);
        connect_status.setText("请稍后...");
        operateButton = (Button) findViewById(R.id.operate);
        operateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (operat_status ==0){
                    operat_status=1;
                    connect_status.setText("连接失败,请重试...");
                    operateButton.setText("手动上线");
                }
                else {
                    operat_status=0;
                    connect_status.setText("连接正常");
                    operateButton.setText("上线成功");
                }
            }
        });




    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this ,SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_internet) {
            Intent intent = new Intent(this ,Main2Activity.class);
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


    public class broadcast extends BroadcastReceiver {
        Context rootcontext;
        public broadcast(Context context){
            this.rootcontext=context;
        }
        @Override
        public void onReceive(Context context, Intent intent) {
            //Log.e("intent", intent.getExtras().toString());
            if(operat_status==1){
                return;
            }
            switch (intent.getAction()){
                case "msg":
                    if( intent.getBooleanExtra("isconnect",false)){
                        try{
                            operateButton.setEnabled(true);
                            connect_status.setText("已连接");
                            operateButton.setText("手动下线");
                        }catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if(!intent.getBooleanExtra("connectHNUST",false)){
                        try{
                            operateButton.setEnabled(true);
                            connect_status.setText("未连接");
                            operateButton.setText("请先连接校园无线网");
                            operateButton.setEnabled(false);
                        }catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else{
                        try{
                            operateButton.setEnabled(true);
                            connect_status.setText("未连接");
                            operateButton.setText("手动上线");
                        }catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

            }
        }
    }
}
