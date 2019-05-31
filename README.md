# Tracking

Declaration in Manifest File

        <receiver
                android:name="com.agnext.trackingutil.services.Restarter"
                android:enabled="true"
                android:exported="true">
            <intent-filter>
                <action android:name="restartservice"/>
            </intent-filter>
        </receiver>
        
       
        <service
                android:name="com.agnext.trackingutil.services.TrackingService"
                android:enabled="true">
        </service>


Add in build.Gradle

        implementation 'com.github.ManbirKakkar:Tracking:0.0.1'


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