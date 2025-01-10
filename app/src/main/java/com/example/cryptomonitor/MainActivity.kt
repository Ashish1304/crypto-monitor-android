package com.example.cryptomonitor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import com.example.cryptomonitor.ui.theme.CryptoMonitorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CryptoMonitorTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    // Content will go here
                }
            }
        }
    }
}
