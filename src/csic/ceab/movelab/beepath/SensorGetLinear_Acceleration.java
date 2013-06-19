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
 **/

package csic.ceab.movelab.beepath;

import java.util.List;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.util.Log;

/**
 * Defines map point objects used in DriverMapActivity.
 * 
 * @author John R.B. Palmer
 * 
 */
public class SensorGetLinear_Acceleration extends SensorGet {

	private SensorManager mSensorManager;

	@Override
	void registerListener() {
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		@SuppressWarnings("deprecation")
		List<Sensor> deviceSensors = mSensorManager
				.getSensorList(Sensor.TYPE_LINEAR_ACCELERATION);

		if (deviceSensors != null && deviceSensors.size() > 0) {
			int chooseIndex = 0;
			int i = 0;
			for (Sensor s : deviceSensors) {
				if (s.getMaximumRange() > deviceSensors.get(chooseIndex)
						.getMaximumRange()) {
					chooseIndex = i;
				}
				i++;
			}
			mSensorManager.registerListener(this,
					deviceSensors.get(chooseIndex),
					PropertyHolder.sensorDelayLINEAR_ACCELERATION());
		}

	}

	@Override
	void unregisterListener() {
		mSensorManager.unregisterListener(this);

	}

	@Override
	String makeFilePrefix() {

		return "LA";

	}

}
