# Crypto Monitor Android App

## About
An Android app that helps monitor cryptocurrency rates from various platforms (Bitso and Binance). The app allows users to:
- Select multiple cryptocurrencies to monitor
- Track prices across different platforms
- Set price alerts for:
  - Upper price limits
  - Lower price limits 
  - Percentage changes

## Development Setup

1. Open this repository in GitHub Codespaces
2. Wait for the automatic environment configuration to complete
3. Build the app by running:
   ```bash
   ./gradlew assembleDebug
   ```
4. Find the generated APK at: `app/build/outputs/apk/debug/app-debug.apk`

## Project Structure

- `/app/src/main/java/com/example/cryptomonitor/` - Main source code
  - `/data/` - Data layer (models, database, API)
  - `/ui/` - User interface components
  - `/service/` - Background services
- `/app/src/main/res/` - Resources
- `/app/src/main/AndroidManifest.xml` - App manifest

## Dependencies

- Android SDK 33
- Kotlin 1.8.0
- Jetpack Compose 1.4.0
- Room Database for local storage
- Retrofit for API communication
- WorkManager for background tasks
EOL

You can now verify the content by running:
