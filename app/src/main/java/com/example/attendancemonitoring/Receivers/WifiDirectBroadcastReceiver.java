package com.example.attendancemonitoring.Receivers;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.util.Log;
import android.view.View;

import com.example.attendancemonitoring.AttendanceActivity;
import com.example.attendancemonitoring.Repositories.UserRepository;

import java.lang.reflect.Method;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

/*
 * This class implements the Singleton pattern
 */
public class WifiDirectBroadcastReceiver extends BroadcastReceiver{

	public static final int IS_OWNER = 1;
	public static final int IS_CLIENT = 2;
	private static final String TAG = "WifiDirectBR";

	private WifiP2pManager mManager;
	private Channel mChannel;
	private Activity mActivity;
	private List<String> peersName = new ArrayList<>();
	private List<WifiP2pDevice> peers = new ArrayList<>();
	private int isGroupeOwner;
	private InetAddress ownerAddr;

	private static WifiDirectBroadcastReceiver instance;

	private WifiDirectBroadcastReceiver(){
		super();
	}

	public static WifiDirectBroadcastReceiver createInstance(){
		if(instance == null){
			instance = new WifiDirectBroadcastReceiver();
		}
		return instance;
	}

	public List<String> getPeersName() { return peersName; }
	public List<WifiP2pDevice> getPeers() { return peers; }
	public int isGroupeOwner() { return isGroupeOwner; }
	public InetAddress getOwnerAddr() { return ownerAddr; }
	public void setmManager(WifiP2pManager mManager) { this.mManager = mManager; }
	public void setmChannel(Channel mChannel) { this.mChannel = mChannel; }
	public void setmActivity(Activity mActivity) { this.mActivity = mActivity; }

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		this.deletePersistentGroups(mManager, mChannel);

		/**********************************
		 Wifi P2P is enabled or disabled
		 **********************************/
		if(action.equals(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION)){
			Log.v(TAG, "WIFI_P2P_STATE_CHANGED_ACTION");

			//check if Wifi P2P is supported
			int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
			if(state != WifiP2pManager.WIFI_P2P_STATE_ENABLED){
				WifiManager wifiManager = (WifiManager) mActivity.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
				wifiManager.setWifiEnabled(true);
//				Toast.makeText(mActivity, "Wifi P2P is supported by this device", Toast.LENGTH_SHORT).show();
			}
/*			else{
				Toast.makeText(mActivity, "Wifi P2P is not supported by this device", Toast.LENGTH_SHORT).show();
			}*/
		}

		/**********************************
		 Available peer list has changed
		 **********************************/
		else if(action.equals(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION)){
			Log.v(TAG, "WIFI_P2P_PEERS_CHANGED_ACTION");
		}

		/***************************************
		 This device's wifi state has changed
		 ***************************************/
		else if(action.equals(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION)){
			Log.v(TAG, "WIFI_P2P_THIS_DEVICE_CHANGED_ACTION");

		}

		/******************************************************************
		 State of connectivity has changed (new connection/disconnection)
		 ******************************************************************/
		else if(action.equals(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION)){
			Log.v(TAG, "WIFI_P2P_CONNECTION_CHANGED_ACTION");

			if(mManager == null){
				return;
			}
			NetworkInfo networkInfo = intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);

			if(networkInfo.isConnected()) {

				mManager.requestConnectionInfo(mChannel, info -> {
					ownerAddr= info.groupOwnerAddress;

						/******************************************************************
						 The client : create a client thread that connects to the group owner
						 ******************************************************************/
						/******************************************************************
						 The GO : create a server thread and accept incoming connections
						 ******************************************************************/

					/*if (UserRepository.getUserRole(context).equals("employee")) {
						isGroupeOwner = IS_OWNER;
						activateGotoAttendance("server");
					}*/

					if(UserRepository.getUserRole(context).equals("employee")) {
						if(info.groupFormed && info.isGroupOwner) {
							isGroupeOwner = IS_OWNER;
							activateGotoAttendance("server");
						}

					} else {
						if(info.groupFormed){
							isGroupeOwner = IS_CLIENT;
							activateGotoAttendance("client");
						}
					}
				});
			}
		}
	}

	private void deletePersistentGroups(WifiP2pManager mManager, WifiP2pManager.Channel mChannel){


		try {
			Method[] methods = WifiP2pManager.class.getMethods();
			for (Method method : methods) {
				if (method.getName().equals("deletePersistentGroup")) {
					// Delete any persistent group
					for (int netid = 0; netid < 32; netid++) {
						method.invoke(mManager, mChannel, netid, null);
					}
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}



	public void activateGotoAttendance(String role){
		if(mActivity.getClass() == AttendanceActivity.class) {


			((AttendanceActivity)mActivity).getWifiMessageLayout().setVisibility(View.GONE);
			((AttendanceActivity)mActivity).getSetActivityName().setVisibility(View.VISIBLE);
			((AttendanceActivity)mActivity).getBtnGoToAttendance().setVisibility(View.VISIBLE);

			((AttendanceActivity)mActivity).getBtnGoToAttendance().setText(String.format("Start %s", role));

		}
	}



}
