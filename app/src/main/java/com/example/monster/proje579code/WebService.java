package com.example.monster.proje579code;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by lxm on 2018/4/19.
 */

public class WebService {
    //  private static String IP = "192.168.137.1:9090";
     private static String IP = "68.181.191.82:9090";
    public static String executeHttpGet(String userid, String macaddress) {

        HttpURLConnection conn = null;
        InputStream is = null;

        try {

            String path = "http://" + IP + "/EE579backend/Searchlet";
            path = path + "?userid=" + userid + "&macaddress=" + macaddress;
            Log.d("path",path);
            conn = (HttpURLConnection) new URL(path).openConnection();
            conn.setConnectTimeout(3000);
            conn.setReadTimeout(3000);
            conn.setDoInput(true);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Charset", "UTF-8");
            int i=conn.getResponseCode();
            Log.d("response",i+"");
            if (conn.getResponseCode() == 200) {

                is = conn.getInputStream();
                String iss=parseInfo(is);
                Log.e("parse",iss);
//                is.close();
                return iss;
            }

        }catch (Exception e) {
            e.printStackTrace();
        } finally {

            if (conn != null) {
                conn.disconnect();
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return null;
    }


    private static String parseInfo(InputStream inStream) throws Exception {
        byte[] data = read(inStream);
        return new String(data, "UTF-8");
    }

    public static byte[] read(InputStream inStream) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[10*1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, len);
        }
 //       inStream.close();
        return outputStream.toByteArray();
    }
}
