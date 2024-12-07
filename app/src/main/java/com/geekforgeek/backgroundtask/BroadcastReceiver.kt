package com.geekforgeek.backgroundtask

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class BroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val isAirplaneMode = intent?.getBooleanExtra("state", false) ?: return
        if (isAirplaneMode){
            showNotification(context!!, "Airplane Mode On")
        } else {
            showNotification(context!!, "Airplane Mode Off")
        }
    }
}