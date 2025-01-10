package com.example.cryptomonitor.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.cryptomonitor.data.models.CryptoAlert

@Composable
fun AddAlertDialog(
    cryptoId: String,
    onDismiss: () -> Unit,
    onConfirm: (CryptoAlert) -> Unit
) {
    var upperLimit by remember { mutableStateOf("") }
    var lowerLimit by remember { mutableStateOf("") }
    var percentageChange by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Alert for $cryptoId") },
        text = {
            Column {
                OutlinedTextField(
                    value = upperLimit,
                    onValueChange = { upperLimit = it },
                    label = { Text("Upper Limit") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                )

                OutlinedTextField(
                    value = lowerLimit,
                    onValueChange = { lowerLimit = it },
                    label = { Text("Lower Limit") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                )

                OutlinedTextField(
                    value = percentageChange,
                    onValueChange = { percentageChange = it },
                    label = { Text("Percentage Change") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val alert = CryptoAlert(
                        coinId = cryptoId,
                        platform = "ALL",
                        upperLimit = upperLimit.toDoubleOrNull(),
                        lowerLimit = lowerLimit.toDoubleOrNull(),
                        percentageChange = percentageChange.toDoubleOrNull()
                    )
                    onConfirm(alert)
                }
            ) {
                Text("Add Alert")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
