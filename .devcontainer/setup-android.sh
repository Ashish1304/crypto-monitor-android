#!/bin/bash

# Install required packages
sudo apt-get update
sudo apt-get install -y wget unzip gradle

# Create Android SDK directory
mkdir -p ${HOME}/android-sdk
export ANDROID_SDK_ROOT=${HOME}/android-sdk

# Download Android Command Line Tools
wget https://dl.google.com/android/repository/commandlinetools-linux-9477386_latest.zip
unzip commandlinetools-linux-*_latest.zip
mkdir -p ${ANDROID_SDK_ROOT}/cmdline-tools/latest
mv cmdline-tools/* ${ANDROID_SDK_ROOT}/cmdline-tools/latest/
rm -r cmdline-tools
rm commandlinetools-linux-*_latest.zip

# Set up environment variables
echo "export ANDROID_SDK_ROOT=${ANDROID_SDK_ROOT}" >> ~/.bashrc
echo "export PATH=${PATH}:${ANDROID_SDK_ROOT}/cmdline-tools/latest/bin:${ANDROID_SDK_ROOT}/platform-tools" >> ~/.bashrc

# Accept licenses and install required packages
yes | ${ANDROID_SDK_ROOT}/cmdline-tools/latest/bin/sdkmanager --licenses
${ANDROID_SDK_ROOT}/cmdline-tools/latest/bin/sdkmanager "platform-tools" "platforms;android-33" "build-tools;33.0.2"

# Create local.properties
echo "sdk.dir=${ANDROID_SDK_ROOT}" > local.properties
