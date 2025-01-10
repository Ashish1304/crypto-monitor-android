package com.example.cryptomonitor.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ApiConfigScreen(
    onSaveConfig: (platform: String, apiKey: String, apiSecret: String) -> Unit,
    onBack: () -> Unit
) {
    var selectedPlatform by remember { mutableStateOf("") }
    var apiKey by remember { mutableStateOf("") }
    var apiSecret by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TopAppBar(
            title = { Text("API Configuration") },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Text("Back")
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        DropdownMenu(
            expanded = false,
            onDismissRequest = { },
            modifier = Modifier.fillMaxWidth()
        ) {
            DropdownMenuItem(onClick = { selectedPlatform = "Binance" }) {
                Text("Binance")
            }
            DropdownMenuItem(onClick = { selectedPlatform = "Bitso" }) {
                Text("Bitso")
            }
        }

        OutlinedTextField(
            value = apiKey,
            onValueChange = { apiKey = it },
            label = { Text("API Key") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        OutlinedTextField(
            value = apiSecret,
            onValueChange = { apiSecret = it },
            label = { Text("API Secret") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        Button(
            onClick = {
                if (selectedPlatform.isNotEmpty() && apiKey.isNotEmpty() && apiSecret.isNotEmpty()) {
                    onSaveConfig(selectedPlatform, apiKey, apiSecret)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            Text("Save Configuration")
        }
    }
}
