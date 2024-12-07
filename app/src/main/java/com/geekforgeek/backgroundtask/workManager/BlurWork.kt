package com.geekforgeek.backgroundtask.workManager

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.geekforgeek.backgroundtask.Utility.DELAY_TIMING
import com.geekforgeek.backgroundtask.Utility.KEY_BLUR_LEVEL
import com.geekforgeek.backgroundtask.Utility.KEY_IMAGE_URL
import com.geekforgeek.backgroundtask.Utility.OUTPUT_PATH
import com.geekforgeek.backgroundtask.showNotification
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.UUID

val TAG_BLUR = BlurWork::class.java.simpleName

class BlurWork(context: Context, parameters: WorkerParameters) :
    CoroutineWorker(context, parameters) {

    override suspend fun doWork(): Result {
        val resourceUri = inputData.getString(KEY_IMAGE_URL)
        val blurLevel = inputData.getInt(KEY_BLUR_LEVEL, 1)

        showNotification(applicationContext, "Blurring the Image")
        return withContext(Dispatchers.IO) {
            delay(DELAY_TIMING)
            return@withContext try {
                require(!resourceUri.isNullOrBlank()){
                    val errorMessage = "Invalid blur Image"
                    Log.e(TAG_BLUR, errorMessage)
                    errorMessage
                }

                val resolver = applicationContext.contentResolver
                val picture = BitmapFactory.decodeStream(
                    resolver.openInputStream(Uri.parse(resourceUri))
                )

                val output = blurBitmap(picture, blurLevel)

                // Write bitmap to a temp file
                val outputUri = writeBitmapToFile(applicationContext, output)

                val outputData = workDataOf(KEY_IMAGE_URL to outputUri.toString())

                Result.success(outputData)

            } catch (throwable: Throwable) {
                Log.e(TAG_BLUR, "Error applying Blur", throwable)
                Result.failure()
            }
        }
    }

    private fun blurBitmap(picture: Bitmap, blurLevel: Int): Bitmap {
        val input = Bitmap.createScaledBitmap(
            picture,
            picture.width / (blurLevel * 5),
            picture.height / (blurLevel * 5),
            true
        )
        return Bitmap.createScaledBitmap(input, picture.width, picture.height, true)
    }

    fun writeBitmapToFile(context: Context, bitmap: Bitmap):Uri{
        val name = String.format("blur-filter-output-%s.png", UUID.randomUUID().toString())
        val outputDir = File(context.filesDir, OUTPUT_PATH)

        if (!outputDir.exists()){
            outputDir.mkdirs()
        }
        val outputFile = File(outputDir, name)
        var out : FileOutputStream? = null
        try {
            out = FileOutputStream(outputFile)
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, out)

        } finally {
            out?.let {
                try {
                    it.close()
                } catch (e:IOException){
                    Log.e(TAG_BLUR, e.message.toString())
                }
            }
        }
        return Uri.fromFile(outputFile)
    }

}




