/*    
 * HearMeOut code exists.
 * File path: frameworks/base/services/java/com/android/server/Detector.java
 * Code style follows the below:
 *     1. https://source.android.com/setup/contribute/code-style
 *     2. https://google.github.io/styleguide/javaguide.html
 */

package com.android.server;

import android.os.IDetector;
import android.os.Message;
import android.os.Looper;
import android.os.Handler;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.Set;

/**
 * Detector is HearMeOut main logic class that is in the Android system service.
 * This class has phishing behavior detection logic and Sender that sends 
 * phishing behavior information to Viewer.
 * In AOSP, you do not use this class directly; instead, you have to use 
 * DetectorManager that is the wrapper class of this service.
 * You should define the interface methods of this class in the Android 
 * Interface Definition Language (AIDL) file (IDetector.aidl).
 */
public class Detector extends IDetector.Stub {
    private static final String TAG = "HearMeOut - Detector";

    private MyWorkerThread mWorker;
    private MyWorkerHandler mHandler;
    private Context mContext;
    private int mValue;
    private String originalCallNumber;
    private long redirectionStartTime;
    private boolean redirectionStarted = false;
    
    // Record recent call redirection info
    private long callRedirectionTime;
    private String callRedirectionPackage;
    private String callRedirectionAppName;

    public int myTime;

    public Detector(Context context) {
        super();
        mContext = context;
        mWorker = new MyWorkerThread("DetectorWorker");
        mWorker.start();
        
        Log.i(TAG, "HearMeOut-Detector, start detector class");
        mValue = -1;

        originalCallNumber = null;
        callRedirectionTime = -1;
        redirectionStartTime = -1;
        callRedirectionPackage = null;
        callRedirectionAppName = null;

        myTime = (int)(System.currentTimeMillis() % 1000000);
        setData(myTime);
        Log.i(TAG, "HearMeOut-Detector, set data as myTime: " + myTime);
    }

    /*public void setData(int val) {
        System.out.println("HearMeOut-Detector, setData " + val);
        Message msg = Message.obtain();
        msg.what = MyWorkerHandler.MESSAGE_SET;
        msg.arg1 = val;
        mHandler.sendMessage(msg);
        mValue = val;
    }*/

    public void setData(int val) {    
        mValue = val;
    }

    public int getData() {
        Log.i(TAG, "HearMeOut-Detector, getData: " + mValue);
        return mValue;
    }

    public boolean outgoingCallChanged(String originalNumber, String packageName,
            String appName) {
        if (originalNumber == null || packageName == null || appName == null) {
            Log.e(TAG, "null input parameters");
            return false;
        }

        if(redirectionStarted == false) {
            originalCallNumber = originalNumber;
            callRedirectionPackage = packageName;
            callRedirectionAppName = appName;
            redirectionStartTime = getCurrentTime();
            redirectionStarted = true;
        }

        return true;
    }

    public boolean outgoingCallCreated(String finalNumber) {
        Log.i(TAG, "outgoingCallCreated: " + finalNumber + ", " + originalCallNumber);
        redirectionStarted = false;
        if (finalNumber == null) {
            Log.e(TAG, "null finalNumber");
            return false;
        }

        if (originalCallNumber != null && isPhishingCallRedirection(originalCallNumber, finalNumber)) {
            // if the outgoing call is phishing
            Log.i(TAG, "outgoingCallCreated - phishing detected: " + "finalNumber: " + finalNumber + ", originalNumber: " + originalCallNumber + ", "  + callRedirectionPackage + ", " + callRedirectionAppName);
            callRedirectionTime = getCurrentTime();

            sender("phishing call redirection detected", callRedirectionPackage, callRedirectionAppName);
            return true;
        } else {
            Log.i(TAG, "outgoingCallCreated - phishing not detected: " + "finalNumber: " + finalNumber + ", originalNumber: " + originalCallNumber + ", "  + callRedirectionPackage + ", " + callRedirectionAppName);
            return false;
        }
    }

    /**  
     * This function compares the changes in the original outgoing number and the 
     * final outgoing number to determine whether the call redirection is phishing.
     * The rules for determining voice phishing call redirection may differ 
     * depending on the telephone number system of each country.
     * Since we do not know the country of the artifact evaluation evaluator, 
     * we use a basic comparision rule.
     */
    private boolean isPhishingCallRedirection(String originalNumber, 
            String finalNumber) {
        if (originalNumber == null || finalNumber == null) {
            Log.e(TAG, "Null in input call number");
            return false;
        }
        Log.i(TAG, "isPhishingCallRedirection: " + originalNumber + ", " + finalNumber);

        if (finalNumber.length() > 0 && originalNumber.length() > 0 &&
                finalNumber.endsWith(originalNumber)) {
            Log.i(TAG, "isPhishingCallREdirection: is not phishing redirection");
            return false;
        }
        Log.i(TAG, "isPhishingCallREdirection: is phishing redirection");
        return true;
    }

    /**
     * This function checks whether the added view is a fake call screen used
     * for voice phishing. The detection logic of each mobile phone may differ 
     * depending on the size of the display screen. For smooth artifact 
     * evaluation, we apply a simple rule.
     */

    public boolean addView(String packageName, String appName, int width, int height) {
        Log.i(TAG, "call add view");
        Log.i(TAG, "addView: " + packageName + ", " + appName + ", " + width + ", " + height);
        if (packageName == null || appName == null) {
            Log.e(TAG, "null input parameters");
            return false;
        }

        long currentTime = getCurrentTime();
        Log.i(TAG, "callRedirection data :" +  callRedirectionPackage + ", " + callRedirectionAppName + ", " + callRedirectionTime);
        if (//width >= 900 && height >= 520 && 
                packageName.equals(callRedirectionPackage) && 
                appName.equals(callRedirectionAppName) && 
                (currentTime - callRedirectionTime) <= 2000) {
            Log.i(TAG, "addView: fake phishing view detected: " + packageName + ", " + appName);            
            sender("fake call screen detected", packageName, appName);
            return true;
        }
        return false;
    }

    public boolean voicePlay(String packageName, String appName) {
        Log.i(TAG, "voicePlay called:" +  packageName + ", " + appName);
        if (packageName == null || appName == null) {
            Log.e(TAG, "null input parameters");
            return false;
        }


        long currentTime = getCurrentTime();
        if ((currentTime - callRedirectionTime) <= 2000 && 
                callRedirectionPackage.equals(packageName) && 
                appName.equals(callRedirectionAppName)) {
            Log.i(TAG, "voicePlay: fake phishing voice play detected: " + packageName + ", " + appName);            
            sender("fake call voice detected", packageName, appName);
            return true;
        }
        return false;
    }

    private boolean sender(String type, String packageName, String appName) {
        if(type == null || packageName == null || appName == null) {
            Log.e(TAG, "null input parameters");
            return false;
        }

        Log.i(TAG, "Send Intent: " + type + ", " + packageName + ", " + 
                appName);
        
        Intent intent = new Intent("android.intent.action.phishing");
        intent.putExtra("type", type);
        intent.putExtra("app name", appName);
        intent.putExtra("package name", packageName);
        intent.putExtra("time", getCurrentTime());
        
        mContext.sendBroadcast(intent);
        return true;
    }

    private long getCurrentTime() {
        return System.currentTimeMillis();
    }

    private long getElapsedTime(long startTime) {
        return System.currentTimeMillis() - startTime;
    }

    private class CallInfo {
        public String number;
        public long time;
        public String packageName;
        public String appName;

        public CallInfo(String number, long time, String packageName, 
                String appName) {
            this.number = number;
            this.time = time;
            this.packageName = packageName;
            this.appName = appName;
        }
    }

    // for testing
    private class MyWorkerThread extends Thread {
        public MyWorkerThread(String name) {
            super(name);
        }
        public void run() {
            Looper.prepare();
            mHandler = new MyWorkerHandler();
            Looper.loop();
        }
    }
    // for testing
    private class MyWorkerHandler extends Handler {
        private static final int MESSAGE_SET = 0;
        @Override
        public void handleMessage(Message msg) {
            try {
                if (msg.what == MESSAGE_SET) {
                    System.out.println("HearMeOut-Detector, set message received:" + msg.arg1);
                }
            } catch (Exception e) {
                // Log, don't crash!
                System.out.println("HearMeOut-Detector, Exception in handleMessage");
            }
        }
    }
}
