/*
/*
 * This file incorporates code from Space Mapper, which is subject 
 * to the following terms: 
 *
 * 		Space Mapper
 * 		Copyright (C) 2012, 2013 John R.B. Palmer
 * 		Contact: jrpalmer@princeton.edu
 *
 * 		Space Mapper is free software: you can redistribute it and/or modify 
 * 		it under the terms of the GNU General Public License as published by 
 * 		the Free Software Foundation, either version 3 of the License, or (at 
 * 		your option) any later version.
 * 
 * 		Space Mapper is distributed in the hope that it will be useful, but 
 * 		WITHOUT ANY WARRANTY; without even the implied warranty of 
 * 		MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  
 * 		See the GNU General Public License for more details.
 * 
 * 		You should have received a copy of the GNU General Public License along 
 * 		with Space Mapper.  If not, see <http://www.gnu.org/licenses/>.
 */

package csic.ceab.movelab.beepath;

import java.util.ArrayList;

import org.json.JSONArray;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.Log;

/**
 * Service for recording Accelerometer data.
 * <p>
 * Dependencies: BeePathBroadcastReceiver.java, FixGet.java.
 * <p>
 * 
 * @author John R.B. Palmer
 */

abstract class SensorGet extends Service implements SensorEventListener {
	StopReceiver stopReceiver;
	IntentFilter stopFilter;
	boolean sensorsInProgress = false;
	String TAG = "SensorGet";
	Context context;

	String thisTripid;

	ArrayList<SensorDataPoint> sdpList;

	@Override
	public void onCreate() {

		context = getApplicationContext();
		if (PropertyHolder.isInit() == false)
			PropertyHolder.init(context);

		thisTripid = PropertyHolder.getTripId();

		Log.i("ACCEL", "on create");
	}

	public void onStart(Intent intent, int startId) {

		Log.i("ACCEL", "on start");

		Thread sensingThread = new Thread(null, doSensing, "sensingBackground");
		sensingThread.start();

	}

	private Runnable doSensing = new Runnable() {
		public void run() {

			trySensing();
		}
	};

	private void trySensing() {
		stopReceiver = null;
		if (sensorsInProgress == false) {
			sensorsInProgress = true;

			sdpList = new ArrayList<SensorDataPoint>();

			stopFilter = new IntentFilter(getResources().getString(
					R.string.internal_message_id)
					+ Util.MESSAGE_STOP_FIXGET);
			stopReceiver = new StopReceiver();
			registerReceiver(stopReceiver, stopFilter);

			registerListener();

		}
	}

	abstract void registerListener();

	@Override
	public void onDestroy() {


		unregisterListener();

			try {
				unregisterReceiver(stopReceiver);
			} catch (IllegalArgumentException e) {

			}
		sensorsInProgress = false;

	}

	abstract void unregisterListener();

	/**
	 * Returns Object that receives client interactions.
	 * 
	 * @return The Object that receives interactions from clients.
	 */
	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	// This is the object that receives interactions from clients.
	private final IBinder mBinder = new Binder() {
		@Override
		protected boolean onTransact(int code, Parcel data, Parcel reply,
				int flags) throws RemoteException {
			return super.onTransact(code, data, reply, flags);
		}
	};

	public class StopReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {

			Log.i("ACCEL", "stop receiver on receive");

			if (sdpList != null && sdpList.size() > 0) {
				storeSensorValues(sdpList);
				Log.i("ACCEL", "on storedsensorvalues");
			}
			sensorsInProgress = false;
			stopSelf();

		}

	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSensorChanged(SensorEvent event) {

		SensorDataPoint thisDataPoint = new SensorDataPoint(thisTripid,
				event.sensor.getName(), event.sensor.getType(),
				event.sensor.getMaximumRange(), -1, event.sensor.getPower(),
				event.sensor.getResolution(), event.sensor.getVendor(),
				event.sensor.getVersion(), event.accuracy, event.timestamp,
				event.values);

		sdpList.add(thisDataPoint);

	}

	public void storeSensorValues(ArrayList<SensorDataPoint> data) {

		if (PropertyHolder.isInit() == false)
			PropertyHolder.init(context);

		JSONArray sdpJSONArray = new JSONArray();

		for (SensorDataPoint sdp : data) {
			sdpJSONArray.put(sdp.exportJSON(context));
		}

		if (sdpJSONArray.length() > 0) {

			String fileprefix = makeFilePrefix();

			Log.i("SensorGet", "about to save JSON array");

			Util.saveJSON(context, Util.DIRECTORY_JSON_UPLOADQUEUE, fileprefix,
					sdpJSONArray.toString());

		}

	}

	abstract String makeFilePrefix();
}