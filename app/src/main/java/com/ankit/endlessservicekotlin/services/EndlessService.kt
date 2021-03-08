package com.ankit.endlessservicekotlin.services

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.ankit.endlessservicekotlin.MainActivity
import com.ankit.endlessservicekotlin.R
import java.text.SimpleDateFormat
import java.util.*


class EndlessService : Service() {

    private val mHandler = Handler()
    private var mTimer: Timer? = null

    val notify_in = 1000 * 2.toLong()

    //    final long notify_interval = 1000 * 60*15 ; //for 15 min
    //long notify_interval_loc = 1000*10*60;
    var sharedPreferences: SharedPreferences? = null

    var isRunning = false
    var isStop = true
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val pref: SharedPreferences
        pref = getSharedPreferences("EndlessService", Context.MODE_PRIVATE)
        val isStart = pref.getString("service_start_stop", "")
        Log.e("TS line no 103 ", "on start command:  $isStart")
        if (isStart.equals("stop", ignoreCase = true)) {
            Log.e("TS line no 122 ", "on stop command")
            //            new OfflineAttendanceUpload().execute();
            stopSelf()
            stopSelf(startId)
        }
        return START_STICKY
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()
        sharedPreferences = getSharedPreferences("EndlessService", 0)
        val editor = sharedPreferences!!.edit()
        editor.putLong("ldateTime", Date().time)
        editor.putString("TimeStart", "stop")
        editor.apply()
        isRunning = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            startForeground(12345678, getNotification());
            notification
            Log.e("TS line no 191 ", "on create command" + Date().time)
            if (mTimer == null) mTimer = Timer()
            isStop = true
            isRunning = true
            //            mTimer.schedule(new TimerTaskToGetLocation(), notify_interval, notify_interval);
            mTimer!!.schedule(TimerTaskToGetLocation(), notify_in, notify_in)
        } else {
            if (mTimer == null) mTimer = Timer()
            isRunning = true
            isStop = true
            notification
            //            mTimer.schedule(new TimerTaskToGetLocation(), notify_interval, notify_interval);
            mTimer!!.schedule(TimerTaskToGetLocation(), notify_in, notify_in)
        }
        startRun()
        Log.e("create service", "create Service")
    }

    fun startRun() {
        val sharedPreferences = getSharedPreferences("EndlessService", 0)
        val service_start_stop =
            sharedPreferences.getString("service_start_stop", "")
        if (service_start_stop.equals("start", ignoreCase = true)) {
            val cDateTime = Date().time
            val lDateTime = sharedPreferences.getLong("ldateTime", 0)
            val diffDateTime = cDateTime - lDateTime
            Log.e(
                "val 1st",
                "$cDateTime::$lDateTime::$diffDateTime"
            )
            if (sharedPreferences.getString("TimeStart", "").equals("start", ignoreCase = true)) {
                if (diffDateTime < 1000 * 60) {
                    Log.e("retuen", "retuen val")
                    return
                }
            }
            val editor = sharedPreferences!!.edit()
            editor.putString("TimeStart", "start")
            editor.putLong("ldateTime", Date().time)
            editor.apply()
        }
        Log.e("line no 375", "data inserting befor uploding")
    }// Add as notification

//        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "EndlessService").setAutoCancel(false)
//                .setPriority(PRIORITY_MAX);

    //
//                Log.e("TS line no 238 ","on create command"+new Date().getTime());
    private val notification:
            //                Log.e("TS line no 255 ","on create command"+new Date().getTime());
            Unit
        private get() {
            val pref: SharedPreferences
            pref = getSharedPreferences("EndlessService", Context.MODE_PRIVATE)
            val isStart = pref.getString("service_start_stop", "")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (isStart.equals("start", ignoreCase = true)) {


                    //
                    //                Log.e("TS line no 238 ","on create command"+new Date().getTime());
                    val notIntent = Intent(this, MainActivity::class.java)
                    val pendingIntent = PendingIntent.getActivity(this, 0, notIntent, 0)
                    Log.e(
                        "TS line no 241 ",
                        "on create command" + Date().time
                    )
                    val notification =
                        NotificationCompat.Builder(this, "EndlessService")
                            .setContentIntent(pendingIntent)
                            .setAutoCancel(true)
                            .setDefaults(Notification.DEFAULT_ALL)
                            .setWhen(System.currentTimeMillis())
                            .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
                            .setLargeIcon(
                                BitmapFactory.decodeResource(
                                    resources,
                                    R.drawable.ic_launcher_background
                                )
                            )
                            .setPriority(NotificationCompat.PRIORITY_HIGH)
                            .setContentText("Endless Service Running")
                            .setSmallIcon(R.drawable.ic_launcher_background)
                            .setPriority(NotificationCompat.PRIORITY_MAX)
                            .build()
                    //                Log.e("TS line no 255 ","on create command"+new Date().getTime());
                    startForeground(1, notification)
                    Log.e(
                        "TS line no 257 ",
                        "on create command" + Date().time
                    )
                }
            } else {
                val builder =
                    NotificationCompat.Builder(this, "EndlessService")
                        .setAutoCancel(true)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setWhen(System.currentTimeMillis())
                        .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
                        .setLargeIcon(
                            BitmapFactory.decodeResource(
                                resources,
                                R.drawable.ic_launcher_background
                            )
                        )
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setContentText("Endless Service Running")
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .setPriority(NotificationCompat.PRIORITY_MAX)
                        .setContentTitle("Endless Service")
                val notificationIntent = Intent(this, MainActivity::class.java)
                val contentIntent = PendingIntent.getActivity(
                    this, 0, notificationIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
                builder.setContentIntent(contentIntent)

                // Add as notification
                val manager =
                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                manager.notify(0, builder.build())
            }

            //        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "EndlessService").setAutoCancel(false)
            //                .setPriority(PRIORITY_MAX);
        }

    private inner class TimerTaskToGetLocation : TimerTask() {
        override fun run() {
            mHandler.post {
                Log.i("line no 561", "inside TimerTaskToGetLocation")
                startRun()


                //showNotification();
            }
        }
    }

    //
    override fun onTaskRemoved(rootIntent: Intent) {
        super.onTaskRemoved(rootIntent)
        Log.e(TAG, "TASK REMOVED")
        val sharedPreferences = getSharedPreferences("EndlessService", Context.MODE_PRIVATE)
        val isStart = sharedPreferences.getString("service_start_stop", "")
        val restartServiceIntent = Intent(applicationContext, MainActivity::class.java)
        if (isStart.equals("start", ignoreCase = true)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Log.e("line no 214", "start service background")
                val editor = sharedPreferences.edit()
                editor.putString("isOpen", "Service")
                editor.apply()
                restartServiceIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                restartServiceIntent.putExtra("isOpen", "Service")
                startActivity(restartServiceIntent)
                //                startForegroundService(restartServiceIntent);
            } else {
                val editor = sharedPreferences.edit()
                editor.putString("isOpen", "Service")
                editor.apply()
                restartServiceIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                restartServiceIntent.putExtra("isOpen", "Service")
                startActivity(restartServiceIntent)
                //                startService(restartServiceIntent);
            }
            Log.e("line no 214", "start service background")
            val editor = sharedPreferences.edit()
            editor.putString("isOpen", "Service")
            editor.apply()
            Log.e("TS line no 137", "Service  start")
        } else {
            val editor = sharedPreferences.edit()
            editor.putString("isOpen", "Self")
            editor.apply()
            //            stopService(restartServiceIntent);
            Log.e("line no 141", "Service stop")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        startRun()
        if (mTimer != null) {
            mTimer!!.cancel()
        }
        val editor = sharedPreferences!!.edit()
        editor.putString("isOpen", "Self")
        editor.commit()
        Log.e("Line 652", "service end")
    }

    companion object {
        private val TAG = EndlessService::class.java.simpleName
    }
}