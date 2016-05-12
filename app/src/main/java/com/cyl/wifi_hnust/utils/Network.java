package com.cyl.wifi_hnust.utils;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.cyl.wifi_hnust.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by yonglong on 2016/5/10.
 */
public class Network extends Service {

    Context context;
    final int CONNECT = 0, DISCONNECT = 1;
    final int SECCUSSCODE = 1, ERRORCODE = 7;
    String account, password;
    PasswordHandler passwordHandler;
  //  MainActivity.BroadCast broadcast;


    public Network(Context context) {
        this.context = context;

        passwordHandler = new PasswordHandler(context);
        this.context = context;

      //  broadcast = new MainActivity().new BroadCast(this);

    }

    public IBinder onBind(Intent intent) {
        return null;
    }


    public void connect(final String account, final String password) {

        final String demo = "id_userName=405g14&userName=405g14&id_userPwd=313770&userPwd=MzEzNzcw&userDynamicPwd=&userDynamicPwdd=&serviceType=&isSavePwd=on&userurl=&userip=&basip=&language=Chinese&usermac=null&wlannasid=&wlanssid=&entrance=null&loginVerifyCode=&userDynamicPwddd=&customPageId=0&pwdMode=0&portalProxyIP=49.123.3.18&portalProxyPort=50200&dcPwdNeedEncrypt=1&assignIpType=0&appRootUrl=http%3A%2F%2F49.123.3.18%3A8080%2Fportal%2F&manualUrl=&manualUrlEncryptKey=";

        final String ID_USERNAME = "id_userName", USERNAME = "userName", USERPWD = "userPwd";
        this.account = account;
        this.password = password;
        new Thread() {
            @Override
            public void run() {
                super.run();
                String param = "id_userName=" + account +
                        "&userName=" + account +
                        "&userPwd="  +
                        passwordHandler.imc_portal_function_base64(password);

                String getresult = HttpUtils.sendPost(Constants.LOGIN_URL, param);
                if (getresult != null) {
                    getresult = passwordHandler.i_p_f_base64Decode(getresult);
                    Message message = new Message();
                    message.what = CONNECT;
                    message.getData().putString("result", getresult);
                    Log.e("test", getresult);
                    handler.sendMessage(message);
                }
            }
        }.start();

    }

    public void disconnect() {
        final String demo = "id_userName=405g14&userName=405g14&id_userPwd=313770&userPwd=MzEzNzcw&userDynamicPwd=&userDynamicPwdd=&serviceType=&isSavePwd=on&userurl=&userip=&basip=&language=Chinese&usermac=null&wlannasid=&wlanssid=&entrance=null&loginVerifyCode=&userDynamicPwddd=&customPageId=0&pwdMode=0&portalProxyIP=49.123.3.18&portalProxyPort=50200&dcPwdNeedEncrypt=1&assignIpType=0&appRootUrl=http%3A%2F%2F49.123.3.18%3A8080%2Fportal%2F&manualUrl=&manualUrlEncryptKey=";

        final String ID_USERNAME = "id_userName", USERNAME = "userName", USERPWD = "userPwd";
        new Thread() {
            @Override
            public void run() {
                super.run();

                String param = "";

                String getresult = HttpUtils.sendPost(Constants.LOGIN_URL, param);
                if (getresult != null) {
                    getresult = passwordHandler.i_p_f_base64Decode(getresult);
                    Message message = new Message();
                    message.what = DISCONNECT;
                    message.getData().putString("result", getresult);
                    Log.e("test", getresult);
                    handler.sendMessage(message);
                }
            }
        }.start();
    }
    String[] splitreason =null;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String result;
            switch (msg.what) {
                case CONNECT:
                    result = msg.getData().getString("result");
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String code = jsonObject.optString("errorNumber");
                        if (code.equals("1")) {
                            dialog(true, "上线成功！");
                        } else {
                            String reason = jsonObject.optString("portServIncludeFailedReason");
                            splitreason = reason.split("\\:");
                            dialog(false, splitreason[1]);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case DISCONNECT:
                    result = msg.getData().getString("result");
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String code = jsonObject.optString("errorNumber");
                        if (code.equals("1")) {

                            dialog(true, "下线成功！");
                        } else {
                            String reason = jsonObject.optString("portServErrorCodeDesc");
                            splitreason = reason.split("\\:");
                            dialog(false, splitreason[1]);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };

    private void dialog(boolean success, String content) {
        try {
            if (success) {
                new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("信息")
                        .setContentText(content)
                        .setConfirmText("确定")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.cancel();
                            }
                        }).show();
            } else {
                new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("注意！")
                        .setContentText(content)
                        .setConfirmText("确定")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.cancel();
                            }
                        }).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

