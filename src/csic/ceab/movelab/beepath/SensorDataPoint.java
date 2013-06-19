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

public class SensorDataPoint {

	String tripid;
	String sensor;
	int sensortype;
	float maxrange;
	int mindelay;
	float sensorpower;
	float resolution;
	String vendor;
	int version;
	int accuracy;
	long time;
	float[] values;

	public SensorDataPoint(String _tripid, String _sensor, int _type,
			float _maxrange, int _mindelay, float _sensorpower,
			float _resolution, String _vendor, int _version, int _accuracy,
			long _time, float[] _values) {

		tripid = _tripid;
		sensor = _sensor;
		sensortype = _type;
		maxrange = _maxrange;
		mindelay = _mindelay;
		sensorpower = _sensorpower;
		resolution = _resolution;
		vendor = _vendor;
		version = _version;
		accuracy = _accuracy;
		time = _time;
		values = _values;
	}

	public JSONObject exportJSON(Context context) {

		PropertyHolder.init(context);

		JSONObject object = new JSONObject();
		try {
			object.put("userid", PropertyHolder.getUserId());
			object.put("tripid", this.tripid);
			object.put("accuracy", String.valueOf(this.accuracy));
			object.put("sensorname", this.sensor);
			object.put("sensortype", String.valueOf(this.sensortype));
			object.put("maxrange", String.valueOf(this.maxrange));
			object.put("mindelay", String.valueOf(this.mindelay));
			object.put("res", String.valueOf(this.resolution));
			object.put("vendor", this.vendor);
			object.put("version", String.valueOf(this.version));
			object.put("sensorpower", String.valueOf(this.sensorpower));
			object.put("timestamp", String.valueOf(this.time));
			String[] vs = { "v1", "v2", "v3", "v4" };
			int i = 0;
			for (float v : this.values) {
				object.put(vs[i], String.valueOf(v));
				i++;
			}
		} catch (JSONException e) {
		}
		return object;
	}

	public boolean uploadJSON(Context context) {

		String response = "";

		String uploadurl = Util.URL_SENSOR_JSON;

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
