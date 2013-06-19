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

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

/**
 * Uploads files to the server.
 * 
 * @author John R.B. Palmer
 * 
 */
public class FileUploader extends Service {
	private boolean uploading = false;

	Context context;
	boolean sensorUploadsNeeded = true;
	boolean trackUploadsNeeded = true;
	boolean sensorErrorUploadsNeeded = true;
	boolean trackErrorUploadsNeeded = true;

	@Override
	public void onStart(Intent intent, int startId) {

		if (!uploading) {
			uploading = true;

			Thread uploadThread = new Thread(null, doFileUploading,
					"uploadBackground");
			uploadThread.start();

		}
	};

	private Runnable doFileUploading = new Runnable() {
		public void run() {
			tryUploads();
		}
	};

	@Override
	public void onCreate() {

		context = getApplicationContext();
		if (PropertyHolder.isInit() == false)
			PropertyHolder.init(context);
	}

	@Override
	public void onDestroy() {

	}

	private void tryUploads() {

		if (Util.isOnline(context)) {

			// //////////
			// FIX ARRAYS
			// //////////

			File[] files = null;

			File directory = new File(context.getFilesDir().getAbsolutePath(),
					Util.DIRECTORY_JSON_FIXARRAY_UPLOADQUEUE);
			directory.mkdirs();

			files = directory.listFiles();

			if (files.length > 0) {

				JSONArray fixJSONArray = new JSONArray();

				for (File f : files) {

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

				}

				if (fixJSONArray.length() > 0) {

					if (!Util.patch2DjangoJSONArray(context, fixJSONArray,
							Util.URL_FIXES_JSON, Util.getUserId(),
							Util.getPassword())) {

						Util.saveJSON(context,
								Util.DIRECTORY_JSON_UPLOAD_ERROR_QUEUE_FIXES,
								Util.FILEPREFIX_UPLOAD_ERRORS,
								fixJSONArray.toString());

					}
				}
			}

			// now sensors

			files = null;

			directory = new File(context.getFilesDir().getAbsolutePath(),
					Util.DIRECTORY_JSON_UPLOADQUEUE);
			directory.mkdirs();

			files = directory.listFiles();

			if (files.length > 0) {

				for (File f : files) {

					JSONArray thisArray;
					try {
						thisArray = new JSONArray(Util.readJSON(f));

						if (Util.patch2DjangoJSONArray(context, thisArray,
								Util.URL_SENSOR_JSON, Util.getUserId(),
								Util.getPassword())) {
							f.delete();
						}

					} catch (JSONException e) {
						e.printStackTrace();
					}

				}

			}

			// now fixes saved after upload errors
			files = null;
			directory = null;
			directory = new File(context.getFilesDir().getAbsolutePath(),
					Util.DIRECTORY_JSON_UPLOAD_ERROR_QUEUE_FIXES);
			directory.mkdirs();
			files = directory.listFiles();
			if (files.length > 0) {
				for (File f : files) {
					try {
						JSONArray ja = new JSONArray(Util.readJSON(f));
						if (Util.uploadJSONArray(context, ja,
								Util.URL_FIXES_JSON)) {
							f.delete();

						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

		}
		uploading = false;
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
