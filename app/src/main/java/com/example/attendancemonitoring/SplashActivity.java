package com.example.attendancemonitoring;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.example.attendancemonitoring.Helpers.SharedPref;
import com.example.attendancemonitoring.Helpers.UserHelper;
import com.example.attendancemonitoring.Repositories.UserRepository;

import java.lang.ref.WeakReference;


public class SplashActivity extends AppCompatActivity {
    private Handler mWaitHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        this.setActivityToFullScreen();

        if  ( !SharedPref.getSharedPreferenceBoolean(this,"is_splash_open",false) ) {
                mWaitHandler.postDelayed(() -> {
                //The following code will execute after the 5 seconds.
                try {
                    new runOnBackground(SplashActivity.this).execute();
                } catch (Exception ignored) {
                    ignored.printStackTrace();
                }
            }, 5000);  // Give a 5 seconds delay.

        } else {
            this.redirect();
        }


    }

    private void redirect() {
        Class<?> activity;
        if (UserHelper.isUserAlreadyRegister(this) && UserRepository.isUserAlreadyRegister(this)) {
            activity = DashboardActivity.class;
        } else {
            activity = MainActivity.class;
        }

        Intent i = new Intent(getApplicationContext(), activity);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(i);
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

            }

            @Override
            protected Void doInBackground(Void... voids) {
                //SharedPref.setSharedPreferenceBoolean(getApplicationContext(),"is_splash_open",true);
                WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                if (wifiManager != null) {
                    wifiManager.setWifiEnabled(false);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
              redirect();
            }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //Remove all the callbacks otherwise navigation will execute even after activity is killed or closed.
        mWaitHandler.removeCallbacksAndMessages(null);
    }
}
