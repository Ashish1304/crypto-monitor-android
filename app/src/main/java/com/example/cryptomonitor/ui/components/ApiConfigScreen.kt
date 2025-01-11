package com.example.cryptomonitor.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun ApiConfigScreen(
    onSaveConfig: (platform: String, apiKey: String, apiSecret: String) -> Unit,
    onBack: () -> Unit,
    isLoading: Boolean = false,
    errorMessage: String? = null
) {
    var selectedPlatform by remember { mutableStateOf("Bitso") }
    var apiKey by remember { mutableStateOf("") }
    var apiSecret by remember { mutableStateOf("") }
    var showDropdown by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TopAppBar(
            title = { Text("API Configuration") },
            navigationIcon = {
                TextButton(onClick = onBack) {
                    Text("Back")
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Platform Selection
        Box {
            OutlinedButton(
                onClick = { showDropdown = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Selected Platform: $selectedPlatform")
            }
            DropdownMenu(
                expanded = showDropdown,
                onDismissRequest = { showDropdown = false }
            ) {
                DropdownMenuItem(onClick = {
                    selectedPlatform = "Bitso"
                    showDropdown = false
                }) {
                    Text("Bitso")
                }
                DropdownMenuItem(onClick = {
                    selectedPlatform = "Binance"
                    showDropdown = false
                }) {
                    Text("Binance")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Error Message
        errorMessage?.let {
            Text(
                text = it,
                color = MaterialTheme.colors.error,
                style = MaterialTheme.typography.caption,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        OutlinedTextField(
            value = apiKey,
            onValueChange = { apiKey = it.trim() },
            label = { Text("API Key") },
            modifier = Modifier.fillMaxWidth(),
            isError = errorMessage != null
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = apiSecret,
            onValueChange = { apiSecret = it.trim() },
            label = { Text("API Secret") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            isError = errorMessage != null
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (apiKey.isNotBlank() && apiSecret.isNotBlank()) {
                    onSaveConfig(selectedPlatform.lowercase(), apiKey, apiSecret)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading && apiKey.isNotBlank() && apiSecret.isNotBlank()
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colors.onPrimary
                )
            } else {
                Text("Save Configuration")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Detailed Instructions Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = 2.dp
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "Setup Instructions:",
                    style = MaterialTheme.typography.h6
                )
                Spacer(modifier = Modifier.height(8.dp))

                when (selectedPlatform) {
                    "Bitso" -> BitsoInstructions()
                    "Binance" -> BinanceInstructions()
                }
            }
        }
    }
}

@Composable
private fun BitsoInstructions() {
    Column {
        Text(
            """
            1. Log in to your Bitso account at bitso.com
            2. Go to Account > API Keys (or visit bitso.com/api_setup)
            3. Click "Create API Keys"
            4. Set permissions:
               - Enable "Read" permissions
               - Disable "Trade" and "Withdraw" permissions
            5. Complete 2FA verification if required
            6. Copy your new API Key and Secret
            7. Store your Secret safely - it won't be shown again
            
            Important Notes:
            • Prices will be shown in MXN
            • Keep your API Secret private
            • Use only READ permissions for security
            """.trimIndent()
        )
    }
}

@Composable
private fun BinanceInstructions() {
    Column {
        Text(
            """
            1. Log in to your Binance account
            2. Go to Profile > API Management
            3. Click "Create API"
            4. Set permissions:
               - Enable only "Read Market Data"
               - Disable all trading and withdrawal permissions
            5. Complete security verification
            6. Save both API Key and Secret immediately
            7. Enable IP restrictions for better security
            
            Important Notes:
            • Prices will be shown in USDT
            • Never share your API Secret
            • Use IP restrictions when possible
            • Enable only read permissions
            """.trimIndent()
        )
    }
}
