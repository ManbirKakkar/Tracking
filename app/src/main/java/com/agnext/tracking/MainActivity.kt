package com.agnext.tracking

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.agnext.trackingutil.permission.TrackingUtil
import com.agnext.trackingutil.services.TrackingService

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(!TrackingUtil(this).isLocationPermission()){
            TrackingUtil(this).requestLocationPermission()
        }else{

            var lat = TrackingUtil(this).latitude
            Log.e("lat", "$lat")

         //   TrackingUtil(this).startTracking()

          //  TrackingUtil(this).startTracking(1000)
            TrackingUtil(this).startTracking("URL_SAMPLE", "USER_ID", 1000)

        }

    }


}
