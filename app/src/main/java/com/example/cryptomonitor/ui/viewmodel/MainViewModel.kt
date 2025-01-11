package com.example.cryptomonitor.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptomonitor.data.models.*
import com.example.cryptomonitor.data.repository.CryptoRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private lateinit var cryptoRepository: CryptoRepository
    
    private val _selectedCurrency = MutableStateFlow("MXN")
    val selectedCurrency = _selectedCurrency.asStateFlow()

    private val _selectedCryptos = MutableStateFlow<Set<String>>(emptySet())
    val selectedCryptos = _selectedCryptos.asStateFlow()

    private val _prices = MutableStateFlow<Map<String, Map<String, CryptoPrice>>>(emptyMap())
    val prices = _prices.asStateFlow()

    private val _alerts = MutableStateFlow<List<CryptoAlert>>(emptyList())
    val alerts = _alerts.asStateFlow()

    private val _apiConfigs = MutableStateFlow<Map<String, ApiConfig>>(emptyMap())
    val apiConfigs = _apiConfigs.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    fun setRepository(repository: CryptoRepository) {
        cryptoRepository = repository
        loadInitialData()
        startPriceUpdates()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            try {
                loadSettings()
                loadAlerts()
                loadApiConfigs()
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    private fun startPriceUpdates() {
        viewModelScope.launch {
            while (true) {
                _selectedCryptos.value.forEach { crypto ->
                    try {
                        updatePrice(crypto)
                    } catch (e: Exception) {
                        _error.value = e.message
                    }
                }
                delay(30000) // Update every 30 seconds
            }
        }
    }

    fun setCurrency(currency: String) {
        viewModelScope.launch {
            _selectedCurrency.value = currency
            cryptoRepository.saveUserSettings(
                UserSettings(
                    preferredCurrency = currency,
                    selectedCryptos = _selectedCryptos.value
                )
            )
        }
    }

    fun toggleCrypto(crypto: String) {
        viewModelScope.launch {
            val current = _selectedCryptos.value
            val newSelection = if (current.contains(crypto)) {
                current - crypto
            } else {
                current + crypto
            }
            _selectedCryptos.value = newSelection
            
            cryptoRepository.saveUserSettings(
                UserSettings(
                    preferredCurrency = _selectedCurrency.value,
                    selectedCryptos = newSelection
                )
            )

            if (newSelection.contains(crypto)) {
                updatePrice(crypto)
            }
        }
    }

    fun addAlert(alert: CryptoAlert) {
        viewModelScope.launch {
            try {
                cryptoRepository.saveAlert(alert)
                loadAlerts()
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun addApiConfig(config: ApiConfig) {
        viewModelScope.launch {
            try {
                cryptoRepository.saveApiConfig(config)
                loadApiConfigs()
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    private suspend fun updatePrice(coinId: String) {
        try {
            val binancePrice = cryptoRepository.getPrice(coinId, "binance")
            val bitsoPrice = cryptoRepository.getPrice(coinId, "bitso")
            
            val currentPrices = _prices.value.toMutableMap()
            currentPrices[coinId] = mapOf(
                "Binance" to binancePrice,
                "Bitso" to bitsoPrice
            )
            _prices.value = currentPrices
        } catch (e: Exception) {
            _error.value = e.message
        }
    }

    private suspend fun loadSettings() {
        val settings = cryptoRepository.getUserSettings()
        _selectedCurrency.value = settings.preferredCurrency
        _selectedCryptos.value = settings.selectedCryptos
    }

    private suspend fun loadAlerts() {
        _alerts.value = cryptoRepository.getAllAlerts()
    }

    private suspend fun loadApiConfigs() {
        val configs = cryptoRepository.getAllApiConfigs()
        _apiConfigs.value = configs.associateBy { it.platform }
    }
}
