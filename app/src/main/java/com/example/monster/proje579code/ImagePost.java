package com.example.monster.proje579code;

/**
 * Created by lxm on 2018/4/21.
 */

import android.graphics.Bitmap;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import android.util.Base64;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by lxm on 2018/4/19.
 */

public class ImagePost {
    //    private static String IP = "192.168.137.1:9090";
     private static String IP = "68.181.191.82:9090";
    // GET method to send message to the server
    public static String executeImagePost(String userid, String imurl) {

        HttpURLConnection conn = null;
        InputStream is = null;
        JSONObject requestParam=new JSONObject();
        try {
            requestParam.put("userid", userid);
            requestParam.put("imageurl", imurl);
            Log.e("IIMMuserid", userid);
        }catch(JSONException e)
        {
            Log.e("JSON","error");
        }
        try {

            String path = "http://" + IP + "/EE579backend/Imagelet";
            Log.d("IIMMpath",path);
            conn = (HttpURLConnection) new URL(path).openConnection();
            conn.setConnectTimeout(5000); // set timeout
            conn.setReadTimeout(5000);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST"); // POST method
            conn.setRequestProperty("Content-Type", "application/json;charset=utf-8"); // JSON format
            DataOutputStream out = new DataOutputStream(conn.getOutputStream());
            out.writeBytes(requestParam.toString());
            out.flush();

            int i=conn.getResponseCode();
            Log.d("IIMMresponse",i+"");
            if (conn.getResponseCode() == 200) {

                is = conn.getInputStream();
                return parseInfo(is);
            }

        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            // close the connection if exit accidentally
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
    public static String executePicturePost(String userid, Bitmap picture) {

        HttpURLConnection conn = null;
        InputStream is = null;
        JSONObject requestParam=new JSONObject();
        String picturestring=bitmapToBase64(picture);
        picturestring= picturestring.replace("\\+","%2B");
        try {
            requestParam.put("userid", userid);
            requestParam.put("picture", picturestring);
            Log.e("PPMMuserid", userid);
            Log.e("PPMMpicture",picturestring);
        }catch(JSONException e)
        {
            Log.e("JSON","error");
        }
        try {

            String path = "http://" + IP + "/EE579backend/Picturelet";
            Log.d("IIMMpath",path);
            conn = (HttpURLConnection) new URL(path).openConnection();
            conn.setConnectTimeout(30000); // timeout
            conn.setReadTimeout(30000);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST"); // POST method
            conn.setRequestProperty("Content-Type", "application/json;charset=utf-8"); // JSON
            DataOutputStream out = new DataOutputStream(conn.getOutputStream());
            out.writeBytes(requestParam.toString());
            out.flush();

            int i=conn.getResponseCode();
            Log.d("IIMMresponse",i+"");
            if (conn.getResponseCode() == 200) {

                is = conn.getInputStream();
                String iss=parseInfo(is);
                Log.e("isssssss",iss);
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
    // convert inputstream into String
    private static String parseInfo(InputStream inStream) throws Exception {
        byte[] data = read(inStream);
        return new String(data, "UTF-8");
    }

    // convert inputstream into Byte
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
    public static String bitmapToBase64(Bitmap bitmap) {

        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                //Compress the image
                bitmap.compress(Bitmap.CompressFormat.JPEG, 60, baos);
                baos.flush();
                baos.close();
                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes,Base64.NO_WRAP);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }


}
