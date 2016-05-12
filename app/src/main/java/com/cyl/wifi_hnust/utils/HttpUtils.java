package com.cyl.wifi_hnust.utils;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;

/**
 * Created by yonglong on 2016/5/10.
 */
public class HttpUtils {

    public static String sendPost(String url, String params) {
        URL realurl = null;
        InputStream in = null;
        HttpURLConnection conn = null;
        try {
            realurl = new URL(url);
            conn = (HttpURLConnection) realurl.openConnection();
            conn.setRequestProperty("Content-type", "application/x-www-form-urlencoded; charset=UTF-8");
            conn.setRequestProperty("Accept", "Accept:text/plain, */*; q=0.01");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.80 Safari/537.36");
            conn.setRequestProperty("X-Requested-With", "XMLHttpRequest");
            conn.setRequestProperty("Accept-Encoding", "gzip, deflate");
            conn.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
            conn.setRequestProperty("Cache-Control", "no-cache");
            conn.setRequestProperty("Connection", "keep-alive");
            conn.setRequestProperty("Host", "49.123.3.18:8080");
            conn.setRequestProperty("Origin", "http://49.123.3.18:8080");
            conn.setRequestProperty("Pragma", "no-cache");
            conn.setRequestProperty("Referer", "http://49.123.3.18:8080/portal/templatePage/20150309114340294/login_custom.jsp");

            conn.setDoOutput(true);

            conn.setRequestMethod("POST");
            DataOutputStream out = new DataOutputStream(conn
                    .getOutputStream());
            out.writeBytes(params);

            out.flush();
            out.close(); // flush and close

            if (conn.getResponseCode() == 200) {
                in = conn.getInputStream();
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
            Log.e("不知名主机", "dasd");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (in != null)
            return readInputStream(in);
        else return null;
    }

    public static String sendGet(String path) {
        String result = null;
        try {
            //创建URL实例
            URL url = new URL(path);

            //获取HettpConnection对象
            HttpURLConnection conn = null;
            conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            int code = conn.getResponseCode();
            if (code == 200) {
                InputStream is = conn.getInputStream();
                result = readInputStream(is);
            }

        } catch (UnknownHostException e) {
            e.printStackTrace();
            Log.e("不知名主机", "UnknownHostException");
        } catch (SocketTimeoutException e) {
            Log.e("BackgroundTask", "oh,no,catch了，SocketTimeoutException");
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("BackgroundTask", "oh,no,catch了，无法连接到互联网");
        }
        return result;
    }

    public static String readInputStream(InputStream is) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int len = 0;
            byte[] buffer = new byte[1024];
            while ((len = is.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            is.close();
            baos.close();
            byte[] result = baos.toByteArray();
            //试着解析result中的字符串
            String temp = new String(result);
            return temp;
        } catch (Exception e) {
            e.printStackTrace();
            return "获取失败";
        }

    }
}
