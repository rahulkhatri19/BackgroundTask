package com.geekforgeek.backgroundtask

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.geekforgeek.backgroundtask.Utility.CHANNEL_ID
import com.geekforgeek.backgroundtask.Utility.NOTIFICATION_ID
import com.geekforgeek.backgroundtask.Utility.NOTIFICATION_TITLE
import com.geekforgeek.backgroundtask.Utility.VERBOSE_NOTIFICATION_CHANNEL
import com.geekforgeek.backgroundtask.Utility.VERBOSE_NOTIFICATION_CHANNEL_DESCRIPTION

fun showNotification(context: Context, message: String) {

    val name = VERBOSE_NOTIFICATION_CHANNEL
    val description = VERBOSE_NOTIFICATION_CHANNEL_DESCRIPTION
    val importance = NotificationManager.IMPORTANCE_HIGH
    val channel = NotificationChannel(CHANNEL_ID, name, importance)
    channel.description = description

    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?
    notificationManager?.createNotificationChannel(channel)

    val builder = NotificationCompat.Builder(context, CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentTitle(NOTIFICATION_TITLE)
        .setContentText(message)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setVibrate(LongArray(0))
    if (ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        // TODO: Consider calling
        //    ActivityCompat#requestPermissions
        // here to request the missing permissions, and then overriding
        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
        //                                          int[] grantResults)
        // to handle the case where the user grants the permission. See the documentation
        // for ActivityCompat#requestPermissions for more details.
        return
    }
    NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, builder.build())

}

















