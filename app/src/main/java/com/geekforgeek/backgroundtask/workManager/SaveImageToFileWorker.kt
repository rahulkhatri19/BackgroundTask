package com.geekforgeek.backgroundtask.workManager

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.geekforgeek.backgroundtask.Utility.DELAY_TIMING
import com.geekforgeek.backgroundtask.Utility.KEY_IMAGE_URL
import com.geekforgeek.backgroundtask.showNotification
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private const val TAG = "SaveImageToFileWorker"

class SaveImageToFileWorker(context: Context, parameters: WorkerParameters) :
    CoroutineWorker(context, parameters) {
    private val title = "Blurred Image"
    private val dateFormatter = SimpleDateFormat(
        "yyyy.MM.dd 'at' HH:mm:ss z",
        Locale.getDefault()
    )

    override suspend fun doWork(): Result {
        showNotification(
            applicationContext, "Saving Image"
        )

        return withContext(Dispatchers.IO) {
            delay(DELAY_TIMING)

            val resolver = applicationContext.contentResolver

            return@withContext try {
                val imageUri = inputData.getString(KEY_IMAGE_URL)
                val bitmap = BitmapFactory.decodeStream(
                    resolver.openInputStream(Uri.parse(imageUri))
                )
                val imageUrl = MediaStore.Images.Media.insertImage(
                    resolver, bitmap, title, dateFormatter.format(Date())
                )
                if (!imageUrl.isNullOrEmpty()) {
                    val output = workDataOf(KEY_IMAGE_URL to imageUrl)

                    Result.success(output)
                } else {
                    Log.e(
                        TAG,
                        "Unable to store image to media"
                    )
                    Result.failure()
                }
            } catch (exception: Exception) {
                Log.e(
                    TAG,
                    "Error in saving image",
                    exception
                )
                Result.failure()
            }
        }
    }
}