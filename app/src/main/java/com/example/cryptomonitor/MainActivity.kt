package com.example.cryptomonitor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cryptomonitor.ui.components.ApiConfigScreen
import com.example.cryptomonitor.ui.components.CryptoList
import com.example.cryptomonitor.ui.theme.CryptoMonitorTheme
import com.example.cryptomonitor.ui.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val appContainer = (application as CryptoApplication).container

        setContent {
            CryptoMonitorTheme {
                val viewModel: MainViewModel = viewModel()
                var showingApiConfig by remember { mutableStateOf(false) }

                LaunchedEffect(Unit) {
                    viewModel.setRepository(appContainer.repository)
                }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    if (showingApiConfig) {
                        ApiConfigScreen(
                            onSaveConfig = { platform, apiKey, apiSecret ->
                                viewModel.saveApiConfig(platform, apiKey, apiSecret)
                                showingApiConfig = false
                            },
                            onBack = { showingApiConfig = false }
                        )
                    } else {
                        CryptoList(
                            viewModel = viewModel,
                            onConfigureApi = { showingApiConfig = true }
                        )
                    }
                }
            }
        }
    }
}
