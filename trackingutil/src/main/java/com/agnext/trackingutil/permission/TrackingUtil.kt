package com.agnext.trackingutil.permission

import android.Manifest
import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.agnext.trackingutil.R
import com.agnext.trackingutil.services.GPSService
import com.agnext.trackingutil.services.TrackingService

open class TrackingUtil constructor(private val mContext: Context): AppCompatActivity() {


    /** Get current latitude*/
    val latitude: String
        get() {
            val gps = GPSService(mContext)
            gps.location
            return gps.latitude.toString()
        }

    /** Get current longitude*/
    val longitude: String
        get() {
            val gps = GPSService(mContext)
            gps.location
            return gps.longitude.toString()
        }

    /** Start tracking with default socketURl, UserId, TimeInterval*/
    fun startTracking() {
        var  mServiceIntent = Intent(mContext, TrackingService::class.java)
        mContext.startService(mServiceIntent)

    }

    /** End Tracking*/
    fun stopTracking(){

    }

    /** Start tracking with added socketURl, UserId, TimeInterval*/
    fun startTracking(timeInterval: Int, userId:String, url:String){
        var  mServiceIntent = Intent(mContext, TrackingService::class.java)
        mServiceIntent.putExtra("TIME", timeInterval)
        mServiceIntent.putExtra("USER_ID", userId)
        mServiceIntent.putExtra("SOCKET_URL", url)
        mContext.startService(mServiceIntent)
    }

    /** Start tracking with added TimeInterval only*/
    fun startTracking(timeInterval: Int){
        var  mServiceIntent = Intent(mContext, TrackingService::class.java)
        mServiceIntent.putExtra("TIME", timeInterval)
        mContext.startService(mServiceIntent)
    }


    /** Check if location permission is enabled */
    fun isLocationPermission(): Boolean {
        val result = ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION)
        val result2 = ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION)
        return result == PackageManager.PERMISSION_GRANTED && result2 == PackageManager.PERMISSION_GRANTED
    }

    /** Request for location permission */
    fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            mContext as Activity,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
            REQUEST_LOCATION
        )
    }

    /** Return for Request Permission */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {


            REQUEST_LOCATION -> if (grantResults.isNotEmpty()) {

                val locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED
                val locationAccepted2 = grantResults[1] == PackageManager.PERMISSION_GRANTED

                if (locationAccepted && locationAccepted2) {

                    showToast(this, "Permission Granted, Now you can access location data")
                    (mContext as Activity).recreate()
                } else {
                    showToast(this, "Permission Denied, You cannot access Location data")

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                            showMessageOKCancel(getString(R.string.allow_permission),
                                DialogInterface.OnClickListener { dialog, which ->
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        requestPermissions(
                                            arrayOf(
                                                Manifest.permission.ACCESS_FINE_LOCATION,
                                                Manifest.permission.ACCESS_COARSE_LOCATION
                                            ),
                                            REQUEST_LOCATION
                                        )
                                    }
                                })
                            return
                        }
                    }

                }
            }


        }
    }

    /** Show Alert Message */
    private fun showMessageOKCancel(message: String, okListener: DialogInterface.OnClickListener) {
        AlertDialog.Builder(this)
            .setMessage(message)
            .setPositiveButton(R.string.ok, okListener)
            .setNegativeButton(R.string.cancel, null)
            .create()
            .show()
    }

    /** Show Toast */
    private fun showToast(context: Context, msg:String){
        Toast.makeText(context, "$msg", Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val REQUEST_LOCATION = 10
    }

}
