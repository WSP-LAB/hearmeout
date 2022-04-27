# HearMeOut
Voice phishing behavior detection system in AOSP

# Overview
HearMeOut operates as an Android system service. You need to register HearMeOut as an Android system service, referring to the description below. You also need to change the source code of each Android module so that HaerMeOut can detect voice phishing behavior.

# Required setup
 - Required hardware: Google Pixel 2
 - Available substitutions hardware: Smartphones that can run Android 8.1 are also possible. But we highly recommend the Google Pixel 2.
 - Required software: Linux (We worked on ubuntu 18.04)
 - Available substitutions software: As long as you can build AOSP, you can use any operating system.

# HaerMeOut System environments
 - AOSP version: 8.1
 - AOSP build id: IDOPM4.171019.021.Q1  
 - AOSP tag: android-8.1.0_r39  
 - Device: Google Pixel 2

# HearMeOut Build Process
1. Build AOSP 8.1 (android-8.1.0_r39)
- Follow the official AOSP guide: https://source.android.com/setup/build/initializing
- AOSP build option: aosp_walleye-userdebug
- If default phone app is not installed, you should install phone app with GAPPS (https://github.com/opengapps/aosp_build)

2. Overwrite HearMeOut source code in the AOSP 8.1
- Overwrite the given source code to a file in the following path.
- CallRedirection/ActivityThread.java -> frameworks/base/core/java/android/app/ActivityThread.java
- CallRedirection/BroadcastReceiver.java -> frameworks/base/core/java/android/content/BroadcastReceiver.java
- CallRedirection/TelephonyConnectionService.java -> packages/services/Telephony/src/com/android/services/telephony/TelephonyConnectionService.java
- FakeCallScreen/WindowManagerImpl.java -> frameworks/base/core/java/android/view/WindowManagerImpl.java
- FakeVoice/MediaPlayer.java -> frameworks/base/media/java/android/media/MediaPlayer.java

3. Register HearMeOut Detector as an Android system service
- Follow the instruction on [Detector](Detector/README.md)

4. Rebuild the AOSP 8.1

5. Flash AOSP to your smartphone
- Follow the official AOSP guide: https://source.android.com/setup/build/running

# HearMeOut evaluation method
Follow the instruction on [Evaluation](Evaluation/README.md)