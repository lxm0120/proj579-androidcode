package com.example.monster.proje579code;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by lxm on 2018/4/19.
 */

public class WebServicePost {
    //    private static String IP = "192.168.137.1:9090";
    private static String IP = "68.181.191.82:9090";
    public static String executeHttpPost(String userid, String macaddress,String username,String image_url,String friends) {

        HttpURLConnection conn = null;
        InputStream is = null;
        JSONObject requestParam=new JSONObject();
        try {
            requestParam.put("userid", userid);
            requestParam.put("macaddress", macaddress);
            requestParam.put("username", username);
            requestParam.put("imageurl", image_url);
            requestParam.put("friends",friends);
        }catch(JSONException e)
        {
            Log.e("JSON","error");
        }
        try {
            String path = "http://" + IP + "/EE579backend/Reglet";
            Log.d("path",path);
            conn = (HttpURLConnection) new URL(path).openConnection();
            conn.setConnectTimeout(3000);
            conn.setReadTimeout(3000);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            DataOutputStream out = new DataOutputStream(conn.getOutputStream());
            out.writeBytes(requestParam.toString());
            out.flush();

            int i=conn.getResponseCode();
            Log.d("response",i+"");
            if (conn.getResponseCode() == 200) {

                is = conn.getInputStream();
                return parseInfo(is);
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
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, len);
        }
        inStream.close();
        return outputStream.toByteArray();
    }
}
