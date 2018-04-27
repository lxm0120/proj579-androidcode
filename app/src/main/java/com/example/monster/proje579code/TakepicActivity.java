package com.example.monster.proje579code;


/**
 * Created by lxm on 2018/4/22.
 */
import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;


public class TakepicActivity extends AppCompatActivity{
    ImageButton btnpic;
    Button btnresult;
    ImageView imgTakenPic;
    String userid = null;
    String[] othersid=null;
    String[]username=null;
    String[]imageurl=null;
    String[]relation=null;
    private File output;
    private Uri imageUri;
    private static final int CROP_PHOTO = 2;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 6;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE2 = 7;
    private static Bitmap bitmap;
    String result = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.take_pic);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        userid = getIntent().getStringExtra("userid");
        btnpic = (ImageButton) findViewById(R.id.takepho);
        btnresult = (Button) findViewById(R.id.getresult);
        imgTakenPic = (ImageView)findViewById(R.id.imageView);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode!=RESULT_CANCELED)
        {
            if(requestCode == CROP_PHOTO){

                try {
                    bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                    imgTakenPic.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                btnresult.setVisibility(View.VISIBLE);
                btnresult.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick (View view){
                        PostPicture(userid,bitmap);


                    }
                });
            }
        }
    }

    protected void PostPicture(final String userid, final Bitmap bitmap){
        new Thread(new Runnable() {
            @Override
            public void run() {
                result=ImagePost.executePicturePost(userid,bitmap);
                Log.e("faceresult",result);
                Intent intent = new Intent(TakepicActivity.this, ResultActivity.class);
                intent.putExtra("userid",userid);
                intent.putExtra("username",username);
                intent.putExtra("othersud",othersid);
                intent.putExtra("imageurl",imageurl);
                intent.putExtra("relation",relation);
                intent.putExtra("result",result);
                startActivity(intent);
            }
        }).start();

    }

    public void takePhone(View view){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_CALL_PHONE2);
        }else {
            takePhoto();
        }
    }






    void takePhoto(){
        File file=new File(Environment.getExternalStorageDirectory(),"Photos");
        if(!file.exists()){
            file.mkdir();
        }
        output=new File(file,System.currentTimeMillis()+".jpg");
        try {
            if (output.exists()) {
                output.delete();
            }
            output.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
        imageUri = Uri.fromFile(output);
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        Intent intent2 = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, CROP_PHOTO);
        intent2.setData(imageUri);
        this.sendBroadcast(intent2);
    }










    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        if (requestCode == MY_PERMISSIONS_REQUEST_CALL_PHONE)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                takePhoto();
            } else
            {
                Toast.makeText(TakepicActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}

