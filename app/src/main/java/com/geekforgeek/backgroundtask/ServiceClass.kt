package com.geekforgeek.backgroundtask

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

class ServiceClass: Service() {
    val SERVICE_TAG = ServiceClass::class.java.simpleName
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        Log.e(SERVICE_TAG, "Background Service created")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        performTask()
        return START_STICKY
    }

    fun performTask(){
        Thread.sleep(Utility.DELAY_TIMING)
        Log.e(SERVICE_TAG, "performing task")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e(SERVICE_TAG, "Service Good Bye")
    }

}