package com.geekforgeek.backgroundtask.workManager

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.geekforgeek.backgroundtask.Utility.DELAY_TIMING
import com.geekforgeek.backgroundtask.Utility.OUTPUT_PATH
import com.geekforgeek.backgroundtask.showNotification
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.io.File

val TAG_CLEAN = CleanupWorker::class.java.simpleName
class CleanupWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {

        showNotification( applicationContext, "Clean up Done")

        return withContext(Dispatchers.IO){
            delay(DELAY_TIMING)
            return@withContext try {
                val outputDirectory = File(applicationContext.filesDir, OUTPUT_PATH)
                if (outputDirectory.exists()) {
                    val entries = outputDirectory.listFiles()
                    if (entries != null) {
                        for (entry in entries) {
                            val name = entry.name
                            if (name.isNotEmpty() && name.endsWith(".png")) {
                                val deleted = entry.delete()
                                Log.i(TAG_CLEAN, "Deleted $name - $deleted")
                            }
                        }
                    }
                }
                Result.success()
            } catch (exception:Exception){
                Log.e(
                    TAG_CLEAN,
                    "",
                    exception
                )
                Result.failure()
            }
        }
    }
}