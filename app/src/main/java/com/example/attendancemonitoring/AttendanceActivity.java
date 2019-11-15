package com.example.attendancemonitoring;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.attendancemonitoring.DatabaseModules.Models.Attendance;
import com.example.attendancemonitoring.Repositories.AttendanceRepository;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class AttendanceActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private ZXingScannerView scannerView;
    private TextView textResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        scannerView = findViewById(R.id.zxscan);
        textResult = findViewById(R.id.text_result);

        Dexter.withActivity(this)
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        scannerView.setResultHandler(AttendanceActivity.this);
                        scannerView.startCamera();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toast.makeText(getApplicationContext(), "You must accept this permission.", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                })
                .check();

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, DashboardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        scannerView.stopCamera();
        super.onDestroy();
    }

    @Override
    public void handleResult(Result rawResult) {
        // Receive the result
        textResult.setText(rawResult.getText());
        // Send to bluetooth
        // Save to attendance
        boolean hasAlreadyAttend = AttendanceRepository.hasAttend(getApplicationContext(), rawResult.getText());
        if  (!hasAlreadyAttend) {
            Attendance attendance = new Attendance(rawResult.getText(), rawResult.getText() + " Description", "10am", "12pm");
            AttendanceRepository.create(this, attendance);
            Toast.makeText(this, "Successfully sign an attendance for " + rawResult.getText() + ".", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "You already attend the " + rawResult.getText() + " activity.", Toast.LENGTH_LONG).show();
        }

        Intent i = new Intent(getApplicationContext(),DashboardActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }
}
