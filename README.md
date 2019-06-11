# Tracking

Add in build.Gradle

        implementation 'com.github.ManbirKakkar:Tracking:v0.0.3'


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

          TrackingUtil(this).startTracking()
          
Start tracking at "N" milliseconds
          
          TrackingUtil(this).startTracking(N) //N is in Int
          