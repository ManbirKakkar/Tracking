package com.agnext.trackingutil.services;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.*;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import com.agnext.trackingutil.R;

import java.util.List;
import java.util.Locale;

public class GPSService extends Service implements LocationListener {

	private final Context mContext;
	boolean isGPSEnabled = false;
	boolean isNetworkEnabled = false;
	public boolean isLocationAvailable = false;
	Location mLocation;
	double mLatitude;
	double mLongitude;

	private static final long TIME = 3000;
	private static final long DISTANCE = 10;
	protected LocationManager mLocationManager;

	public GPSService(Context context) {
		this.mContext = context;
		mLocationManager = (LocationManager) mContext
				.getSystemService(LOCATION_SERVICE);

	}

	/**
	 * Returs the Location
	 *
	 * @return Location or null if no location is found
	 */
	@SuppressLint("MissingPermission")
	public Location getLocation() {
		try {

			// Getting GPS status
			isGPSEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
			isNetworkEnabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
			// If GPS enabled, get latitude/longitude using GPS Services
			if (isGPSEnabled) {
				mLocationManager.requestLocationUpdates(
						LocationManager.GPS_PROVIDER, TIME, DISTANCE, this);
				if (mLocationManager != null) {
					mLocation = mLocationManager
							.getLastKnownLocation(LocationManager.GPS_PROVIDER);
					if (mLocation != null) {
						mLatitude = mLocation.getLatitude();
						mLongitude = mLocation.getLongitude();
						isLocationAvailable = true; // setting a flag that
						// location is available
						return mLocation;
					}
				}
			}

			// If we are reaching this part, it means GPS was not able to fetch
			// any location
			// Getting network status
			isNetworkEnabled = mLocationManager
					.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

			if (isNetworkEnabled) {
				mLocationManager.requestLocationUpdates(
						LocationManager.NETWORK_PROVIDER, TIME, DISTANCE, this);
				if (mLocationManager != null) {
					mLocation = mLocationManager
							.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
					if (mLocation != null) {
						mLatitude = mLocation.getLatitude();
						mLongitude = mLocation.getLongitude();
						isLocationAvailable = true; // setting a flag that
						// location is available
						return mLocation;
					}
				}
			}
			// If reaching here means, we were not able to get location neither
			// from GPS not Network,
			if (!isGPSEnabled) {
				// so asking user to open GPS
				askUserToOpenGPS();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		// if reaching here means, location was not available, so setting the
		// flag as false
		isLocationAvailable = false;
		return null;
	}


	/**
	 * get latitude
	 *
	 * @return latitude in double
	 */
	public double getLatitude() {
		if (mLocation != null) {
			mLatitude = mLocation.getLatitude();
		}
		return mLatitude;
	}

	/**
	 * get longitude
	 *
	 * @return longitude in double
	 */
	public double getLongitude() {
		if (mLocation != null) {
			mLongitude = mLocation.getLongitude();
		}
		return mLongitude;
	}

	/**
	 * close GPS to save battery
	 */
	public void closeGPS() {
		if (mLocationManager != null) {
			mLocationManager.removeUpdates(GPSService.this);
		}
	}

	/**
	 * show settings to open GPS
	 */
	public void askUserToOpenGPS() {
		AlertDialog.Builder mAlertDialog = new AlertDialog.Builder(mContext);

		// Setting Dialog Title
		mAlertDialog.setTitle(R.string.location_no_available_Open_GPS)
				.setMessage(R.string.activate_GPS_to_use_use_location_services)
				.setPositiveButton(R.string.open_settings, (dialog, which) -> {
					Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
					mContext.startActivity(intent);
				})
				.setNegativeButton(R.string.cancel, (dialog, which) -> dialog.cancel()).show();
	}

	/**
	 * Updating the location when location changes
	 */
	@Override
	public void onLocationChanged(Location location) {
		mLatitude = location.getLatitude();
		mLongitude = location.getLongitude();
	}

	@Override
	public void onProviderDisabled(String provider) {
	}

	@Override
	public void onProviderEnabled(String provider) {
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	public String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
		String strAdd = "";
		Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
		try {
			List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
			if (addresses != null) {
				Address returnedAddress = addresses.get(0);
				StringBuilder strReturnedAddress = new StringBuilder("");

				for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
					strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
				}
				strAdd = strReturnedAddress.toString();
				Log.e("loction address", "" + strReturnedAddress.toString());
			} else {
				Log.e("loction address", "No Address returned!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strAdd;
	}
}
