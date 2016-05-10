package com.cyl.wifi_hnust.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by yonglong on 2016/5/10.
 */
public class HttpUtils {

    public InputStream sendPost(String url, String params)
    {
        URL realurl = null;
        InputStream in = null;
        HttpURLConnection conn = null;
        try{
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
            PrintWriter pw = new PrintWriter(conn.getOutputStream());
            pw.print(params);
            pw.flush();
            pw.close();
            in = conn.getInputStream();
        }catch(MalformedURLException e){
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return in;
    }
}
