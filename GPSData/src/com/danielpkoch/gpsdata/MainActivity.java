package com.danielpkoch.gpsdata;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

public class MainActivity extends Activity
{

	private LocationManager locationManager;
	
	private TextView provider;
	private TextView updates;
	private TextView longitude;
	private TextView latitude;
	private TextView bearing;
	private TextView speed;
	private TextView altitude;
	private TextView accuracy;
	private TextView time;
	
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        provider = (TextView) findViewById(R.id.provider);
        updates = (TextView) findViewById(R.id.updates);
        longitude = (TextView) findViewById(R.id.longitude);
        latitude = (TextView) findViewById(R.id.latitude);
        bearing = (TextView) findViewById(R.id.bearing);
        speed = (TextView) findViewById(R.id.speed);
        altitude = (TextView) findViewById(R.id.altitude);
        accuracy = (TextView) findViewById(R.id.accuracy);
        time = (TextView) findViewById(R.id.time);
        
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }
    
    private LocationListener locationListener = new LocationListener()
    {
    	private int updates_count;
    	//private long last_time_ns = SystemClock.elapsedRealtimeNanos();
    	private long last_time_ms = 0;
    	
    	public void onLocationChanged(Location location)
    	{
    		updates_count++;
    		
    		provider.setText(location.getProvider());
    		updates.setText(String.format("%d", updates_count));
    		latitude.setText(formatMeasurement(location.getLatitude(), 8, R.string.unit_deg));
    		longitude.setText(formatMeasurement(location.getLongitude(), 8, R.string.unit_deg));
    		bearing.setText(formatMeasurement(location.getBearing(), 2, R.string.unit_deg));
    		speed.setText(formatMeasurement(location.getSpeed(), 2, R.string.unit_meters_per_sec));
    		altitude.setText(formatMeasurement(location.getAltitude(), 2, R.string.unit_meter));
    		accuracy.setText(formatMeasurement(location.getAccuracy(), 4, R.string.unit_meter));
    		
    		//long this_time_ns = location.getElapsedRealtimeNanos();
    		//double time_delta = (this_time_ns - last_time_ns) / 1e9;
    		//last_time_ns = this_time_ns;
    		long this_time_ms = location.getTime();
    		double time_delta = (this_time_ms - last_time_ms) / 1e3;
    		last_time_ms = this_time_ms;
    		
    		time.setText(String.format("%.2f%s", time_delta, getResources().getString(R.string.unit_sec)));
    	}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {}

		@Override
		public void onProviderDisabled(String provider) {}

		@Override
		public void onProviderEnabled(String provider) {}
    };
    
    private String formatMeasurement(double value, int precision, int unit)
    {
    	return Html.fromHtml(String.format("%." + precision + "f%s", value, getResources().getString(unit))).toString();
    }
    
}
