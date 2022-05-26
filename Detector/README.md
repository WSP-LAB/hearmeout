# HearMeOut: Detector 
Android system service for detection of phishing behavior.
This module should be registered in the Android system service.
The detector operates within the Android framework as a system service.

# Register Detector as Android system service 
1. Create service class in the Android framework
- Copy [Detector](Detector.java) into frameworks/base/services/java/com/android/server/

2. Register the service in SystemServer.java
- Replace frameworks/base/services/java/com/android/server/SystemServer.java with [SystemServer](SystemServer.java)

3. Create a .aidl interface of the service in the Android framework
- Copy [Detector interface](IDetector.aidl) into frameworks/base/core/java/android/os/

4. Register .aidl interface in the build system
- Add the following statement "LOCAL_SRC_FI:LES" in frameworks/base/Android.mk
```java
core/java/android/os/IDetector.aidl \
```

5. Create a service manager for the system service in the Android framework
- Copy [DetectorMangaer](DetectorManager.aidl) into frameworks/base/core/java/android/os/

6. Register the service manager in SystemServiceRegistry
- Add the following code in the static block of frameworks/base/core/java/android/app/SystemServiceRegistry.java
```java
registerService(Context.PHISHING_DETECTOR, DetectorManager.class, 
        new CachedServiceFetcher<DetectorManager>() {
        @Override
        public DetectorManager createService(ContextImpl ctx) {
            Log.i("HearMeOut - SystemServiceRegistry", 
                    "Register HearMeOut as an Android system service");
            return DetectorManager.getDetectorManager();
        }});
```

7. Register name of Detector service manager in Context
- Add the following code in the frameworks/base/core/java/android/content/Context.java
```java
public static final String PHISHING_DETECTOR = "phishing_detector";
```

# Config the Android framework build
There are two options to make AOSP using permissive mode, you can choose one of the two options to proceed.
We highly recommend to use option 1.

1. Option 1 - User permissive mode for SElinux 
- We explain based on Android 8.1 Pixel 2 device. If you are using a different environment, use option 2.
- Add follow line to device/google/wahoo/BoardConfig.mk
```
androidboot.selinux=permissive
```
- Add follow line to device/google/wahoo/BoardConfig.mk
```
androidboot.selinux=permissive
BOARD_KERNEL_CMDLINE += androidboot.selinux=permissive
```
- Add follow line to system/core/rootdir/init.rc below the line "on init"
```
setenforce 0
```
- After modifying the command line, perform make clean, then make bootimage, and flash the new boot image.
- Check your permissive mode on the device using "adb shell getenforce", after build the AOSP


2. Option 2 - Add system service in the Android build configuration
- Add Detector as a system service in the Android build process
- Refer the links to make new system policy: https://source.android.com/security/selinux/implement, https://source.android.com/security/selinux/customize#android-o, https://source.android.google.cn/security/selinux/device-policy?hl=ko
- Detector system service name: phishing_detector
