package com.example.attendancemonitoring;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.attendancemonitoring.InitThreads.ClientInit;
import com.example.attendancemonitoring.Receivers.WifiDirectBroadcastReceiver;

public class AttendanceActivity extends Activity {

    public static final String TAG = "AttendanceActivity";
    public static final String DEFAULT_CHAT_NAME = "DEFAULT_CHATNAME";
    private WifiP2pManager mManager;
    private WifiP2pManager.Channel mChannel;
    private WifiDirectBroadcastReceiver mReceiver;
    private IntentFilter mIntentFilter;
    private Button goToAttendance;
    private ImageView goToSettings;
    private TextView goToSettingsText;
    private TextView setChatNameLabel;
    private EditText setChatName;
    private ImageView disconnect;
    public static String chatName;
//    public static ServerInit server;

    //Getters and Setters
    public WifiP2pManager getmManager() { return mManager; }
    public WifiP2pManager.Channel getmChannel() { return mChannel; }
    public WifiDirectBroadcastReceiver getmReceiver() { return mReceiver; }
    public IntentFilter getmIntentFilter() { return mIntentFilter; }
    public Button getGoToAttendance(){ return goToAttendance; }
    public TextView getSetChatNameLabel() { return setChatNameLabel; }
    public ImageView getGoToSettings() { return goToSettings; }
    public EditText getSetChatName() { return setChatName; }
    public TextView getGoToSettingsText() { return goToSettingsText; }
    public ImageView getDisconnect() { return disconnect; }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        //Init the Channel, Intent filter and Broadcast receiver
        init();
        goToAttendance = findViewById(R.id.gotoAttendance);
        goToAttendance();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);


    }

    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(mReceiver, mIntentFilter);

        mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {

            @Override
            public void onSuccess() {
                Log.v(TAG, "Discovery process succeeded");
            }

            @Override
            public void onFailure(int reason) {
                Log.v(TAG, "Discovery process failed");
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
    }



    public void init(){
        mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(this, getMainLooper(), null);
        mReceiver = WifiDirectBroadcastReceiver.createInstance();
        mReceiver.setmManager(mManager);
        mReceiver.setmChannel(mChannel);
        mReceiver.setmActivity(this);

        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
    }

    public void goToAttendance(){
        goToAttendance.setOnClickListener(arg0 -> {

               if(mReceiver.isGroupeOwner() ==  WifiDirectBroadcastReceiver.IS_CLIENT){
                    Toast.makeText(AttendanceActivity.this, "I'm the client", Toast.LENGTH_SHORT).show();
                    ClientInit client = new ClientInit(mReceiver.getOwnerAddr());
                    client.start();
                   Intent intent = new Intent(getApplicationContext(), ScanQRActivity.class);
                   startActivity(intent);
                } else {
                   Toast.makeText(AttendanceActivity.this, "You are not a client!", Toast.LENGTH_SHORT).show();
               }

        });
    }

  /*  public void disconnect(){
        disconnect.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mManager.removeGroup(mChannel, null);
                finish();
            }
        });
    }*/

  /*  public void goToSettings(){
        goToSettings.setOnClickListener(arg0 -> {

            //Open Wifi settings
            startActivityForResult(new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS), 0);
        });
    }*/



}
