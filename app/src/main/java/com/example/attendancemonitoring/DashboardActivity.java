package com.example.attendancemonitoring;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.attendancemonitoring.Repositories.UserRepository;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONObject;

import java.net.URISyntaxException;

import me.aflak.bluetooth.Bluetooth;
import me.aflak.bluetooth.interfaces.BluetoothCallback;

public class DashboardActivity extends AppCompatActivity {
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private NavigationView nvDrawer;


    private Bluetooth bluetooth;

    private ActionBarDrawerToggle drawerToggle;


    private Socket mSocket;
    {
        try {
            mSocket = IO.socket("http://192.168.1.4:3030");
        } catch (URISyntaxException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private Emitter.Listener onNewMessage = args -> runOnUiThread(() -> {
        JSONObject data = (JSONObject) args[0];
        Toast.makeText(DashboardActivity.this, String.valueOf(data), Toast.LENGTH_LONG).show();
    });




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Set a Toolbar to replace the ActionBar.
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nvDrawer = findViewById(R.id.nvView);
        // Setup drawer view
        setupDrawerContent(nvDrawer);

        // Find our drawer view
        mDrawer = findViewById(R.id.drawer_layout);
        drawerToggle = setupDrawerToggle();

        // Setup toggle to display hamburger icon with nice animation
        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerToggle.syncState();

        // Tie DrawerLayout events to the ActionBarToggle
        mDrawer.addDrawerListener(drawerToggle);

        mSocket.connect();
//        mSocket.on("sample", onNewMessage);


        bluetooth = new Bluetooth(this);
        bluetooth.setBluetoothCallback(bluetoothCallback);

    }



    @Override
    protected void onStart() {
        super.onStart();
        bluetooth.onStart();
        if(bluetooth.isEnabled()){
            Toast.makeText(this, "Bluetooth is on", Toast.LENGTH_SHORT).show();
            // Pair the device or automatically send.

        } else {
            bluetooth.enable();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        bluetooth.onStop();
    }

    private BluetoothCallback bluetoothCallback = new BluetoothCallback() {
        @Override public void onBluetoothTurningOn() {}
        @Override public void onBluetoothTurningOff() {}
        @Override public void onBluetoothOff() {}
        @Override public void onUserDeniedActivation() {}

        @Override
        public void onBluetoothOn() {
            // doStuffWhenBluetoothOn() ...
        }
    };

    // `onPostCreate` called when activity start-up is complete after `onStart()`
    // NOTE 1: Make sure to override the method with only a single `Bundle` argument
    // Note 2: Make sure you implement the correct `onPostCreate(Bundle savedInstanceState)` method.
    // There are 2 signatures and only `onPostCreate(Bundle state)` shows the hamburger icon.
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        drawerToggle.onConfigurationChanged(newConfig);
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        // NOTE: Make sure you pass in a valid toolbar reference.  ActionBarDrawToggle() does not require it
        // and will not render the hamburger icon without it.
        return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open,  R.string.drawer_close);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                menuItem -> {
                    selectDrawerItem(menuItem);
                    return true;
                });
    }

    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        Fragment fragment = null;
        Class fragmentClass = ActivityFragment.class;
        boolean isFragment = false;
        switch(menuItem.getItemId()) {
            case R.id.nav_attendance:
                Intent intent = new Intent(this, AttendanceActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_second_fragment:
                fragmentClass = SecondFragment.class;
                isFragment = true;
                break;
            case R.id.nav_third_fragment:
                fragmentClass = ThirdFragment.class;
                isFragment = true;
                break;
            default:
                fragmentClass = ActivityFragment.class;
                isFragment = true;
        }

        if  (isFragment) {
            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Insert the fragment by replacing any existing fragment
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
        }



        // Highlight the selected item has been done by NavigationView
        menuItem.setChecked(true);
        // Set action bar title
        setTitle(menuItem.getTitle());
        // Close the navigation drawer
        mDrawer.closeDrawers();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



}
