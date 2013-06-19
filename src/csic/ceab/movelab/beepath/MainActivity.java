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
 * 
 */

package csic.ceab.movelab.beepath;

import java.io.File;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.TextView;

/**
 * Main activity that user interacts with while performing the search..
 * 
 * @author John R.B. Palmer
 */
public class MainActivity extends FragmentActivity {

	Context context = this;

	TextView tv;

	// The server address for the html/javascript code. MAKE SURE TO UPDATE THIS
	// WHEN NEW VERsION READY.
	private static final String BP_URL = "http://bee-path.net/app2013/index.html";

	// private static final String BP_URL =
	// "http://tce.ceab.csic.es/beepath/ug.html";

	FixReceiver fixReceiver;

	private WebView myWebView;
	private Dialog alert = null;
	private static final int MENU_EXIT = Menu.FIRST;
	private static final int EXIT_DIALOG = 0;

	private float currentLat = 0;
	private float currentLng = 0;
	private float currentAcc = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.webview);

		myWebView = (WebView) findViewById(R.id.webview);

		myWebView.getSettings().setSupportMultipleWindows(true);

		myWebView.getSettings().setPluginsEnabled(true);

		myWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		myWebView.getSettings().setJavaScriptEnabled(true);
		myWebView.loadDataWithBaseURL("file:///android_asset/",
				"jquery-1.9.1.min.js", "text/html", "UTF-8", null);

		// myWebView.setWebChromeClient(new WebChromeClient());

		myWebView.loadUrl(BP_URL);

		// adding the interface
		myWebView.addJavascriptInterface(new WebAppInterface(this),
				"BeePathAndroid");

		myWebView.setWebChromeClient(new MyJavaScriptChromeClient());

		PropertyHolder.init(context);

		myWebView.loadUrl("javascript:setAndroid();");

		// FOR TESTING ONLY I AM CALLING START HERE
		// startFunction("h", "h");

		tv = (TextView) findViewById(R.id.locDisplay);
	}

	@Override
	protected void onPause() {
		unregisterReceiver(fixReceiver);

		super.onPause();
	}

	@Override
	protected void onResume() {

		PropertyHolder.useSensorACCELEROMETER(true);
		PropertyHolder.useSensorGRAVITY(false);
		PropertyHolder.useSensorMAGNETIC_FIELD(false);
		PropertyHolder.useSensorGYROSCOPE(false);
		PropertyHolder.useSensorLINEAR_ACCELERATION(false);
		PropertyHolder.useSensorORIENTATION(false);

		PropertyHolder
				.sensorDelayACCELEROMETER(SensorManager.SENSOR_DELAY_NORMAL);
		PropertyHolder.sensorDelayGRAVITY(SensorManager.SENSOR_DELAY_NORMAL);
		PropertyHolder.sensorDelayGYROSCOPE(SensorManager.SENSOR_DELAY_NORMAL);
		PropertyHolder
				.sensorDelayLINEAR_ACCELERATION(SensorManager.SENSOR_DELAY_NORMAL);
		PropertyHolder
				.sensorDelayMAGNETIC_FIELD(SensorManager.SENSOR_DELAY_NORMAL);
		PropertyHolder
				.sensorDelayORIENTATION(SensorManager.SENSOR_DELAY_NORMAL);

		IntentFilter fixFilter;
		fixFilter = new IntentFilter(getResources().getString(
				R.string.internal_message_id)
				+ Util.MESSAGE_FIX_RECORDED);
		fixReceiver = new FixReceiver();
		registerReceiver(fixReceiver, fixFilter);

		super.onResume();
	}

	// Interface between Android code and javascript code.
	public class WebAppInterface {
		Context mContext;

		/** Instantiate the interface and set the context */
		WebAppInterface(Context c) {
			mContext = c;
		}

		@JavascriptInterface
		public String isInNative() {
			return "yes";
		}

		// Start the main activity
		@JavascriptInterface
		public void enterApp(String user, String password) {

			startFunction(user, password);

		}

		@JavascriptInterface
		public float getLat() {

			Log.i("BEE_LOC", "getLat called");

			return currentLat;
		}

		@JavascriptInterface
		public float getLon() {
			Log.i("BEE_LOC", "getLon called");

			return currentLng;
		}

		@JavascriptInterface
		public float getAcc() {

			Log.i("BEE_LOC", "getAcc called");

			return currentAcc;
		}

		@JavascriptInterface
		public void stopSession() {

			context.sendBroadcast(new Intent(Util.createInternalMessage(
					Util.MESSAGE_UNSCHEDULE, context)));
			stopService(new Intent(MainActivity.this, FixGet.class));
			stopService(new Intent(MainActivity.this, FileUploader.class));

			stopService(new Intent(MainActivity.this,
					SensorGetAccelerometer.class));
			stopService(new Intent(MainActivity.this,
					SensorGetMagneticField.class));
			stopService(new Intent(MainActivity.this,
					SensorGetOrientation.class));
			stopService(new Intent(MainActivity.this, SensorGetGravity.class));
			stopService(new Intent(MainActivity.this,
					SensorGetLinear_Acceleration.class));
			stopService(new Intent(MainActivity.this, SensorGetGyroscope.class));

			new FinalUploadTask().execute(context);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// Check if the key event was the Back button and if there's history
		if ((keyCode == KeyEvent.KEYCODE_BACK) && myWebView.canGoBack()) {
			myWebView.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, MENU_EXIT, 1, "EXIT");
		return true;
	}

	@SuppressWarnings("deprecation")
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case MENU_EXIT:
			showDialog(EXIT_DIALOG);
			break;
		}
		return true;
	}

	public void stop() {
		if (alert != null && alert.isShowing())
			alert.dismiss();
		super.finish();
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		AlertDialog.Builder builder = null;
		switch (id) {
		case EXIT_DIALOG:
			builder = new AlertDialog.Builder(this);
			builder.setMessage("Do you want to exit?")
					.setCancelable(true)
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									stop();
								}
							});
			alert = builder.create();
			break;
		}
		return alert;
	}

	public class FinalUploadTask extends AsyncTask<Context, Integer, Boolean> {

		ProgressDialog prog;
		int myProgress;
		int nfiles;
		int i;

		@Override
		protected void onPreExecute() {

			prog = new ProgressDialog(context);
			prog.setIndeterminate(false);
			prog.setMax(100);
			prog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			prog.show();

		}

		protected Boolean doInBackground(Context... context) {

			// //////////
			// FIX ARRAYS
			// //////////

			File[] files = null;

			File directory = new File(context[0].getFilesDir()
					.getAbsolutePath(),
					Util.DIRECTORY_JSON_FIXARRAY_UPLOADQUEUE);
			directory.mkdirs();

			files = directory.listFiles();

			nfiles = files.length;
			i = 0;

			if (nfiles > 0) {

				JSONArray fixJSONArray = new JSONArray();

				for (File f : files) {

					publishProgress((int) 20 * (i / nfiles));

					try {

						JSONArray thisArray = new JSONArray(Util.readJSON(f));

						for (int i = 0; i < thisArray.length(); i++) {

							fixJSONArray.put(thisArray.getJSONObject(i));

						}

						f.delete();

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					i++;
				}

				if (fixJSONArray.length() > 0) {

					Util.patch2DjangoJSONArray(context[0], fixJSONArray,
							Util.URL_FIXES_JSON, Util.getUserId(),
							Util.getPassword());

				}
			}

			publishProgress(40);

			// now sensors

			files = null;

			directory = new File(context[0].getFilesDir().getAbsolutePath(),
					Util.DIRECTORY_JSON_UPLOADQUEUE);
			directory.mkdirs();

			files = directory.listFiles();

			nfiles = files.length;
			i = 0;

			if (files.length > 0) {

				for (File f : files) {

					JSONArray thisArray;
					try {
						thisArray = new JSONArray(Util.readJSON(f));

						Util.patch2DjangoJSONArray(context[0], thisArray,
								Util.URL_SENSOR_JSON, Util.getUserId(),
								Util.getPassword());
						f.delete();

					} catch (JSONException e) {
						e.printStackTrace();
					}

					i++;
				}

			}

			publishProgress(60);

			// now fixes saved after upload errors
			files = null;
			directory = null;
			directory = new File(context[0].getFilesDir().getAbsolutePath(),
					Util.DIRECTORY_JSON_UPLOAD_ERROR_QUEUE_FIXES);
			directory.mkdirs();
			files = directory.listFiles();

			nfiles = files.length;
			i = 0;
			if (files.length > 0) {
				for (File f : files) {
					try {
						JSONArray ja = new JSONArray(Util.readJSON(f));
						Util.uploadJSONArray(context[0], ja,
								Util.URL_FIXES_JSON);
						f.delete();

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					i++;

				}
			}
			publishProgress(100);

			return true;
		}

		protected void onProgressUpdate(Integer... progress) {
			prog.setProgress(progress[0]);

		}

		protected void onPostExecute(Boolean result) {

			prog.dismiss();

			// TODO return to webview

		}

	}

	public class FixReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {

			currentLat = intent.getFloatExtra("lat", 0);
			currentLng = intent.getFloatExtra("lng", 0);
			currentAcc = intent.getFloatExtra("acc", 0);

			tv.setText("Lat: " + currentLat + "\nLon: " + currentLng
					+ "\nAccuracy: " + currentAcc);

			Log.i("BEE_LOC", "lat: " + currentLat);
			Log.i("BEE_LOC", "lng: " + currentLng);
			Log.i("BEE_LOC", "acc: " + currentAcc);

		}

	}

	private void startFunction(String user, String password) {
		PropertyHolder.setRegistered(true);
		PropertyHolder.setServiceOn(true);

		// TODO grab this from js
		PropertyHolder.setUserId(user);
		PropertyHolder.setPassword(password);

		// trip id not used for this, so I am just setting it to x so it is
		// not null
		PropertyHolder.setTripId("xxx");

		context.sendBroadcast(new Intent(Util.createInternalMessage(
				Util.MESSAGE_SCHEDULE, context)));
		startService(new Intent(MainActivity.this, FixGet.class));
		PropertyHolder.tripStartTime(System.currentTimeMillis());

	}

	private class MyJavaScriptChromeClient extends WebChromeClient {
		@Override
		public boolean onJsAlert(WebView view, String url, String message,
				final JsResult result) {
			// handle Alert event, here we are showing AlertDialog
			new AlertDialog.Builder(MainActivity.this)
					// .setTitle("JavaScript Alert !")
					.setMessage(message)
					.setPositiveButton(android.R.string.ok,
							new AlertDialog.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									result.confirm();
								}
							}).setCancelable(false).create().show();
			return true;
		}
	}

}
