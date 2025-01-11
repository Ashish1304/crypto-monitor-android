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
    existingAlert: CryptoAlert? = null,
    onDismiss: () -> Unit,
    onConfirm: (CryptoAlert) -> Unit
) {
    var upperLimit by remember { mutableStateOf(existingAlert?.upperLimit?.toString() ?: "") }
    var lowerLimit by remember { mutableStateOf(existingAlert?.lowerLimit?.toString() ?: "") }
    var percentageChange by remember { mutableStateOf(existingAlert?.percentageChange?.toString() ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (existingAlert != null) "Edit Alert for $cryptoId" else "Add Alert for $cryptoId") },
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
                        id = existingAlert?.id ?: 0,
                        coinId = cryptoId,
                        platform = "ALL",
                        upperLimit = upperLimit.toDoubleOrNull(),
                        lowerLimit = lowerLimit.toDoubleOrNull(),
                        percentageChange = percentageChange.toDoubleOrNull()
                    )
                    onConfirm(alert)
                }
            ) {
                Text(if (existingAlert != null) "Update Alert" else "Add Alert")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
