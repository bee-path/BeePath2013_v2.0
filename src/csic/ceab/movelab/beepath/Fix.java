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

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

/**
 * Defines map point objects used in DriverMapActivity.
 * 
 * @author John R.B. Palmer
 * 
 */
public class Fix {

	String tripid;
	double lat;
	double lng;
	double alt;
	float acc;
	String prov;
	long time;
	int pow;

	Fix(String _tripid, double _lat, double _lng, double _alt, float _acc,
			String _prov, long _time, int _pow) {

		tripid = _tripid;
		lat = _lat;
		lng = _lng;
		alt = _alt;
		acc = _acc;
		prov = _prov;
		time = _time;
		pow = _pow;

	}

	public JSONObject exportJSON(Context context) {

		PropertyHolder.init(context);

		JSONObject object = new JSONObject();
		try {
			object.put("userid", PropertyHolder.getUserId());
			object.put("tripid", this.tripid);
			object.put("lat", String.valueOf(this.lat));
			object.put("lon", String.valueOf(this.lng));
			object.put("alt", String.valueOf(this.alt));
			object.put("acc", String.valueOf(this.acc));
			object.put("provider", String.valueOf(this.prov));
			object.put("timestamp", String.valueOf(this.time));
			object.put("pow_bat", String.valueOf(this.pow));

		} catch (JSONException e) {
		}
		return object;
	}

	public String makeFileName(Context context) {

		PropertyHolder.init(context);
		return PropertyHolder.getUserId() + "_" + this.tripid + "_"
				+ Util.fileNameDate(this.time);
	}

	public boolean uploadJSON(Context context) {


		String response = "";

		String uploadurl = Util.URL_FIXES_JSON;

		try {

			// Create a new HttpClient and Post Header
			HttpPost httppost = new HttpPost(uploadurl);

			HttpParams myParams = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(myParams, 10000);
			HttpConnectionParams.setSoTimeout(myParams, 60000);
			HttpConnectionParams.setTcpNoDelay(myParams, true);

			httppost.setHeader("Content-type", "application/json");
			HttpClient httpclient = new DefaultHttpClient();

			StringEntity se = new StringEntity(exportJSON(context).toString(),
					HTTP.UTF_8);
			se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
					"application/json"));
			httppost.setEntity(se);

			// Execute HTTP Post Request

			ResponseHandler<String> responseHandler = new BasicResponseHandler();

			response = httpclient.execute(httppost, responseHandler);

		} catch (ClientProtocolException e) {


		} catch (IOException e) {
		}

		if (response.contains("SUCCESS")) {

			return true;
		} else {
			return false;
		}

	}

}
