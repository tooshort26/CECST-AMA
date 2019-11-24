package com.example.attendancemonitoring;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.attendancemonitoring.Adapters.AttendsAdapter;
import com.example.attendancemonitoring.AsyncTasks.SendMessageClient;
import com.example.attendancemonitoring.DatabaseModules.DB;
import com.example.attendancemonitoring.Entities.Message;
import com.example.attendancemonitoring.Helpers.QRCode;
import com.example.attendancemonitoring.Helpers.Strings;
import com.example.attendancemonitoring.Receivers.WifiDirectBroadcastReceiver;
import com.example.attendancemonitoring.Repositories.ActivityRepository;
import com.example.attendancemonitoring.Repositories.AttendanceRepository;
import com.example.attendancemonitoring.Repositories.UserRepository;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import me.dm7.barcodescanner.zxing.ZXingScannerView;


public class ScanQRActivity extends Activity implements ZXingScannerView.ResultHandler {


    private static final String TAG = "ScanQRActivity";


    private WifiP2pManager mManager;
    private Channel mChannel;
    private WifiDirectBroadcastReceiver mReceiver;
    private IntentFilter mIntentFilter;


    private ZXingScannerView scannerView;
    private ImageView qrCode;


    private static RecyclerView attendsView;
    private static List<Message> listMessage;
    private static List<String> listAndroidID;

    private static AttendsAdapter attendsAdapter;
    private static LinearLayoutManager layoutManager;
    private static String activityId = "0";
    private static String activityName;

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

        attendsView = findViewById(R.id.attends_view);
        scannerView = findViewById(R.id.scannerView);
        qrCode = findViewById(R.id.qrCode);

        LinearLayout serverLayout = findViewById(R.id.serverLayout);
        LinearLayout clientLayout = findViewById(R.id.clientLayout);
        TextView txtActivityName = findViewById(R.id.txtActivityName);


        //Start the service to receive message
        startService(new Intent(this, MessageService.class));

        // Check if the user is group owner/server or a client.
        if (mReceiver.isGroupeOwner() == WifiDirectBroadcastReceiver.IS_OWNER) {
            // Display the recycler view & QR code

            Intent intent = getIntent();
            activityId = intent.getStringExtra("ACTIVITY_ID");
            activityName = intent.getStringExtra("ACTIVITY_NAME");
            generateQRCodeByActivityName(activityName,  activityId);
            txtActivityName.setText(String.format("%s", activityName));
            serverLayout.setVisibility(View.VISIBLE);

            listMessage = new ArrayList<>();
            listAndroidID = new ArrayList<>();

            attendsAdapter = new AttendsAdapter(this, listMessage);

            attendsView.addItemDecoration(new DividerItemDecoration(this, 0));

            layoutManager = new LinearLayoutManager(this);

            attendsView.setLayoutManager(layoutManager);

            attendsView.setAdapter(attendsAdapter);
        } else {
            // Un-hide the QR Code Scanner View
            clientLayout.setVisibility(View.VISIBLE);
            initQRCodeScanner();
        }

    }

    private void generateQRCodeByActivityName(String activity, String activity_id) {
        try {
            Bitmap bmp = QRCode.encodeAsBitmap(activity + "," + activity_id);
            qrCode.setImageBitmap(bmp);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }




    private void initQRCodeScanner() {
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        scannerView.setResultHandler(ScanQRActivity.this);
                        scannerView.startCamera();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toast.makeText(ScanQRActivity.this, "You need to accept this permission.", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                }).check();
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
        newDialog.setTitle("Close");
        newDialog.setMessage("Are you sure you want to close process?");

        newDialog.setPositiveButton("Yes", (dialog, which) -> {
            if (AttendanceActivity.server != null) {
                AttendanceActivity.server.interrupt();
            }
            android.os.Process.killProcess(android.os.Process.myPid());
            // Save the android IDs
        });

        newDialog.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        newDialog.show();
    }

    @Override
    protected void onDestroy() {
        scannerView.stopCamera();
        super.onDestroy();
        super.onStop();
    }


    // Hydrate Message object then launch the AsyncTasks to send it
    public void sendMessage(int type, String message) {
        Message mes = new Message(type, message, null, AttendanceActivity.useFullName);


        if (mReceiver.isGroupeOwner() == WifiDirectBroadcastReceiver.IS_CLIENT) {
            Log.e(TAG, "Message hydrated, start SendMessageClient AsyncTask");
            new SendMessageClient(ScanQRActivity.this, mReceiver.getOwnerAddr()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mes);
        }

    }

    // Update the attendance list of the server.
    public static void refreshList(Message message, boolean isMine) {
        String deviceId = Strings.splitAndGetLastElement(message.getmText());
        String[] splitted = message.getmText().split(Pattern.quote(","));
        String studentId = splitted[0];
        String studentName = splitted[1];

        message.setMine(isMine);
        if(!listAndroidID.contains(deviceId) && DB.getInstance(attendsView.getContext()).attendanceDao().check(Integer.parseInt(activityId), deviceId) <= 0) {
            listAndroidID.add(deviceId);
            listMessage.add(message);
            AttendanceRepository.create(attendsView.getContext(), Integer.parseInt(activityId), studentName, studentId, deviceId);
            attendsAdapter.notifyDataSetChanged();
        } else {
            attendsAdapter.displayDuplicate();
        }

    }



    // Save the app's state (foreground or background) to a SharedPrefereces
    public void saveStateForeground(boolean isForeground) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        Editor edit = prefs.edit();
        edit.putBoolean("isForeground", isForeground);
        edit.apply();
    }


    // Result of the QR Code Scan
    @Override
    public void handleResult(Result rawResult) {

        String[] splitted = rawResult.getText().split(Pattern.quote(","));
        int len = splitted.length - 1;
        int activityId = Integer.parseInt(splitted[len]);
        String activityName = splitted[0];


        @SuppressLint("HardwareIds") String androidID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        String message = UserRepository.getIdNumber(this) + "," + UserRepository.getUserFullname(this) + "," + androidID;
        sendMessage(Message.TEXT_MESSAGE, message);
        int isStudentPhoneAlreadyUse = DB.getInstance(this).attendanceDao().check(activityId, androidID);

        if (isStudentPhoneAlreadyUse <= 0) {
            ActivityRepository.create(this, activityId, activityName, "activity_description");
            AttendanceRepository.create(this, activityId, UserRepository.getUserFullname(this), UserRepository.getIdNumber(this), androidID);
        } else {
            Toast.makeText(this, "Invalid you already signed this attendance.", Toast.LENGTH_SHORT).show();
        }
        // Disconnect from the server.
//        mManager.removeGroup(mChannel, null);
        finish();
     }
}
