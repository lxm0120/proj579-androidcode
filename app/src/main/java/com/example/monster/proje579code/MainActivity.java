package com.example.monster.proje579code;

import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.HttpMethod;
import com.facebook.LoggingBehavior;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.GraphResponse;
import com.facebook.GraphRequest;
import com.squareup.picasso.Picasso;
//sobrxqjskl_1522705093@tfbnw.net

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "blApp";
    private static int flag = 0;
    private JSONObject jsonObj;
    private final int REQUEST_ENABLE_BT = 1;
    private Button scanbutton;
    public static BluetoothAdapter mBluetoothAdapter;
    private Handler mHandler;
    private TextView textView;
    private static final String EMAIL = "email";
    private static int logged_in =0;
    public String USER_ID=null;
    private String MAC_ADD=null;
    private String USER_NAME=null;
    private String IMAGE_URL=null;
    private String FRIENDS=null;
    static final String STATE_LOGIN ="0";
    AccessTokenTracker accessTokenTracker;
    AccessToken accessToken;
    CallbackManager callbackManager;
    LoginButton loginButton;
    TextView FacebookDataTextView;
    ImageView imageViewprofile;
    private static String fbid="";
    Button findfriendbutton,selectphotosbutton,takepicbutton,fb_bind_button;
    private String photosurl;
    public static final String EXTRA_MESSAGE = "com.example.monster.proj579code.MESSAGE";
    String id_1;
    JSONArray data;
    JSONObject jsonObject;
    int friends_num;
    Button fb_logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FacebookSdk.sdkInitialize(MainActivity.this);
        FacebookSdk.setIsDebugEnabled(true);
        FacebookSdk.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);

        callbackManager = CallbackManager.Factory.create();

        FacebookDataTextView = (TextView)findViewById(R.id.TextView1);
        loginButton = (LoginButton) findViewById(R.id.login_button);
        fb_bind_button = findViewById(R.id.fb_bind);
        findfriendbutton = findViewById(R.id.butseefriend);
        selectphotosbutton = findViewById(R.id.selectphotos);
        takepicbutton=findViewById(R.id.takepicbtn);
        imageViewprofile=findViewById(R.id.imageview);
        fb_logout = findViewById(R.id.fb_bind2);

        loginButton.setReadPermissions(Arrays.asList(EMAIL,"user_photos","public_profile","user_friends"));

        if(AccessToken.getCurrentAccessToken()!=null){
            GraphLoginRequest(AccessToken.getCurrentAccessToken());
            fb_bind_button.setVisibility(View.GONE);
            fb_logout.setVisibility(View.VISIBLE);
            flag = 1;
            findfriendbutton.setVisibility(View.VISIBLE);
            selectphotosbutton.setVisibility(View.VISIBLE);
            takepicbutton.setVisibility(View.VISIBLE);
            //Toast.makeText(MainActivity.this,"Already logged in",Toast.LENGTH_SHORT).show();

        }else {

            // If not login in then show the Toast.
            fb_bind_button.setText("Log In");
            //Toast.makeText(MainActivity.this,"User not logged in",Toast.LENGTH_SHORT).show();
        }
        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                Log.e("DD","**************************");
                findfriendbutton.setVisibility(View.VISIBLE); //To set visible
                selectphotosbutton.setVisibility(View.VISIBLE); //To set visible
                takepicbutton.setVisibility(View.VISIBLE);
                fb_bind_button.setVisibility(View.GONE);
                fb_logout.setVisibility(View.VISIBLE);
                flag = 1;
                Log.e("DD",loginResult.getAccessToken().toString());
                Log.e("DD",AccessToken.getCurrentAccessToken().toString());
                GraphLoginRequest(loginResult.getAccessToken());
               // GraphFriendRequest(loginResult.getAccessToken());
                Log.e("DD:",loginResult.toString());
                getBluetoothMacAddress();


            }


            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });
        // Detect user is login or not. If logout then clear the TextView and delete all the user info from TextView.
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken accessToken, AccessToken accessToken2) {
                if (accessToken2 == null) {

                    // Clear the TextView after logout.
                    Log.e("DD","goneeeeeeeeeeeeeeeeeeeeeeeee2222");
                    FacebookDataTextView.setText(null);
                    imageViewprofile.setImageDrawable(null);
                    findfriendbutton.setVisibility(View.GONE);
                    selectphotosbutton.setVisibility(View.GONE);
                    takepicbutton.setVisibility(View.GONE);
                    imageViewprofile.setImageResource(R.drawable.ic_face_black_48dp);
                    fb_bind_button.setVisibility(View.VISIBLE);
                    fb_logout.setVisibility(View.GONE);
                    flag = 0;

                }
            }
        };


      //  mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
      //  if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
      //      Intent enableBtlntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
      //      startActivityForResult(enableBtlntent, REQUEST_ENABLE_BT);
      //  }


    }
    protected void onConn(final String userid,final String macaddress,final String username,final String  imageurl,final String friend) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int i =0 ;
                boolean b=false;
                Log.e("bmain",String.valueOf(b));
                while(!b) {
                    b = userid!=null && macaddress!= null&& username!=null && imageurl!=null;
                    Log.e("bmain",String.valueOf(b));

                    if (b){
                        Log.d("param", userid + "," + macaddress + "," + username + "," + imageurl);
                        WebServicePost.executeHttpPost(userid, macaddress, username, imageurl,friend);
                        b = false;
                        break;
                    }
                    if(i>20)
                    {break;}
                    i++;
                }
            }
        }).start();

    }
    // Method to access Facebook User Data.
    protected void GraphLoginRequest(final AccessToken accessToken){
        GraphRequest graphRequest = GraphRequest.newMeRequest(accessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject jsonObject, GraphResponse graphResponse) {

                        try {
                            Log.e("DD","trying ------------------------");
                            Log.e("DD",graphResponse.toString());
                            Log.e("DD",jsonObject.getString("id"));
                            Log.e("DD", jsonObject.getString("first_name"));
                            Log.e("DD", jsonObject.toString());
                            USER_ID=jsonObject.getString("id");
                            USER_NAME=jsonObject.getString("name");
                            IMAGE_URL="https://graph.facebook.com/"+USER_ID+"/picture?type=large";
                            fbid = jsonObject.getString("id");
                            Picasso.with(MainActivity.this).load("https://graph.facebook.com/" + jsonObject.getString("id")+ "/picture?type=large").into(imageViewprofile);
                            FacebookDataTextView.setText("Welcome,  "+jsonObject.getString("first_name")+"!");


                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }

                        GraphFriendRequest(accessToken);
                        GetAlbums(accessToken);
                        onConn(USER_ID,MAC_ADD,USER_NAME,IMAGE_URL,FRIENDS);
                    }
                });

        Bundle bundle = new Bundle();
        bundle.putString(
                "fields",
                "id,name,link,email,gender,last_name,first_name,locale,timezone,updated_time,verified"
        );
        graphRequest.setParameters(bundle);
        graphRequest.executeAsync();
        GraphFriendRequest(accessToken);
    }





    protected void GraphFriendRequest(AccessToken accessToken){

        GraphRequest request = GraphRequest.newGraphPathRequest(
                accessToken,
                "/me/friends?fields=id",
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        Log.e("DD","tyring_+_+_+_+_+_+_+_+");
                        Log.e("DD", response.toString());
                        JSONArray data =new JSONArray();
                        try {
                            data = new JSONObject(response.getRawResponse()).getJSONArray("data");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        for(int i=0; i<data.length();i++){
                            try {
                                if(i==0){
                                    FRIENDS=data.getJSONObject(i).getString("id");
                                    continue;
                                }
                                FRIENDS = FRIENDS+"-"+data.getJSONObject(i).getString("id");
                            }catch(JSONException e){
                                e.printStackTrace();
                            }
                        }
                        if(FRIENDS!=null){
                            Log.e("friends",FRIENDS);
                        }
                    }
                });


        Bundle bundle = new Bundle();
        bundle.putString(
                "fields",
                "data"
        );
        request.setParameters(bundle);
        request.executeAsync();
    }

    public void seeFriends(View view) {
        Intent intent = new Intent(this, SeeFriendsActivity.class);
        //EditText editText = (EditText) findViewById(R.id.editText);
        //String message = editText.getText().toString();
        //intent.putExtra(EXTRA_MESSAGE, message);
        intent.putExtra("userid",USER_ID);
        startActivity(intent);
    }
    public void takePic(View view) {
        Intent intent = new Intent(this, TakepicActivity.class);
        //EditText editText = (EditText) findViewById(R.id.editText);
        //String message = editText.getText().toString();
        //intent.putExtra(EXTRA_MESSAGE, message);
        intent.putExtra("userid",USER_ID);
        startActivity(intent);
    }
    public boolean selectPhotos(View view) {
        Intent intent = new Intent(MainActivity.this,  photosactivity.class);
        //EditText editText = (EditText) findViewById(R.id.editText);
        //String message = editText.getText().toString();
        intent.putExtra("response", photosurl);
        intent.putExtra("userid",USER_ID);
        Log.d(TAG,"jw:"+photosurl);
        Log.e("TOEKN!!!1111!",AccessToken.getCurrentAccessToken().toString());
        //startActivity(intent);
        this.startActivityForResult(intent, 233);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        Log.e("BACK!!!!23","122345");
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("BACK!!!!23","12234 code is " + resultCode+"result code = "+RESULT_OK+"requestCode="+requestCode);

        if (requestCode == 1) {
            Log.e("BACK!!!!23","1223 code is " + resultCode+"result code = "+RESULT_OK);
            if(resultCode == RESULT_OK) {
                String photourls = data.getStringExtra("photos");
                Log.e("BACK!!",photourls.toString());
            }
        }
        callbackManager.onActivityResult(requestCode, resultCode, data);

        }


    protected void GetPhotos(String albumid, final AccessToken accessToken){
        final Bundle parameters = new Bundle();
        parameters.putString("fields", "data,paging");
        GraphRequest request = new GraphRequest(
                accessToken,
                "/" + albumid + "/photos?limit=100", parameters,HttpMethod.GET,new GraphRequest.Callback() {
            @Override
            public void onCompleted(GraphResponse response) {
                JSONArray data;
                try {
                    data = response.getJSONObject().getJSONArray("data");
                    Log.e("GOTT", data.toString() );
                    Log.e("GOTT",String.valueOf(data.length()));
                    Log.e("GOTT", "previosu photourl ="+ photosurl);
                    if (data.length() > 0){
                        if ( photosurl == null )
                            photosurl = data.toString();

                        else{
                            String tempphotourl = photosurl;
                            String tempdatastr = data.toString();
                            String s3=new String("");
                            tempphotourl=tempphotourl.substring(tempphotourl.indexOf("[")+1, tempphotourl.lastIndexOf("]"));
                            tempdatastr=tempdatastr.substring(tempdatastr.indexOf("[")+1, tempdatastr.lastIndexOf("]"));
                            photosurl="["+tempphotourl+","+tempdatastr+"]";
                        }}
                    Log.e("GOTT", "new photourl ="+ photosurl);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        request.executeAsync();

    }
    protected void GetAlbums(final AccessToken accessToken){
        final Bundle parameters = new Bundle();
        parameters.putString("fields", "data,paging");
        GraphRequest request = new GraphRequest(
                accessToken,
                "/" + fbid + "/albums", parameters,HttpMethod.GET,new GraphRequest.Callback() {
            @Override
            public void onCompleted(GraphResponse response) {
                JSONArray data;
                try {
                    data = response.getJSONObject().getJSONArray("data");
                    Log.e("ALB", data.toString() );
                    Log.e("ALB",String.valueOf(data.length()));
                    for (int i=0;i<data.length();i++){
                        String albumid = data.getJSONObject(i).getString("id");
                        Log.e("ALB",albumid);
                        GetPhotos(albumid, accessToken);
                    }
                    // photosurl = data.toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        request.executeAsync();

    }




    private void getBluetoothMacAddress()
    {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        String bluetoothMacAddress = "";

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M){
            try {

                Field mServiceField = bluetoothAdapter.getClass().getDeclaredField("mService");
                mServiceField.setAccessible(true);

                Object btManagerService = mServiceField.get(bluetoothAdapter);

                if (btManagerService != null) {
                    bluetoothMacAddress = (String) btManagerService.getClass().getMethod("getAddress").invoke(btManagerService);
                }
            } catch (NoSuchFieldException e) {
                Log.e("Not working","catch 1");

            } catch (NoSuchMethodException e) {
                Log.e("Not working","catch 2");

            } catch (IllegalAccessException e) {
                Log.e("Not working","catch 3");
            } catch (InvocationTargetException e) {

                Log.e("Not working","catch 4");
            }
        } else {

            bluetoothMacAddress = bluetoothAdapter.getAddress();

        }
        Log.d("BT ",bluetoothMacAddress);
        MAC_ADD=bluetoothMacAddress;
    }

    @Override
    protected void onResume() {
        super.onResume();
        //findfriendbutton.setVisibility(View.VISIBLE);
    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state
        savedInstanceState.putInt(STATE_LOGIN, flag);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Don't forget to unregister the ACTION_FOUND receiver.
 //       unregisterReceiver(mReceiver);
    }


    //setting login, logout button and AlertDialog
    public void judge(View view) {
        if (flag == 0){
            showdialog();
        }
        else if (flag == 1){
            com.facebook.login.widget.LoginButton btn = new com.facebook.login.widget.LoginButton(MainActivity.this);
            btn.performClick();
        }
    }

    public void showdialog() {
        AlertDialog.Builder alertdialogbuilder = new AlertDialog.Builder(this);
        alertdialogbuilder.setMessage("This app wants to access your Facebook account.");
        alertdialogbuilder.setPositiveButton("ALLOW", click1);
        alertdialogbuilder.setNegativeButton("DENY", click2);
        AlertDialog alertDialog1 = alertdialogbuilder.create();
        alertDialog1.show();
    }

    private DialogInterface.OnClickListener click1 = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface arg0, int arg1) {
            com.facebook.login.widget.LoginButton btn = new com.facebook.login.widget.LoginButton(MainActivity.this);
            btn.performClick();
        }
    };

    private DialogInterface.OnClickListener click2 = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface arg0, int arg1) {
            arg0.cancel();
        }
    };


    public void logout(View view) {
        com.facebook.login.widget.LoginButton btn = new com.facebook.login.widget.LoginButton(MainActivity.this);
        btn.performClick();
    }
}
