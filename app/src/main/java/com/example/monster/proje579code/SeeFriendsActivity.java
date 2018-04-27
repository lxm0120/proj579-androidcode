package com.example.monster.proje579code;

        import android.Manifest;
        import android.bluetooth.BluetoothAdapter;
        import android.bluetooth.BluetoothDevice;
        import android.content.BroadcastReceiver;
        import android.content.Context;
        import android.content.Intent;
        import android.content.IntentFilter;
        import android.os.Build;
        import android.os.Handler;
        import android.os.Looper;
        import android.os.Message;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.View;
        import android.widget.Button;
        import android.widget.ImageView;
        import android.widget.ListView;
        import android.widget.SimpleAdapter;
        import android.widget.TextView;

        import com.squareup.picasso.Picasso;

        import org.json.JSONException;
        import org.json.JSONObject;

        import java.util.ArrayList;
        import java.util.HashMap;
        import java.util.List;
        import java.util.Map;


public class SeeFriendsActivity extends AppCompatActivity{
    private static final String TAG = "SeeFriendsActivity";
    BluetoothAdapter mBluetoothAdapter;
    public ArrayList<BluetoothDevice> mBTDevices = new ArrayList<>();
    ListView lvNewDevices;
    String userid;
    private Handler handler1;
    private Handler handler2;
    String result=null;
    int flag = 0;
    private SimpleAdapter simpleAdapter;
    private List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
    JSONObject obj;
    String a,b,c;


    private final BroadcastReceiver mReceiver3 = new BroadcastReceiver(){
        public void onReceive (Context context,Intent intent){
            String action = intent . getAction ();
            if (BluetoothDevice.ACTION_FOUND.equals (action)){
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                mBTDevices.add(device);
                Log.d(TAG,"onDevices: " + device.getName() + ": " + device.getAddress());
                String mac=device.getAddress();
                Log.e("macmac",mac);
                new MyThread().start();
                if (handler1 !=null){
                    Message msg = handler1.obtainMessage(1,mac);
                    handler1.sendMessage(msg);
                }
                if (result!=null){
                    Log.e("result",result);
                    ///////////print
                    result=null;
                }
            }
        }
    };


    @Override
    protected void onDestroy() {
        Log.d(TAG,"onDestroy: called");
        super.onDestroy();
        if (flag ==1){
        unregisterReceiver(mReceiver3);
        flag = 0;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_friends);


        mBTDevices = new ArrayList<>();
   //     userid=getIntent().getStringExtra("userid");
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();



    }



    public void btnDiscover(View view) {
        Log.d(TAG,"btnDiscover: Looking for unpaired devices");
        flag = 1;
        if (mBluetoothAdapter.isDiscovering()){
            mBluetoothAdapter.cancelDiscovery();
            Log.d(TAG,"btnDiscover: Canceling discovery");

            checkBTPermissions();

            mBluetoothAdapter.startDiscovery();
            IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(mReceiver3,discoverDevicesIntent);
        }

        if (!mBluetoothAdapter.isDiscovering()){
            checkBTPermissions();

            mBluetoothAdapter.startDiscovery();
            IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(mReceiver3,discoverDevicesIntent);
        }
    }

    private void checkBTPermissions() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP){
            int permissionCheck = this.checkSelfPermission("Manifest.permission.ACCESS_FINE_LOCATION");
            permissionCheck += this.checkSelfPermission("Manifest.permission.ACCESS_FINE_LOCATION");
            if (permissionCheck != 0) {
                this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},1001);
            }
            else {
                Log.d(TAG,"checkBTPermissions: No need to check permission. SDK version < LOLLIPOP");
            }
        }
    }

    public void display(View view) {
        ImageView Iv = findViewById(R.id.profilepic);
        TextView Tv = findViewById(R.id.profilename);
        Picasso.with(SeeFriendsActivity.this).load("https://graph.facebook.com/" + a+ "/picture?type=large").into(Iv);
        Tv.setText(b+"\n"+c);
    }

    class MyHandler extends Handler{
        public MyHandler(Looper looper){
            super(looper);
        }
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            Log.e("mainmain",(String)msg.obj);
            result=(String)msg.obj;
            //  Handler in main recieves message
        }
    }
    class MyThread extends Thread{
        public void run(){
            Looper.prepare();
            handler1 = new ThreadHandler(Looper.myLooper());
            Looper.loop();
        }
        class ThreadHandler extends Handler{
            public ThreadHandler(Looper looper){
                super(looper);
            }
            public void handleMessage(Message msg){
                handler2 = new MyHandler(Looper.getMainLooper());
                Log.e("thread",(String)msg.obj);
                Log.e("useridddddd",userid);
                String result=WebService.executeHttpGet(userid,(String)msg.obj);
//                Log.e("rere",":"+result);
                Log.d(TAG,"rere"+result);
                if (!(result==null||result.equals("null"))){
                    Log.e("rerere",result);
                    Message msg2 = handler2.obtainMessage(1,result);
                    Log.e("msg2",(String)msg2.obj);
                    handler2.sendMessage(msg2);

                    try {
                        obj = new JSONObject(result);
                        a = obj.getString("userid");
                        b = obj.getString("username");
                        c = obj.getString("relation");
                        Log.e("aaaaaa",a+b+c);





                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable(){
                        @Override
                        public void run(){
                            Button btn = findViewById(R.id.display);
                            btn.performClick();
                        }
                    });

                }

            }
        }
    }



}


