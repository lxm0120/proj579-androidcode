package com.example.monster.proje579code;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.facebook.AccessTokenTracker;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;


import com.facebook.AccessToken;
import com.facebook.GraphRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import static java.nio.file.Paths.get;

/**
 * Created by usama on 4/12/18.
 */

public class photosactivity extends AppCompatActivity {
    int numphotos = 100;
    Photo[] photos;
    Photo[] solophotos   = new Photo[numphotos];
    int soloind =0;
    AccessToken accessToken;
    AccessTokenTracker accessTokenTracker;
    int photindex = 0;
    PhotosAdapter photosAdapter;
    GridView gridView;
    private String userid = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_photos);
        gridView = (GridView) findViewById(R.id.gridview);

        userid=getIntent().getStringExtra("userid");
        String photoUrls = getIntent().getStringExtra("response");

        //accessToken =(AccessToken) getIntent().getStringExtra("token");
        accessToken = AccessToken.getCurrentAccessToken();
        Log.e("TOEKN!!!222!",accessToken.toString());
        JSONArray photoids = null;

       // try {
        try {
            photoids =new JSONArray(photoUrls);
            numphotos = photoids.length();
            Log.e("HANJEE!",photoids.toString());
            Log.e("LENGTH = ",String.valueOf( photoids.length()));
            photos = new Photo[numphotos];
            for (int i=0;i<photoids.length();i++) {
                String source = photoids.getJSONObject(i).getString("id");
                Log.e("DDSIR", "/" + source + "/picture");

                Bundle params = new Bundle();
                params.putBoolean("redirect", false);
                //params.putString("fields","data,paging");
                new GraphRequest(accessToken, "/"+source+"/picture", params, HttpMethod.GET, new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {

                        Log.e("DDSSSSSS", "tyring_+ for photos");

                        Log.e("DD!!!", response.toString());
                        Log.e("DD!!!",response.getJSONObject().toString());
                        try {
                            JSONObject photourldata = response.getJSONObject().getJSONObject("data");
                            String photourl = photourldata.getString("url");
                            Log.e("DD!!!URL",photourl);
                            Photo photobj = new Photo(photourl);
                            //Log.e("DDSSS22S", "photolength before = "+);
                            photos[photindex]=photobj;
                            photindex++;
                            Log.e("DDSSS22S", "photoindex = "+String.valueOf(photindex) + "photolength = " + photos.length );

                            if ( photindex == photos.length ){
                                Log.e("DDSSSSSS", "FINALLY MET ");
                                photosAdapter = new PhotosAdapter(photosactivity.this, photos);
                                gridView.setAdapter(photosAdapter);

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }).executeAsync();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.e("EE","EKKJFKSAJFKSAJKF");



        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                Photo photo = photos[position];
                photo.toggleFavorite();
                Log.e("CLICKED",String.valueOf( solophotos.length));
                //Photo photobj = new Photo(pos);
                //Log.e("DDSSS22S", "photolength before = "+);
                //photos[photindex]=photobj;
                solophotos[soloind] =photos[position];
                soloind++;
                Log.e("AFTERCLICKED",String.valueOf( solophotos.length)+"solind = "+soloind);
               upload(userid,solophotos[soloind-1].getImageUrl());
               //Log.e("aaaaaaaa",solophotos[soloind].getImageUrl());
                for(int i=soloind-1;i<soloind;i++){
                    Log.e("AFTER",solophotos[i].getImageUrl());
                }


                // This tells the GridView to redraw itself
                // in turn calling your BooksAdapter's getView method again for each cell
                photosAdapter.notifyDataSetChanged();
            }
        });
    }
    protected void upload(final String userid,final String  imageurl) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                boolean b=false;
                Log.e("IIMMb",String.valueOf(b));
                while(!b) {
                    b = userid!=null  && imageurl!=null;
                    Log.e("IIMMb",String.valueOf(b));

                    if (b){
                        Log.d("IIMMparam", userid + ","  + imageurl);
                        ImagePost.executeImagePost(userid,imageurl);
                        b = false;
                        break;
                    }
                }
            }
        }).start();

    }
    @Override
    protected void onPause() {
        super.onPause();
        Intent intentt = new Intent();
        intentt.putExtra("photos", solophotos.toString());
        setResult(photosactivity.RESULT_OK, intentt);
        finishActivity(233);
        Log.e("Pausing","---");


    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("stoping","---");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("Destroying","---");


        // Don't forget to unregister the ACTION_FOUND receiver.
        //       unregisterReceiver(mReceiver);
    }




}