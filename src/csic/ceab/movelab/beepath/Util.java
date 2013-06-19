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
 * This file also incorporates code written by Chang Y. Chung and Necati E. Ozgencil 
 * for the Human Mobility Project, which is subject to the following terms: 
 * 
 * 		Copyright (C) 2010, 2011 Human Mobility Project
 *
 *		Permission is hereby granted, free of charge, to any person obtaining 
 *		a copy of this software and associated documentation files (the
 *		"Software"), to deal in the Software without restriction, including
 *		without limitation the rights to use, copy, modify, merge, publish, 
 *		distribute, sublicense, and/or sell copies of the Software, and to
 *		permit persons to whom the Software is furnished to do so, subject to
 *		the following conditions:
 *
 *		The above copyright notice and this permission notice shall be included
 *		in all copies or substantial portions of the Software.
 *
 *		THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 *		EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 *		MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 *		IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 *		CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 *		TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 *		SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE. * 
 */

package csic.ceab.movelab.beepath;

import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.FileLock;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Various static fields and methods used in the application, some taken from
 * Human Mobility Project.
 * 
 * @author Chang Y. Chung
 * @author Necati E. Ozgencil
 * @author John R.B. Palmer
 */
public class Util {

	public static final boolean testMode = false;

	public static String getPassword() {

		if (testMode) {
			return "1234";
		} else {
			return PropertyHolder.getPassword();
		}

	}

	public static String getUserId() {

		if (testMode) {
			return "john";
		} else {
			return PropertyHolder.getUserId();
		}

	}

	public static final String FILEPREFIX_UPLOAD_ERRORS = "UE";

	public static final String DIRECTORY_JSON_UPLOAD_ERROR_QUEUE_FIXES = "json_upload_error_queue_fixes";

	public static final String DIRECTORY_JSON_UPLOAD_ERROR_QUEUE_SENSORS = "json_upload_error_queue_sensors";

	public static final String DIRECTORY_JSON_UPLOADQUEUE = "json_upload_queue";

	public static final String DIRECTORY_JSON_FIXARRAY_UPLOADQUEUE = "json_fixarray_upload_queue";

	public static final boolean USE_SENSORS = true;

	public final static String MESSAGE_STOP_FIXGET = ".STOP_FIXGET";
	public final static String MESSAGE_LONGSTOP_FIXGET = ".LONGSTOP_FIXGET";
	public final static String MESSAGE_SCHEDULE = ".SCHEDULE_SERVICE";
	public final static String MESSAGE_UNSCHEDULE = ".UNSCHEDULE_SERVICE";
	public final static String MESSAGE_FIX_RECORDED = ".NEW_FIX_RECORDED";
	public final static String MESSAGE_FIX_UPLOADED = ".NEW_FIX_UPLOADED";
	public final static String MESSAGE_UPLOADS_NEEDED = ".UPLOADS_NEEDED";
	public final static String MESSAGE_UPLOADS_NOT_NEEDED = ".UPLOADS_NOT_NEEDED";

	public static String createInternalMessage(String msg, Context context) {
		return context.getResources().getString(R.string.internal_message_id)
				+ msg;
	}

	public final static int TRACKING_NOTIFICATION = 0;

	public static String[] ALPHA_NUMERIC_DIGITS = { "0", "1", "2", "3", "4",
			"5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H",
			"I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U",
			"V", "W", "X", "Y", "Z", "a", "b", "c", "d", "e", "f", "g", "h",
			"i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u",
			"v", "w", "x", "y", "z" };

	public static boolean privateMode = false;

	public static int EXTRARUNS = 4;

	public static boolean flushGPSFlag = false;

	public static boolean redrawMap = false;

	public static long xTime = 1 * 60 * 60 * 1000;
	/**
	 * Default value for the interval between location fixes. In milliseconds.
	 */
	public static final long ALARM_INTERVAL = 15000; // 15 second

	public static final long UPLOAD_INTERVAL = 1000 * 60 * 2; // 2 minutes

	/**
	 * Server URLs for uploads.
	 */

	public static final String URL_FIXES_JSON = "http://161.116.80.73:8000/api/v1/update/";
	public static final String URL_SENSOR_JSON = "http://161.116.80.73:8000/api/v1/sensor_update/";

	/**
	 * Extension to append to all files saved for uploading.
	 */
	public static final String EXTENSION = ".dat";

	/**
	 * Maximum length of time to run location listeners during each fix attempt.
	 * In milliseconds.
	 */
	public static final long LISTENER_WINDOW = 5 * 1000;

	public static final long LONG_LISTENER_WINDOW = 60 * 1000;

	public final static long SECONDS = 1000;
	public final static long MINUTES = SECONDS * 60;
	public final static long HOURS = MINUTES * 60;
	public final static long DAYS = HOURS * 24;
	public final static long WEEKS = DAYS * 7;

	// Min average comfortable walking speed (cm/s) from Bohannon 1997,
	// http://ageing.oxfordjournals.org/content/26/1/15.full.pdf+html
	public static int WALKING_SPEED = 127;

	// Use the distance one would cover at walking speed capped at 80 (which is
	// standard city block size)
	public static int getMinDist(Context context) {

		PropertyHolder.init(context);

		int fixIntervalSeconds = (int) ((int) PropertyHolder.getAlarmInterval() / (int) SECONDS);

		int expectedWalkingDistanceMeters = (int) (WALKING_SPEED * fixIntervalSeconds) / 100;
		return Math.min(MIN_DIST, expectedWalkingDistanceMeters);
	}

	public static int MIN_DIST = 80;

	/**
	 * Value at which a GPS location will be preferred to a network location,
	 * even if the network location is listed with a higher accuracy.
	 */
	public static final float MIN_GPS_ACCURACY = 50;

	/**
	 * Value at which a location will be used, and both listeners stopped even
	 * if not yet at the end of the listener window.
	 * 
	 * SETTING THIS FROM 15 TO 5 FOR FESTA
	 */
	public static final float OPT_ACCURACY = 5;

	/**
	 * Value at which a location will be used, and both listeners stopped even
	 * if not yet at the end of the listener window - for long runs.
	 */
	public static final float OPT_ACCURACY_LONGRUNS = 50;

	/**
	 * Minimum accuracy necessary for location to be used.
	 */
	public static final float MIN_ACCURACY = 500;

	/**
	 * Default time for storing user data when user selects to do so. In days.
	 */
	public static final int STORAGE_DAYS = 7;

	/**
	 * Dummy variable indicating whether application is currently trying to
	 * upload data.
	 */
	public static boolean uploading = false;

	/**
	 * Default value for figuring out when alarm manager started counting. For
	 * use with the display timer in the DriverMapActivity activity.
	 */
	public static long countingFrom = 0;

	public static long lastFixStartedAt = 0;

	/**
	 * counter for how many fixes have been missed in a row.
	 */
	public static int missedFixes = 0;

	/**
	 * temp holder for info on latest fix.
	 */
	public static String lastFixTimeStamp = null;

	/**
	 * temp holder for info on latest fix.
	 */
	public static long lastFixTime = 0;

	/**
	 * temp holder for info on latest fix.
	 */
	public static double lastFixLat = 0;

	/**
	 * temp holder for info on latest fix.
	 */
	public static double lastFixLon = 0;

	/**
	 * holder for current value of the listener window
	 */
	public static long listenerTimer = LISTENER_WINDOW;

	/**
	 * Surrounds the given string in quotation marks. Taken from Human Mobility
	 * Project code written by Chang Y. Chung and Necati E. Ozgencil.
	 * 
	 * @param str
	 *            The string to be encased in quotation marks.
	 * @return The given string trimmed and encased in quotation marks.
	 */
	public static String enquote(String str) {
		final String dq = "\"";
		final String ddq = dq + dq;
		StringBuilder sb = new StringBuilder("");
		sb.append(dq);
		sb.append((str.trim()).replace(dq, ddq));
		sb.append(dq);
		return sb.toString();
	}

	/**
	 * Formats the given coordinate and converts to String form. Taken from
	 * Human Mobility Project code written by Chang Y. Chung and Necati E.
	 * Ozgencil.
	 * 
	 * @param coord
	 *            The coordinate value to be formatted.
	 * @return The properly formatted coordinate in String form
	 */
	public static String fmtCoord(double coord) {
		return String.format("%1$11.6f", coord);
	}

	/**
	 * Formats the given time and converts to String form. Taken from Human
	 * Mobility Project code written by Chang Y. Chung and Necati E. Ozgencil.
	 * 
	 * @param time
	 *            The time value to be formatted.
	 * @return The properly formatted time value in String form
	 */
	public static String iso8601(long time) {
		return String.format("%1$tFT%1$tT", time);
	}

	/**
	 * Formats the given time and converts to String form. Taken from Human
	 * Mobility Project code written by Chang Y. Chung and Necati E. Ozgencil.
	 * 
	 * @param datetime
	 *            The Date object, whose long time value must be formatted.
	 * @return The properly formatted time value of the Date Object in String
	 *         form
	 */
	public static String iso8601(Date datetime) {
		return iso8601(datetime.getTime());
	}

	/**
	 * Formats a date object for displaying it to the user.
	 * 
	 * @param date
	 *            The Date object to be formatted.
	 * @return The properly formatted time and date as a String.
	 * 
	 */
	public static String userDate(Date date) {
		SimpleDateFormat s = new SimpleDateFormat("HH:mm dd/MM/yyyy");
		String format = s.format(date);
		return format;
	}

	/**
	 * Formats the location time, given as a long in milliseconds, for use in
	 * filenames.
	 * 
	 * @param locationTime
	 *            The long value to be formatted.
	 * @return The properly formatted time and date as a String.
	 */
	public static String fileNameDate(long locationTime) {
		Date date = new Date(locationTime);
		SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		String format = s.format(date);
		return format;
	}

	/**
	 * Gets the current system time in milliseconds. Taken from Human Mobility
	 * Project code written by Chang Y. Chung and Necati E. Ozgencil.
	 * 
	 * @return The current system time in milliseconds.
	 */
	public static String now() {
		return iso8601(System.currentTimeMillis());
	}

	/**
	 * Displays a brief message on the phone screen. Taken from Human Mobility
	 * Project code written by Chang Y. Chung and Necati E. Ozgencil.
	 * 
	 * @param context
	 *            Interface to application environment
	 * @param msg
	 *            The message to be displayed to the user
	 */
	public static void toast(Context context, String msg) {

		TextView tv = new TextView(context);
		tv.setText(msg);
		Drawable bknd = context.getResources().getDrawable(
				R.drawable.white_border);
		tv.setBackgroundDrawable(bknd);
		tv.setPadding(20, 20, 20, 20);
		tv.setTextSize(20);

		Toast t = new Toast(context);
		t.setDuration(Toast.LENGTH_LONG);
		t.setView(tv);
		t.show();
	}

	/**
	 * Checks if the phone has an internet connection.
	 * 
	 * @param context
	 *            The application context.
	 * @return True if phone has a connection; false if not.
	 */
	public static boolean isOnline(Context context) {

		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnected()) {
			return true;
		}
		return false;
	}

	public static boolean uploadFile(byte[] bytes, String filename,
			String uploadurl) {

		HttpURLConnection conn = null;
		DataOutputStream dos = null;
		// DataInputStream inStream = null;

		String lineEnd = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";

		int bytesRead, bytesAvailable, bufferSize;
		byte[] buffer;
		int maxBufferSize = 64 * 1024; // old value 1024*1024
		ByteArrayInputStream byteArrayInputStream = null;
		boolean isSuccess = true;
		try {
			// ------------------ CLIENT REQUEST

			byteArrayInputStream = new ByteArrayInputStream(bytes);

			// open a URL connection to the Servlet
			URL url = new URL(uploadurl);
			// Open a HTTP connection to the URL
			conn = (HttpURLConnection) url.openConnection();
			// Allow Inputs
			conn.setDoInput(true);
			// Allow Outputs
			conn.setDoOutput(true);
			// Don't use a cached copy.
			conn.setUseCaches(false);
			// set timeout
			conn.setConnectTimeout(60000);
			conn.setReadTimeout(60000);
			// Use a post method.
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("Content-Type",
					"multipart/form-data;boundary=" + boundary);

			dos = new DataOutputStream(conn.getOutputStream());
			dos.writeBytes(twoHyphens + boundary + lineEnd);
			dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\""
					+ filename + "\"" + lineEnd);
			dos.writeBytes(lineEnd);

			// create a buffer of maximum size
			bytesAvailable = byteArrayInputStream.available();
			bufferSize = Math.min(bytesAvailable, maxBufferSize);
			buffer = new byte[bufferSize];

			// read file and write it into form...
			bytesRead = byteArrayInputStream.read(buffer, 0, bufferSize);
			while (bytesRead > 0) {
				dos.write(buffer, 0, bufferSize);
				bytesAvailable = byteArrayInputStream.available();
				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				bytesRead = byteArrayInputStream.read(buffer, 0, bufferSize);
			}

			// send multipart form data necesssary after file data...
			dos.writeBytes(lineEnd);
			dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

			// close streams
			// Log.e(TAG,"UploadService Runnable:File is written");
			// fileInputStream.close();
			// dos.flush();
			// dos.close();
		} catch (Exception e) {
			// Log.e(TAG, "UploadService Runnable:Client Request error", e);
			isSuccess = false;
		} finally {
			if (dos != null) {
				try {
					dos.close();
				} catch (IOException e) {
					// Log.e(TAG, "exception" + e);

				}
			}
			if (byteArrayInputStream != null) {
				try {
					byteArrayInputStream.close();
				} catch (IOException e) {
					// Log.e(TAG, "exception" + e);

				}
			}

		}

		// ------------------ read the SERVER RESPONSE
		try {

			if (conn.getResponseCode() != 200) {
				isSuccess = false;
			}
		} catch (IOException e) {
			// Log.e(TAG, "Connection error", e);
			isSuccess = false;
		}

		return isSuccess;
	}

	public static boolean uploadJSONArray(Context context, JSONArray jsonArray,
			String uploadurl) {

		String response = "";

		try {

			// Create a new HttpClient and Post Header
			HttpPatch httppatch = new HttpPatch(uploadurl);

			HttpParams myParams = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(myParams, 10000);
			HttpConnectionParams.setSoTimeout(myParams, 60000);
			HttpConnectionParams.setTcpNoDelay(myParams, true);

			httppatch.setHeader("Content-type", "application/json");

			String auth = "-u john:1234";

			httppatch.setHeader("Authorization", auth);

			HttpClient httpclient = new DefaultHttpClient();

			ByteArrayEntity bae = new ByteArrayEntity(jsonArray.toString()
					.getBytes("UTF8"));

			// StringEntity se = new StringEntity(jsonArray.toString(),
			// HTTP.UTF_8);
			// se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
			// "application/json"));
			bae.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
					"application/json"));
			httppatch.setEntity(bae);

			// Execute HTTP Post Request

			ResponseHandler<String> responseHandler = new BasicResponseHandler();

			response = httpclient.execute(httppatch, responseHandler);

		} catch (ClientProtocolException e) {

		} catch (IOException e) {
		}

		if (response.contains("SUCCESS")) {
			Log.i("SERVER RESPONSE", response);
			return true;
		} else {
			Log.i("SERVER RESPONSE", response);
			return false;
		}

	}

	/**
	 * Saves a byte array to the internal storage directory.
	 * 
	 * @param context
	 *            The application context.
	 * @param filename
	 *            The file name to use.
	 * @param bytes
	 *            The byte array to be saved.
	 */
	public static void saveJSON(Context context, String dir, String fileprefix,
			String inputString) {
		// String TAG = "Util.saveFile";
		FileOutputStream fos = null;
		FileLock lock = null;

		try {
			byte[] bytes = inputString.getBytes("UTF-8");

			File directory = new File(context.getFilesDir().getAbsolutePath(),
					dir);

			directory.mkdirs();

			File target = new File(directory, fileprefix
					+ System.currentTimeMillis() + ".txt");
			fos = new FileOutputStream(target);

			lock = fos.getChannel().lock();


			fos.write(bytes);

		} catch (IOException e) {
			// logging exception but doing nothing
			// Log.e(TAG, "Exception " + e);
		} finally {

			if (lock != null) {

				try {
					lock.release();
				} catch (Exception e) {

				}
			}
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					// logging exception but doing nothing
					// Log.e(TAG, "Exception " + e);

				}
			}

		}

	}

	public static String readJSON(File file) {
		int bytesRead, bytesAvailable, bufferSize;
		byte[] buffer;
		int maxBufferSize = 64 * 1024; // old value 1024*1024
		FileInputStream fileInputStream = null;
		String result = "";

		try {
			fileInputStream = new FileInputStream(file);

			bytesAvailable = fileInputStream.available();

			bufferSize = Math.min(bytesAvailable, maxBufferSize);
			buffer = new byte[bufferSize];

			// read file and write it into form...
			bytesRead = fileInputStream.read(buffer, 0, bufferSize);
			while (bytesRead > 0) {
				bytesAvailable = fileInputStream.available();
				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				bytesRead = fileInputStream.read(buffer, 0, bufferSize);
			}
			result = new String(buffer, "UTF-8");

		} catch (FileNotFoundException e) {

		}

		catch (IOException e) {
		}

		finally {
			if (fileInputStream != null) {
				try {
					fileInputStream.close();
				} catch (IOException e) {

				}
			}

		}

		return result;

	}

	public File[] listJSONFiles(Context context) {
		String dir = context.getFilesDir().getAbsolutePath();
		File directory = new File(dir, DIRECTORY_JSON_UPLOADQUEUE);
		File[] files = directory.listFiles();
		return files;
	}

	public static boolean patch2DjangoJSONArray(Context context,
			JSONArray jsonArray, String uploadurl, String username,
			String password) {

		String response = "";

		try {

			JSONObject obj = new JSONObject();
			obj.put("objects", jsonArray);
			JSONArray emptyarray = new JSONArray();
			obj.put("deleted_objects", emptyarray);


			CredentialsProvider credProvider = new BasicCredentialsProvider();
			credProvider.setCredentials(new AuthScope(AuthScope.ANY_HOST,
					AuthScope.ANY_PORT), new UsernamePasswordCredentials(
					username, password));

			// Create a new HttpClient and Post Header
			HttpPatch httppatch = new HttpPatch(uploadurl);

			HttpParams myParams = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(myParams, 10000);
			HttpConnectionParams.setSoTimeout(myParams, 60000);
			HttpConnectionParams.setTcpNoDelay(myParams, true);

			httppatch.setHeader("Content-type", "application/json");

			DefaultHttpClient httpclient = new DefaultHttpClient();

			httpclient.setCredentialsProvider(credProvider);
			// ByteArrayEntity bae = new ByteArrayEntity(obj.toString()
			// .getBytes("UTF8"));

			StringEntity se = new StringEntity(obj.toString(), HTTP.UTF_8);
			se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
					"application/json"));
			// bae.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
			// "application/json"));
			httppatch.setEntity(se);

			// Execute HTTP Post Request

			HttpResponse httpResponse = httpclient.execute(httppatch);

			response = httpResponse.getStatusLine().toString();



		} catch (ClientProtocolException e) {

		
		} catch (IOException e) {

		
		} catch (JSONException e) {
			e.printStackTrace();
		}

		if (response.contains("ACCEPTED")) {
			return true;
		} else {
			return false;
		}

	}
	

	public static int getBatteryLevel(Context context) {
		Intent batteryIntent = context.registerReceiver(null, new IntentFilter(
				Intent.ACTION_BATTERY_CHANGED));
		int level = batteryIntent.getIntExtra("level", -1);
		int scale = batteryIntent.getIntExtra("scale", -1);

		// Error checking that probably isn't needed but I added just in case.
		if (level == -1 || scale == -1) {
			return -1;
		}

		int powerLevel = (int) Math.round(level * 100.0 / scale);

		return powerLevel;
	}


}
