package com.cyl.wifi_hnust.utils;

import android.content.Context;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Created by yonglong on 2016/5/10.
 */
public class PasswordHandler {
    Context context;
    public PasswordHandler(Context context){
        this.context=context;
    }

    public String imc_portal_function_base64(String input) {


        final String keyStr = "ABCDEFGHIJKLMNOP" +
                "QRSTUVWXYZabcdef" +
                "ghijklmnopqrstuv" +
                "wxyz0123456789+/" +
                "=";
        String output = "";
        int chr1, chr2, chr3, enc1, enc2, enc3, enc4;
        int i = 0;
        while(i<input.length()-2)
        {
            chr1 = input.codePointAt(i++);
            chr2 = input.codePointAt(i++);
            chr3 = input.codePointAt(i++);

            enc1 = chr1 >> 2;
            enc2 = ((chr1 & 3) << 4) | (chr2 >> 4);
            enc3 = ((chr2 & 15) << 2) | (chr3 >> 6);
            enc4 = chr3 & 63;

            if (isNaN(chr2)) {
                enc3 = enc4 = 64;
            } else if (isNaN(chr3)) {
                enc4 = 64;
            }
            output = output + keyStr.charAt(enc1) + keyStr.charAt(enc2) + keyStr.charAt(enc3) + keyStr.charAt(enc4);
        }

        return output;
    }

    public String i_p_f_base64Decode(String str) {
        final int[] base64DecodeChars = {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, -1, 63,
                52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14,
                15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40,
                41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1, -1, -1};
        int len, c1 = 0, c2=0, c3=0, c4=0;
        int i = 0;
        String out = "";
        len = str.length();
        while (i < len) {
        /* c1 */
            do {
                if(i<len) {
                    c1 = base64DecodeChars[str.codePointAt(i++) & 0xff];
                }else {
                    break;
                }
            } while (i < len && c1 == -1);
            if (c1 == -1)
                break;

        /* c2 */
            do {
                if(i<len) {
                    c2 = base64DecodeChars[str.codePointAt(i++) & 0xff];
                }else{
                    break;
                }
            } while (i < len && c2 == -1);
            if (c2 == -1)
                break;
            out += String.valueOf((char) ((c1 << 2) | ((c2 & 0x30) >> 4)));

        /* c3 */
            do {
                if(i<len) {
                    c3 = str.codePointAt(i++) & 0xff;
                }else{
                    break;
                }
                if (c3 == 61)
                    return out;
                c3 = base64DecodeChars[c3];
            } while (i < len && c3 == -1);
            if (c3 == -1)
                break;

            //ut += String.fromCharCode(((c2 & 0XF) << 4) | ((c3 & 0x3C) >> 2));
            out += String.valueOf((char) (((c2 & 0XF) << 4) | ((c3 & 0x3C) >> 2)));
        /* c4 */
            do {
                if(i<len) {
                    c4 = str.codePointAt(i++) & 0xff;
                }else{
                    break;
                }
                if (c4 == 61)
                    return out;
                c4 = base64DecodeChars[c4];
            } while (i < len && c4 == -1);
            if (c4 == -1)
                break;
            //t += String.fromCharCode(((c3 & 0x03) << 6) | c4);
            out += String.valueOf((char) (((c3 & 0x03) << 6) | c4));
        }
        if(out.charAt(out.length()-1)=='%'){
            out=out.substring(0,out.length()-1);
        }
        String final_result="error";
        Log.d("out", out);
        try {
            final_result= URLDecoder.decode(out, "UTF-8");
        } catch (UnsupportedEncodingException | IllegalArgumentException e) {
            e.printStackTrace();
        }
        Log.d("out",final_result);
        return final_result;

    }
    private boolean isNaN(int value){
        if(value>=30&&value<=39){
            return true;
        }else{
            return false;
        }
    }
}
