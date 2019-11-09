package com.example.attendancemonitoring;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.example.attendancemonitoring.Helpers.SharedPref;

import java.lang.ref.WeakReference;


public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        this.setActivityToFullScreen();
        if  ( !SharedPref.getSharedPreferenceBoolean(this,"is_splash_open",false) ) {
            new runOnBackground(SplashActivity.this).execute();
        } else {
            Intent i = new Intent(getApplicationContext(),RegisterActivity.class);
            startActivity(i);
            finish();
        }

    }
    private void setActivityToFullScreen()
    {
        Window window = getWindow();
        WindowManager.LayoutParams winParams = window.getAttributes();
        winParams.flags &= ~WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        window.setAttributes(winParams);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    private class runOnBackground extends AsyncTask<Void , Integer , Void> {

        private WeakReference<SplashActivity> activityReference;

        // Only retain a weak reference to the activity
            runOnBackground(SplashActivity context) {
                activityReference = new WeakReference<>(context);
            }

            @Override
            protected void onPreExecute() {
                // What do you want to execute while the splash screen display.
            }

            @Override
            protected Void doInBackground(Void... voids) {
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                Intent i = new Intent(getApplicationContext(),RegisterActivity.class);
                startActivity(i);
                finish();
        }

    }
}
