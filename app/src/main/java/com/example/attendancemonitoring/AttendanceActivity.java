package com.example.attendancemonitoring;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.preference.PreferenceManager;

import com.example.attendancemonitoring.Helpers.Strings;
import com.example.attendancemonitoring.InitThreads.ClientInit;
import com.example.attendancemonitoring.InitThreads.ServerInit;
import com.example.attendancemonitoring.Receivers.WifiDirectBroadcastReceiver;
import com.example.attendancemonitoring.Repositories.UserRepository;

public  class AttendanceActivity extends Activity {

        public static final String TAG = "MainActivity";
        public static final String DEFAULT_CHAT_NAME = "";
        private WifiP2pManager mManager;
        private WifiP2pManager.Channel mChannel;
        private WifiDirectBroadcastReceiver mReceiver;
        private IntentFilter mIntentFilter;

        private Button btnGoToAttendance;

        private TextView setActivityName;
        private TextView helpMessage;

        private LinearLayout wifiMessageLayout;

        public static String useFullName;
        public static ServerInit server;

        //Getters and Setters
        public Button getBtnGoToAttendance(){ return btnGoToAttendance; }
        public LinearLayout getWifiMessageLayout() { return wifiMessageLayout; }
        public TextView getSetActivityName() { return setActivityName; }
        public TextView getHelpMessage() { return helpMessage; }



        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_attendance);

            //Init the Channel, Intent filter and Broadcast receiver
            init();

            wifiMessageLayout = findViewById(R.id.wifiMessageLayout);

            wifiMessageLayout.setOnClickListener(view -> startActivityForResult(new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS), 0));


            btnGoToAttendance = findViewById(R.id.goToAttendance);
            setActivityName = findViewById(R.id.setActivityName);
            helpMessage = findViewById(R.id.helpMessage);

            Intent intent = getIntent();

            this.initHelpMessageForUser();
            this.gotoAttendance();


            if(UserRepository.getUserRole(this).equals("employee")) {
                this.saveUserFullName(this, intent.getStringExtra("ACTIVITY_NAME"));
                setActivityName.setText(String.format("Activity Name : %s", Strings.capitalize(loadUserFullName(this))));
            } else {
                this.saveUserFullName(this, UserRepository.getUserFullname(this));
                setActivityName.setText(Strings.capitalize(loadUserFullName(this)));
            }


        }

    private void initHelpMessageForUser() {
        if(UserRepository.getUserRole(this).equals("employee")) {
            helpMessage.setText(R.string.server_help_message);
        } else {
            helpMessage.setText(R.string.client_help_message);
        }
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

        public void gotoAttendance() {
            btnGoToAttendance.setOnClickListener(arg0 -> {

                    saveUserFullName(AttendanceActivity.this, setActivityName.getText().toString());
                    useFullName = loadUserFullName(AttendanceActivity.this);

                    //Start the init process
                    if(mReceiver.isGroupeOwner() ==  WifiDirectBroadcastReceiver.IS_OWNER){
                        Toast.makeText(AttendanceActivity.this, "I'm the group owner  " + mReceiver.getOwnerAddr().getHostAddress(), Toast.LENGTH_SHORT).show();
                        server = new ServerInit();
                        server.start();
                    }

                    else if(mReceiver.isGroupeOwner() ==  WifiDirectBroadcastReceiver.IS_CLIENT){
                        Toast.makeText(AttendanceActivity.this, "I'm the client", Toast.LENGTH_SHORT).show();
                        ClientInit client = new ClientInit(mReceiver.getOwnerAddr());
                        client.start();
                    }

                    //Open the ChatActivity
                    Intent intent = new Intent(getApplicationContext(), ScanQRActivity.class);
                    if(UserRepository.getUserRole(this).equals("employee")) {
                        Intent i = getIntent();
                        String activity_name = i.getStringExtra("ACTIVITY_NAME");
                        String activity_id = i.getStringExtra("ACTIVITY_ID");
                        intent.putExtra("ACTIVITY_NAME", activity_name);
                        intent.putExtra("ACTIVITY_ID", activity_id);
                    }
                    startActivity(intent);

            });
        }

       /* public void disconnect(){
            disconnect.setOnClickListener(v -> {
                mManager.removeGroup(mChannel, null);
                finish();
            });
        }

   */

        //Save the chat name to SharedPreferences
        public void saveUserFullName(Context context, String chatName) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor edit = prefs.edit();
            edit.putString("useFullName", chatName);
            edit.commit();
        }

        //Retrieve the chat name from SharedPreferences
        public static String loadUserFullName(Context context) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            return prefs.getString("useFullName", DEFAULT_CHAT_NAME);
        }

    }