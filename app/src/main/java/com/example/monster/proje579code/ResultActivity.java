package com.example.monster.proje579code;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lxm on 2018/4/22.
 */

public class ResultActivity extends AppCompatActivity{

    JSONObject obj;
    String a,b,c;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_display);
        String result = getIntent().getStringExtra("result");
        try {
            obj = new JSONObject(result);
            Log.e("eeeee",result);
            a = obj.getString("userid");
            b = obj.getString("username");
            c = obj.getString("relation");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ImageView Iv = findViewById(R.id.profilepic);
        TextView Tv = findViewById(R.id.profilename);
        Picasso.with(ResultActivity.this).load("https://graph.facebook.com/" + a+ "/picture?type=large").into(Iv);
        Tv.setText(b+"\n"+c);

    }

}
