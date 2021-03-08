package com.ankit.endlessservicekotlin.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.ankit.endlessservicekotlin.services.EndlessService

public class BootRecievr  : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        Log.e("Service Stops", "Ohhhhhhh")
        val intent1 = Intent(context, EndlessService::class.java)
        intent1.action = "startservice"
        context.startService(intent1)
    }
}