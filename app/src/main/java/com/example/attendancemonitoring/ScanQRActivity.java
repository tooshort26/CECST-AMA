package com.example.attendancemonitoring;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Button;

import com.example.attendancemonitoring.AsyncTasks.SendMessageClient;
import com.example.attendancemonitoring.Entities.Message;
import com.example.attendancemonitoring.Receivers.WifiDirectBroadcastReceiver;

public class ScanQRActivity extends Activity {
    private static final String TAG = "ScanQRActivity";

    private WifiP2pManager mManager;
    private Channel mChannel;
    private WifiDirectBroadcastReceiver mReceiver;
    private IntentFilter mIntentFilter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qr);

        mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(this, getMainLooper(), null);
        mReceiver = WifiDirectBroadcastReceiver.createInstance();
        mReceiver.setmActivity(this);

        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        //Send a message
        Button btnSendMessage = findViewById(R.id.sendMessage);
        btnSendMessage.setOnClickListener(arg0 -> sendMessage(Message.TEXT_MESSAGE));
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
        saveStateForeground(true);
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
        saveStateForeground(false);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder newDialog = new AlertDialog.Builder(this);
        newDialog.setTitle("Close chatroom");
        newDialog.setMessage("Are you sure you want to close this chatroom?\n"
                + "You will no longer be able to receive messages, and "
                + "all unsaved media files will be deleted.\n"
                + "If you are the server, all other users will be disconnected as well.");

        newDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                android.os.Process.killProcess(android.os.Process.myPid());
            }

        });

        newDialog.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        newDialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        super.onStop();
    }



    // Hydrate Message object then launch the AsyncTasks to send it
    public void sendMessage(int type) {
        Message mes = new Message(type, "MESSAGE_FROM_CLIENT", null, AttendanceActivity.chatName);


        if(mReceiver.isGroupeOwner() == WifiDirectBroadcastReceiver.IS_CLIENT){
            Log.e(TAG, "Message hydrated, start SendMessageClient AsyncTask");
            new SendMessageClient(ScanQRActivity.this, mReceiver.getOwnerAddr()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mes);
        }
    }


    // Save the app's state (foreground or background) to a SharedPrefereces
    public void saveStateForeground(boolean isForeground){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        Editor edit = prefs.edit();
        edit.putBoolean("isForeground", isForeground);
        edit.commit();
    }



}
