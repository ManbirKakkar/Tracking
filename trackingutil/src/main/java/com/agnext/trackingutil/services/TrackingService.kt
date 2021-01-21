package com.agnext.trackingutil.services


import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.SystemClock
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.github.nkzawa.socketio.client.IO
import com.github.nkzawa.socketio.client.Socket
import org.json.JSONObject
import java.net.URISyntaxException


class TrackingService : Service() {

    private var latitude: Double = 0.toDouble()
    private var longitude: Double = 0.toDouble()

    private var mHandler = Handler()

    var mInterval: Int? = 1000
    var mURL: String? = ""
    var mUserId: String? = ""

    private var mStatusChecker: Runnable = object : Runnable {
        override fun run() {
            try {
                val mGPSService = GPSService(this@TrackingService)
                mGPSService.location

                latitude = mGPSService.latitude
                longitude = mGPSService.longitude

                Log.e("Tracking", "$latitude $longitude")


                try {

                    if (mURL == null) {
                        mURL = "http://ec2-18-224-21-43.us-east-2.compute.amazonaws.com:4444/"
                    }

                    var mSocket = IO.socket("$mURL")
                    mSocket!!.connect()

                    var jObj = JSONObject()
                    jObj.put("lat", "$latitude")
                    jObj.put("lng", "$longitude")
                    jObj.put("userId", "$mUserId")

                    mSocket!!.emit("addDoc", jObj)


                } catch (e: URISyntaxException) {
                    throw RuntimeException(e)
                }


            } finally {
                mHandler.postDelayed(this, mInterval!!.toLong())
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun startMyOwnForeground() {
        val NOTIFICATION_CHANNEL_ID = "example.permanence"
        val channelName = "Background Service"
        val chan = NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE)
        chan.lightColor = Color.BLUE
        chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE

        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(chan)

        val notificationBuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
        val notification = notificationBuilder.setOngoing(true)
            .setContentTitle("App is running in background")
            .setPriority(NotificationManager.IMPORTANCE_MIN)
            .setCategory(Notification.CATEGORY_SERVICE)
            .build()
        startForeground(2, notification)

    }


    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        when {
            Build.VERSION.SDK_INT > Build.VERSION_CODES.O -> startMyOwnForeground()
            else -> startForeground(1, Notification())
        }

        mInterval = intent.getIntExtra("TIME", 5000)
        mURL = intent.getStringExtra("SOCKET_URL")
        mUserId = intent.getStringExtra("USER_ID")

        startRepeatingTask()
        return START_STICKY

    }


    override fun onTaskRemoved(rootIntent: Intent) {
        val restartServiceIntent = Intent(applicationContext, this.javaClass)
        restartServiceIntent.setPackage(packageName)

        val restartServicePendingIntent =
            PendingIntent.getService(applicationContext, 1, restartServiceIntent, PendingIntent.FLAG_ONE_SHOT)
        val alarmService = applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmService.set(
            AlarmManager.ELAPSED_REALTIME,
            SystemClock.elapsedRealtime() + 1000,
            restartServicePendingIntent
        )

        super.onTaskRemoved(rootIntent)
    }

    override fun onDestroy() {
        super.onDestroy()

        val broadcastIntent = Intent()
        broadcastIntent.action = "restartservice"
        broadcastIntent.setClass(this, Restarter::class.java)
        this.sendBroadcast(broadcastIntent)
    }

    private fun startRepeatingTask() {
        mStatusChecker.run()
    }

    private fun stopRepeatingTask() {
        mHandler.removeCallbacks(mStatusChecker)
    }


    override fun onBind(intent: Intent): IBinder? {
        return null
    }


}


