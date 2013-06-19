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

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.hardware.SensorManager;

/**
 * Manipulates the application's shared preferences, values that must persist
 * throughout the application's installed lifetime.
 * 
 * @author John R.B. Palmer
 */
public class PropertyHolder {
	private static SharedPreferences sharedPreferences;
	private static Editor editor;

	/**
	 * Initialize the shared preferences handle.
	 * 
	 * @param context
	 *            Interface to application environment
	 */
	public static void init(Context context) {
		sharedPreferences = context.getSharedPreferences("PROPERTIES",
				Context.MODE_PRIVATE);
		editor = sharedPreferences.edit();
	}

	public static boolean isInit() {
		return sharedPreferences != null;
	}

	public static void deleteAll() {
		editor.clear();
		editor.commit();
	}


	public static String PASSWORD = "PASSWORD";
	public static String getPassword() {
		return sharedPreferences.getString(PASSWORD,
				null);
	}
	public static void setPassword(String password) {
		editor.putString(PASSWORD, password);
		editor.commit();
	}

	
	public static String USE_SENSOR_ACCELEROMETER = "USE_SENSOR_ACCELEROMETER";
	public static boolean useSensorACCELEROMETER() {
		return sharedPreferences.getBoolean(USE_SENSOR_ACCELEROMETER,
				false);
	}
	public static void useSensorACCELEROMETER(boolean use) {
		editor.putBoolean(USE_SENSOR_ACCELEROMETER, use);
		editor.commit();
	}
	public static String USE_SENSOR_ORIENTATION = "USE_SENSOR_ORIENTATION";
	public static boolean useSensorORIENTATION() {
		return sharedPreferences.getBoolean(USE_SENSOR_ORIENTATION,
				false);
	}
	public static void useSensorORIENTATION(boolean use) {
		editor.putBoolean(USE_SENSOR_ORIENTATION, use);
		editor.commit();
	}
	public static String USE_SENSOR_MAGNETIC_FIELD = "USE_SENSOR_MAGNETIC_FIELD";
	public static boolean useSensorMAGNETIC_FIELD() {
		return sharedPreferences.getBoolean(USE_SENSOR_MAGNETIC_FIELD,
				false);
	}
	public static void useSensorMAGNETIC_FIELD(boolean use) {
		editor.putBoolean(USE_SENSOR_MAGNETIC_FIELD, use);
		editor.commit();
	}
	public static String USE_SENSOR_GYROSCOPE = "USE_SENSOR_GYROSCOPE";
	public static boolean useSensorGYROSCOPE() {
		return sharedPreferences.getBoolean(USE_SENSOR_GYROSCOPE,
				false);
	}
	public static void useSensorGYROSCOPE(boolean use) {
		editor.putBoolean(USE_SENSOR_GYROSCOPE, use);
		editor.commit();
	}
	public static String USE_SENSOR_LINEAR_ACCELERATION = "USE_SENSOR_LINEAR_ACCELERATION";
	public static boolean useSensorLINEAR_ACCELERATION() {
		return sharedPreferences.getBoolean(USE_SENSOR_LINEAR_ACCELERATION,
				false);
	}
	public static void useSensorLINEAR_ACCELERATION(boolean use) {
		editor.putBoolean(USE_SENSOR_LINEAR_ACCELERATION, use);
		editor.commit();
	}
	public static String USE_SENSOR_GRAVITY = "USE_SENSOR_GRAVITY";
	public static boolean useSensorGRAVITY() {
		return sharedPreferences.getBoolean(USE_SENSOR_GRAVITY,
				false);
	}
	public static void useSensorGRAVITY(boolean use) {
		editor.putBoolean(USE_SENSOR_GRAVITY, use);
		editor.commit();
	}

	
	
	
	public static String SENSOR_DELAY_ACCELEROMETER = "SENSOR_DELAY_ACCELEROMETER";

	public static int sensorDelayACCELEROMETER() {
		return sharedPreferences.getInt(SENSOR_DELAY_ACCELEROMETER,
				SensorManager.SENSOR_DELAY_NORMAL);
	}

	public static void sensorDelayACCELEROMETER(int delay) {
		editor.putInt(SENSOR_DELAY_ACCELEROMETER, delay);
		editor.commit();
	}

	
	public static String SENSOR_DELAY_MAGNETIC_FIELD = "SENSOR_DELAY_MAGNETIC_FIELD";

	public static int sensorDelayMAGNETIC_FIELD() {
		return sharedPreferences.getInt(SENSOR_DELAY_MAGNETIC_FIELD,
				SensorManager.SENSOR_DELAY_NORMAL);
	}

	public static void sensorDelayMAGNETIC_FIELD(int delay) {
		editor.putInt(SENSOR_DELAY_MAGNETIC_FIELD, delay);
		editor.commit();
	}

	
	public static String SENSOR_DELAY_ORIENTATION = "SENSOR_DELAY_ORIENTATION";

	public static int sensorDelayORIENTATION() {
		return sharedPreferences.getInt(SENSOR_DELAY_ORIENTATION,
				SensorManager.SENSOR_DELAY_NORMAL);
	}

	public static void sensorDelayORIENTATION(int delay) {
		editor.putInt(SENSOR_DELAY_ORIENTATION, delay);
		editor.commit();
	}

	public static String SENSOR_DELAY_LINEAR_ACCELERATION = "SENSOR_DELAY_LINEAR_ACCELERATION";

	public static int sensorDelayLINEAR_ACCELERATION() {
		return sharedPreferences.getInt(SENSOR_DELAY_LINEAR_ACCELERATION,
				SensorManager.SENSOR_DELAY_NORMAL);
	}

	public static void sensorDelayLINEAR_ACCELERATION(int delay) {
		editor.putInt(SENSOR_DELAY_LINEAR_ACCELERATION, delay);
		editor.commit();
	}

	public static String SENSOR_DELAY_GYROSCOPE = "SENSOR_DELAY_GYROSCOPE";

	public static int sensorDelayGYROSCOPE() {
		return sharedPreferences.getInt(SENSOR_DELAY_GYROSCOPE,
				SensorManager.SENSOR_DELAY_NORMAL);
	}

	public static void sensorDelayGYROSCOPE(int delay) {
		editor.putInt(SENSOR_DELAY_GYROSCOPE, delay);
		editor.commit();
	}

	public static String SENSOR_DELAY_GRAVITY = "SENSOR_DELAY_GRAVITY";

	public static int sensorDelayGRAVITY() {
		return sharedPreferences.getInt(SENSOR_DELAY_GRAVITY,
				SensorManager.SENSOR_DELAY_NORMAL);
	}

	public static void sensorDelayGRAVITY(int delay) {
		editor.putInt(SENSOR_DELAY_GRAVITY, delay);
		editor.commit();
	}

	
	
	public static long tripStartTime() {
		return sharedPreferences.getLong("TRIP_START_TIME", 0);
	}

	public static void tripStartTime(long start_time) {
		editor.putLong("TRIP_START_TIME", start_time);
		editor.commit();
	}

	public static boolean uploadsNeeded() {
		return sharedPreferences.getBoolean("UPLOADS_NEEDED", false);
	}

	public static void uploadsNeeded(boolean _uploads_needed) {
		editor.putBoolean("UPLOADS_NEEDED", _uploads_needed);
		editor.commit();
	}

	public static boolean isActivated() {
		return sharedPreferences.getBoolean("ACTIVATED", false);
	}

	public static void setActivated(boolean _activated) {
		editor.putBoolean("ACTIVATED", _activated);
		editor.commit();
	}

	public static String getTripId() {
		return sharedPreferences.getString("TRIP_ID", null);
	}

	public static void setTripId(String _tripId) {
		editor.putString("TRIP_ID", _tripId);
		editor.commit();
	}

	public static void setIntro(boolean intro) {
		editor.putBoolean("INTRO", intro);
		editor.commit();
	}

	public static boolean getIntro() {
		return sharedPreferences.getBoolean("INTRO", true);
	}

	public static void setAlarmInterval(long alarmInterval) {
		editor.putLong("ALARM_INTERVAL", alarmInterval);
		editor.commit();
	}

	public static long getAlarmInterval() {
		long interval = sharedPreferences.getLong("ALARM_INTERVAL", -1);
		if (interval == -1) {
			interval = Util.ALARM_INTERVAL;
			PropertyHolder.setAlarmInterval(interval);
		}
		return interval;
	}

	/**
	 * Checks if alarm service is scheduled to run the FixGet service/if the
	 * FixGet service is currently running. Returns a default value of
	 * <code>false</code> if the SERVICE_ON flag has not been explicitly set
	 * previously.
	 * 
	 * @return <code>true</code> if the FixGet service is scheduled and running,
	 *         <code>false</code> if the FixGet service is currently stopped.
	 */
	public static boolean isServiceOn() {
		return sharedPreferences.getBoolean("SERVICE_ON", false);
	}

	/**
	 * Sets the SERVICE_ON flag in the shared preferences to the given boolean
	 * value.
	 * 
	 * @param _isOn
	 *            The boolean value to which to set the SERVICE_ON flag.
	 */
	public static void setServiceOn(boolean _isOn) {
		editor.putBoolean("SERVICE_ON", _isOn);
		editor.commit();
	}

	/**
	 * Checks if a user is currently logged in to the DriverMapActivity
	 * application. Returns a default value of <code>false</code> if the
	 * IS_REGISTERED flag has not been explicitly set previously.
	 * 
	 * @return <code>true</code> if a user is currently logged in to the
	 *         DriverMapActivity application, <code>false</code> if no user is
	 *         logged in.
	 */
	public static boolean isRegistered() {
		return sharedPreferences.getBoolean("IS_REGISTERED", false);
	}

	/**
	 * Sets the IS_REGISTERED flag in the shared preferences to the given
	 * boolean value.
	 * 
	 * @param _isRegistered
	 *            The boolean value to which to set the IS_REGISTERED flag.
	 */
	public static void setRegistered(boolean _isRegistered) {
		editor.putBoolean("IS_REGISTERED", _isRegistered);
		editor.commit();
	}

	/**
	 * Gets the user ID stored in shared preferences. This user ID refers to the
	 * unique row ID for this user in the User table of the PMP mobility
	 * database. Returns a default value of -1 if the USER_ID flag has not been
	 * explicitly set previously.
	 * 
	 * @return The logged in user's user ID if a user is logged in, -1 if no one
	 *         is logged in.
	 */
	public static String getUserId() {
		return sharedPreferences.getString("USER_ID", null);
	}

	/**
	 * Sets the USER_ID in the shared preferences to the given value.
	 * 
	 * @param _userId
	 *            The value to which to set the USER_ID.
	 */
	public static void setUserId(String _userId) {
		editor.putString("USER_ID", _userId);
		editor.commit();
	}

}