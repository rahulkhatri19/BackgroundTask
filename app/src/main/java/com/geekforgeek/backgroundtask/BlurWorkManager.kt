package com.geekforgeek.backgroundtask

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Button
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.geekforgeek.backgroundtask.Utility.KEY_BLUR_LEVEL
import com.geekforgeek.backgroundtask.Utility.KEY_IMAGE_URL
import com.geekforgeek.backgroundtask.workManager.BlurAmount
import com.geekforgeek.backgroundtask.workManager.BlurWork
import com.geekforgeek.backgroundtask.workManager.CleanupWorker
import com.geekforgeek.backgroundtask.workManager.SaveImageToFileWorker

@Composable
fun BlurWorkManager(modifier: Modifier) {
    Box(Modifier.background(Color.White)) {

        val blurList = arrayListOf<BlurAmount>()
        blurList.add(BlurAmount(R.string.select_one, 1))
        blurList.add(BlurAmount(R.string.select_two, 2))
        blurList.add(BlurAmount(R.string.select_three, 3))
        val context = LocalContext.current
        val workManager = WorkManager.getInstance(context)

        var selectedValue by remember { mutableStateOf(1) }

        Column(
            Modifier
                .padding(horizontal = 16.dp)
                .fillMaxHeight()) {
            Spacer(Modifier.height(56.dp))
            Image(
                painter = painterResource(R.drawable.cookie_img),
                modifier = Modifier.size(200.dp),
                contentDescription = "",
                alignment = Alignment.Center
            )

            Text(
                text = stringResource(R.string.select_blur_amount),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            blurList.forEach { list ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectable(
                            role = Role.RadioButton,
                            selected = selectedValue == list.amountSequence,
                            onClick = { selectedValue = list.amountSequence }
                        )
                        .size(56.dp)
                    ) {
                    RadioButton(
                        selected = selectedValue == list.amountSequence,
                        onClick = null,
                        modifier = Modifier.size(48.dp)
                    )
                    Text(
                        text = stringResource(list.amountText)
                    )
                }
            }

            Button(
                onClick = {
                    blurTask(selectedValue, workManager, context)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.start)
                )
            }

        }
    }
}

fun blurTask(blurLevel:Int, workManager: WorkManager, context: Context){
    var continuation =
        workManager.beginWith(OneTimeWorkRequest.Companion.from(CleanupWorker::class.java))
    val blurBuilder = OneTimeWorkRequestBuilder<BlurWork>()
    blurBuilder.setInputData(createInputDataForWorkRequest(blurLevel, getImageUrl(context)))
    continuation = continuation.then(blurBuilder.build())

    val save = OneTimeWorkRequestBuilder<SaveImageToFileWorker>().build()
    continuation = continuation.then(save)

    continuation.enqueue()

}

private fun createInputDataForWorkRequest(blurLevel: Int, imageUri: Uri): Data {
    val builder = Data.Builder()
    builder.putString(KEY_IMAGE_URL, imageUri.toString()).putInt(KEY_BLUR_LEVEL, blurLevel)
    return builder.build()
}

private fun getImageUrl(context: Context): Uri {
    val resources = context.resources
    return Uri.Builder()
        .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
        .authority(resources.getResourcePackageName(R.drawable.cookie_img))
        .appendPath(resources.getResourceTypeName(R.drawable.cookie_img))
        .appendPath(resources.getResourceEntryName(R.drawable.cookie_img))
        .build()
}