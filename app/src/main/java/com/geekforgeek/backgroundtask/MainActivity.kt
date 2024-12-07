package com.geekforgeek.backgroundtask

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.geekforgeek.backgroundtask.ui.theme.BackgroundTaskTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BackgroundTaskTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    BlurWorkManager(Modifier.padding(innerPadding))
//                    val intent = IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED)
//                    registerReceiver(BroadcastReceiver(), intent)

                    val service = Intent(this, ServiceClass::class.java)
                    startService(service)
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BackgroundTaskTheme {
        Greeting("Android")
    }
}