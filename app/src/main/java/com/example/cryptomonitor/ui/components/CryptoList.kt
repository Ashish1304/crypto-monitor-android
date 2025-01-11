package com.example.cryptomonitor.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.cryptomonitor.ui.viewmodel.MainViewModel
import com.example.cryptomonitor.data.models.CryptoPrice

@Composable
fun CryptoList(
    viewModel: MainViewModel,
    onConfigureApi: () -> Unit
) {
    var showAddAlertDialog by remember { mutableStateOf<String?>(null) }
    val selectedCurrency by viewModel.selectedCurrency.collectAsState()
    val selectedCryptos by viewModel.selectedCryptos.collectAsState()
    val prices by viewModel.prices.collectAsState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Crypto Monitor",
                style = MaterialTheme.typography.h5,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            CurrencySelector(
                selectedCurrency = selectedCurrency,
                onCurrencySelected = viewModel::setCurrency
            )

            Divider(modifier = Modifier.padding(vertical = 8.dp))

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val cryptoOptions = listOf(
                    "BTC" to "Bitcoin",
                    "ETH" to "Ethereum",
                    "XRP" to "Ripple",
                    "DOGE" to "Dogecoin",
                    "MANA" to "Decentraland",
                    "SOL" to "Solana",
                    "ADA" to "Cardano",
                    "DOT" to "Polkadot",
                    "MATIC" to "Polygon",
                    "LINK" to "Chainlink"
                )

                items(cryptoOptions) { (symbol, name) ->
                    CryptoCard(
                        symbol = symbol,
                        name = name,
                        isSelected = selectedCryptos.contains(symbol),
                        prices = prices[symbol],
                        selectedCurrency = selectedCurrency,
                        onToggle = { viewModel.toggleCrypto(symbol) },
                        onAlertClick = { showAddAlertDialog = symbol }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onConfigureApi,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Configure API Keys")
            }
        }
    }

    showAddAlertDialog?.let { crypto ->
        AddAlertDialog(
            cryptoId = crypto,
            onDismiss = { showAddAlertDialog = null },
            onConfirm = { alert ->
                viewModel.addAlert(alert)
                showAddAlertDialog = null
            }
        )
    }
}

@Composable
private fun CryptoCard(
    symbol: String,
    name: String,
    isSelected: Boolean,
    prices: Map<String, CryptoPrice>?,
    selectedCurrency: String,
    onToggle: () -> Unit,
    onAlertClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = 2.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = name, style = MaterialTheme.typography.h6)
                    Text(text = symbol, style = MaterialTheme.typography.caption)
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Switch(
                        checked = isSelected,
                        onCheckedChange = { onToggle() }
                    )
                    IconButton(onClick = onAlertClick) {
                        Text("🔔")
                    }
                }
            }
            
            if (isSelected && prices != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Column {
                    Text(
                        "Current Prices",
                        style = MaterialTheme.typography.subtitle2,
                        color = Color.Gray
                    )
                    prices.forEach { (platform, priceData) ->
                        Text(
                            "$platform: ${String.format("%.2f", priceData.price)} $selectedCurrency",
                            style = MaterialTheme.typography.body2
                        )
                    }
                }
            }
        }
    }
}
