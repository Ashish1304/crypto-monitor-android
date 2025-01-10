package com.example.cryptomonitor.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CurrencySelector(
    selectedCurrency: String,
    onCurrencySelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val currencies = listOf("MXN", "USD", "EUR")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Currency:",
            modifier = Modifier.padding(end = 8.dp)
        )
        Box {
            Button(onClick = { expanded = true }) {
                Text(selectedCurrency)
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                currencies.forEach { currency ->
                    DropdownMenuItem(
                        onClick = {
                            onCurrencySelected(currency)
                            expanded = false
                        }
                    ) {
                        Text(currency)
                    }
                }
            }
        }
    }
}
