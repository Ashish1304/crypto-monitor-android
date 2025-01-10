package com.example.cryptomonitor.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptomonitor.data.models.*
import com.example.cryptomonitor.data.repository.CryptoRepository
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

    fun setRepository(repository: CryptoRepository) {
        cryptoRepository = repository
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            loadSettings()
            loadAlerts()
            loadApiConfigs()
        }
    }

    fun setCurrency(currency: String) {
        viewModelScope.launch {
            _selectedCurrency.value = currency
            cryptoRepository.saveUserSettings(UserSettings(preferredCurrency = currency))
            refreshPrices()
        }
    }

    fun toggleCrypto(crypto: String) {
        val current = _selectedCryptos.value
        if (current.contains(crypto)) {
            _selectedCryptos.value = current - crypto
        } else {
            _selectedCryptos.value = current + crypto
        }
    }

    fun saveApiConfig(platform: String, apiKey: String, apiSecret: String) {
        viewModelScope.launch {
            cryptoRepository.saveApiConfig(ApiConfig(
                platform = platform,
                apiKey = apiKey,
                apiSecret = apiSecret
            ))
            loadApiConfigs()
        }
    }

    fun addAlert(alert: CryptoAlert) {
        viewModelScope.launch {
            cryptoRepository.saveAlert(alert)
            loadAlerts()
        }
    }

    private suspend fun loadSettings() {
        val settings = cryptoRepository.getUserSettings()
        _selectedCurrency.value = settings.preferredCurrency
    }

    private suspend fun loadAlerts() {
        _alerts.value = cryptoRepository.getAllAlerts()
    }

    private suspend fun loadApiConfigs() {
        val configs = cryptoRepository.getAllApiConfigs()
        _apiConfigs.value = configs.associateBy { it.platform }
    }

    private suspend fun refreshPrices() {
        // Implement price refresh with currency conversion
    }
}
