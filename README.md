# Tracking

Add in build.Gradle

        implementation 'com.github.ManbirKakkar:Tracking:0.0.5'


        allprojects {
            repositories {
                maven { url 'https://jitpack.io' }
                 }
             }
             
             
Java 1.8 is required to use the lib. Add the following line in build.gradle
             
         compileOptions {
              targetCompatibility 1.8
              sourceCompatibility 1.8
           }


# Usage

To check if Location Permission is enabled

         TrackingUtil(this).isLocationPermission()

To request if Location Permission

          TrackingUtil(this).requestLocationPermission()

Get latitude and Longitude

          var lat = TrackingUtil(this).latitude
          var lat = TrackingUtil(this).longitude
Start tracking

            TrackingUtil(this).startTracking("URL_SAMPLE", "USER_ID", TIME_INTERVAL)
            
          
Backend Socket implementation should be done to receive emit as

           var jObj = JSONObject()
           jObj.put("lat", "$latitude")
           jObj.put("lng", "$longitude")
           jObj.put("userId", "$mUserId")
           
           mSocket!!.emit("addDoc", jObj)
          