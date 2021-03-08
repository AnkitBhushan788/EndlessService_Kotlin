package com.ankit.endlessservicekotlin

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.ankit.endlessservicekotlin.services.EndlessService
import java.util.*

class MainActivity : AppCompatActivity() {
    private val POWERMANAGER_INTENTS = arrayOf(
            Intent().setComponent(ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity")),
            Intent().setComponent(ComponentName("com.letv.android.letvsafe", "com.letv.android.letvsafe.AutobootManageActivity")),
            Intent().setComponent(ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity")),
            Intent().setComponent(ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.appcontrol.activity.StartupAppControlActivity")),
            Intent().setComponent(ComponentName("com.coloros.safecenter", "com.coloros.safecenter.permission.startup.StartupAppListActivity")),
            Intent().setComponent(ComponentName("com.coloros.safecenter", "com.coloros.safecenter.startupapp.StartupAppListActivity")),
            Intent().setComponent(ComponentName("com.oppo.safe", "com.oppo.safe.permission.startup.StartupAppListActivity")),
            Intent().setComponent(ComponentName("com.iqoo.secure", "com.iqoo.secure.ui.phoneoptimize.AddWhiteListActivity")),
            Intent().setComponent(ComponentName("com.iqoo.secure", "com.iqoo.secure.ui.phoneoptimize.BgStartUpManager")),
            Intent().setComponent(ComponentName("com.vivo.permissionmanager", "com.vivo.permissionmanager.activity.BgStartUpManagerActivity")),
            Intent().setComponent(ComponentName("com.samsung.android.lool", "com.samsung.android.sm.ui.battery.BatteryActivity")),
            Intent().setComponent(ComponentName("com.htc.pitroad", "com.htc.pitroad.landingpage.activity.LandingPageActivity")),
            Intent().setComponent(ComponentName("com.asus.mobilemanager", "com.asus.mobilemanager.MainActivity"))
    )
    var pref: SharedPreferences? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        pref = getSharedPreferences("EndlessService", Context.MODE_PRIVATE)
        val i = intent
        val `is` = i.getStringExtra("isOpen")
        if (`is` != null) {
            if (`is`.equals("Service", ignoreCase = true)) {
//            onPause();
                //moveTaskToBack(true);
                finish()
            }
        }
        try {
            for (intent in POWERMANAGER_INTENTS) if (packageManager.resolveActivity(intent!!, PackageManager.MATCH_DEFAULT_ONLY) != null) {
                startActivity(intent)
                break
            }
            initOPPO()
            oppopermission()
            autoLaunchVivo(applicationContext)
        } catch (e: Exception) {
        }
    }

    fun StartService(view: View) {
        val editor = pref!!.edit()
        editor.putString("startLoc", "start")
        editor.putString("TimeStart", "stop")
        editor.putString("service_start_stop", "start")
        editor.putLong("ldateTime", Date().time)
        editor.apply()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.e("line no 214", "start service background")
            val startIntent = Intent(this@MainActivity, EndlessService::class.java)
            startForegroundService(startIntent)
        } else {
            Log.e("line no 214", "start service normal")
            val startIntent = Intent(this@MainActivity, EndlessService::class.java)
            startService(startIntent)
        }
    }
    fun StopService(view: View) {
        val editor = pref!!.edit()
        editor.putString("startLoc", "stop")
        editor.putString("TimeStart", "stop")
        editor.putString("service_start_stop", "stop")
        editor.putLong("ldateTime", Date().time)
        editor.apply()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.e("line no 214", "start service background")
            val startIntent = Intent(this@MainActivity, EndlessService::class.java)
            stopService(startIntent)
        } else {
            Log.e("line no 214", "start service normal")
            val startIntent = Intent(this@MainActivity, EndlessService::class.java)
            stopService(startIntent)
        }
    }


    private fun initOPPO() {
        try {
            val i = Intent(Intent.ACTION_MAIN)
            i.component = ComponentName("com.oppo.safe", "com.oppo.safe.permission.floatwindow.FloatWindowListActivity")
            startActivity(i)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            try {
                val intent = Intent("action.coloros.safecenter.FloatWindowListActivity")
                intent.component = ComponentName("com.coloros.safecenter", "com.coloros.safecenter.permission.floatwindow.FloatWindowListActivity")
                startActivity(intent)
            } catch (ee: java.lang.Exception) {
                ee.printStackTrace()
                try {
                    val i = Intent("com.coloros.safecenter")
                    i.component = ComponentName("com.coloros.safecenter", "com.coloros.safecenter.sysfloatwindow.FloatWindowListActivity")
                    startActivity(i)
                } catch (e1: java.lang.Exception) {
                    e1.printStackTrace()
                }
            }
        }
    }

    fun oppopermission() {
        if (Build.MANUFACTURER.equals("oppo", ignoreCase = true)) {
            try {
                val intent = Intent()
                intent.setClassName("com.coloros.safecenter",
                        "com.coloros.safecenter.permission.startup.StartupAppListActivity")
                startActivity(intent)
            } catch (e: java.lang.Exception) {
                try {
                    val intent = Intent()
                    intent.setClassName("com.oppo.safe",
                            "com.oppo.safe.permission.startup.StartupAppListActivity")
                    startActivity(intent)
                } catch (ex: java.lang.Exception) {
                    try {
                        val intent = Intent()
                        intent.setClassName("com.coloros.safecenter",
                                "com.coloros.safecenter.startupapp.StartupAppListActivity")
                        startActivity(intent)
                    } catch (exx: java.lang.Exception) {
                    }
                }
            }
        }
    }

    private fun autoLaunchVivo(context: Context) {
        try {
            val intent = Intent()
            intent.component = ComponentName("com.iqoo.secure",
                    "com.iqoo.secure.ui.phoneoptimize.AddWhiteListActivity")
            context.startActivity(intent)
        } catch (e: java.lang.Exception) {
            try {
                val intent = Intent()
                intent.component = ComponentName("com.vivo.permissionmanager",
                        "com.vivo.permissionmanager.activity.BgStartUpManagerActivity")
                context.startActivity(intent)
            } catch (ex: java.lang.Exception) {
                try {
                    val intent = Intent()
                    intent.setClassName("com.iqoo.secure",
                            "com.iqoo.secure.ui.phoneoptimize.BgStartUpManager")
                    context.startActivity(intent)
                } catch (exx: java.lang.Exception) {
                    ex.printStackTrace()
                }
            }
        }
    }

}