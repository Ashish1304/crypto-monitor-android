package com.example.cryptomonitor.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.*
import com.example.cryptomonitor.data.repository.CryptoRepository
import kotlinx.coroutines.coroutineScope
import java.util.concurrent.TimeUnit

class PriceMonitorWorker(
    context: Context,
    params: WorkerParameters,
    private val repository: CryptoRepository
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result = coroutineScope {
        try {
            val alerts = repository.getAllAlerts()
            
            alerts.forEach { alert ->
                try {
                    val price = repository.getPrice(alert.coinId, alert.platform)
                    
                    alert.upperLimit?.let { limit ->
                        if (price.price > limit) {
                            showNotification(
                                "${alert.coinId} price above ${String.format("%.2f", limit)}"
                            )
                        }
                    }

                    alert.lowerLimit?.let { limit ->
                        if (price.price < limit) {
                            showNotification(
                                "${alert.coinId} price below ${String.format("%.2f", limit)}"
                            )
                        }
                    }

                    alert.percentageChange?.let { percentChange ->
                        // Implement percentage change logic here
                    }
                } catch (e: Exception) {
                    // Log error or show notification for failed price check
                }
            }

            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }

    private fun showNotification(message: String) {
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Price Alerts",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setContentTitle("Crypto Price Alert")
            .setContentText(message)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }

    companion object {
        private const val CHANNEL_ID = "crypto_monitor_channel"

        fun schedule(context: Context) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val request = PeriodicWorkRequestBuilder<PriceMonitorWorker>(
                15, TimeUnit.MINUTES
            )
                .setConstraints(constraints)
                .build()

            WorkManager.getInstance(context)
                .enqueueUniquePeriodicWork(
                    "price_monitor",
                    ExistingPeriodicWorkPolicy.KEEP,
                    request
                )
        }
    }
}
