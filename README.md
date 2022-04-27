# HearMeOut
Voice phishing behavior detection system in AOSP

# Overview
HearMeOut operates as an Android system service. You need to register HearMeOut as an Android system service, referring to the description below. You also need to change the source code of each Android module so that HaerMeOut can detect voice phishing behavior.

# Required setup
 - Smartphone: Google Pixel 2 (Smartphones that can run Android 8.1 are also possible. But we highly recommend the Google Pixel 2.)
 - OS: Linux for building AOSP 8.1 with HearMeOut

# HaerMeOut System environments
 - AOSP version: 8.1
 - AOSP build id: IDOPM4.171019.021.Q1  
 - AOSP tag: android-8.1.0_r39  
 - Device: Google Pixel 2

# HearMeOut Build Guide
1. build AOSP 8.1 (android-8.1.0_r39)
- Follow the official AOSP guide: https://source.android.com/setup/build/initializing
- AOSP build option: aosp_walleye-userdebug
- If default phone app is not installed, you should install phone app with GAPPS (https://github.com/opengapps/aosp_build)

2. Overwrite HearMeOut source code in AOSP
- Overwrite the given source code to a file in the following path.
- CallRedirection/ActivityThread.java -> frameworks/base/core/java/android/app/ActivityThread.java
- CallRedirection/BroadcastReceiver.java -> frameworks/base/core/java/android/content/BroadcastReceiver.java
- CallRedirection/TelephonyConnectionService.java -> packages/services/Telephony/src/com/android/services/telephony/TelephonyConnectionService.java
- FakeCallScreen/WindowManagerImpl.java -> frameworks/base/core/java/android/view/WindowManagerImpl.java
- FakeVoice/MediaPlayer.java -> frameworks/base/media/java/android/media/MediaPlayer.java

3. Register HearMeOut Detector as an Android system service
- Follow the instruction on [Detector](Detector/README.md)

# HearMeOut evaluation method
Follow the instruction on [Evaluation](Evaluation/README.md)