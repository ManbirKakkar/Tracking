# Tracking

Declartion in Manifest File

    
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
