/*    
 * HearMeOut code exists.
 * File path: frameworks/base/core/java/android/os/DetectorManager.java
 * Code style follows the below:
 *     1. https://source.android.com/setup/contribute/code-style
 *     2. https://google.github.io/styleguide/javaguide.html
 */

package android.os;

import android.os.IDetector;
import android.os.IBinder;
import android.os.ServiceManager;
import android.content.Context;
import android.util.Log;

/**
 * DetectorManager is a wrapper class of Detector that is the Android system
 * service. DetectorManager holds Singleton class of Detector. DetectorManager
 * also provide interface methods of Detector.
 * In AOSP, you do not instantiate this class directly; instead, you have to
 * retrieve a reference to an instance through 
 * {@link Context#getSystemService Context.getSystemService(Context.PHISHING_DETECTOR)}.
 */
public class DetectorManager {
    private static final String TAG = "HearMeOut - DetectorManager";

    private static DetectorManager sDetectorManager;
    private IDetector mDetector;
    
    DetectorManager(IDetector detector) {
        sDetectorManager = null;
        if (detector == null) {
            Log.e(TAG, "Input parameter (IDetector) is null");
        }
        Log.i(TAG, "DetectorManager init");
        mDetector = detector;
    }

    /*
     * Add comments
     */
    public static synchronized DetectorManager getDetectorManager() {
        if (sDetectorManager == null) {
            IBinder binder = ServiceManager.getService(Context.PHISHING_DETECTOR);
            if (binder != null) {
                IDetector managerService = IDetector.Stub.asInterface(binder);
                sDetectorManager = new DetectorManager(managerService);
                Log.i(TAG, "Get Detector service");
            } else {
                Log.e(TAG, "Can't find Detector service");
                return null;
            }
        }
        return sDetectorManager;
    }

    // for testing
    public void setData(int arg) {
          try {
              System.out.println("Hear-kjkpoi-proxy, Going to call service from framework proxy");
              mDetector.setData(arg);
              System.out.println("Hear-kjkpoi-proxy, Service called successfully from framework proxy");
          } catch (Exception e) {
              System.out.println("Hear-kjkpoi-proxy, FAILED to call service from framework proxy");
             e.printStackTrace();
          }
      }
  
    // for testing  
    public int getData() {
        try {
            System.out.println("Hear-kjkpoi-proxy, Going to call getdata from framework proxy");
            return mDetector.getData();
        } catch (Exception e) {
            System.out.println("Hear-kjkpoi-proxy, FAILED to call getdata service from framework proxy");
            e.printStackTrace();
        }
      return -1;
    }

    /** 
     * When an outgoing call number is changed, the original outgoing call information is 
     * recorded in a call receiver thread.
     */ 
    public boolean outgoingCallChanged(String originalNumber, String packageName,
            String appName) {
        try {
            return mDetector.outgoingCallChanged(originalNumber, packageName, 
                    appName);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return false;
    }

    /*
     * When an outgoing call created, the final outgoing call information is 
     * recorded in the TelephonyConnectionService.onCreateOutgoingConnection().
     */
    public boolean outgoingCallCreated(String finalNumber) {
        try {
            return mDetector.outgoingCallCreated(finalNumber);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return false;
    }

    /*
     * Records the view information when the view is displayed on the screen in
     * the WidnowManagerImpl.
     */
    public boolean addView(String packageName, String appName, int width, int height) {
        try {
            return mDetector.addView(packageName, appName, width, height);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return false;
    }

    /**
     * Records context information when voice is played in the MediaPlayer.
     */
    public boolean voicePlay(String packageName, String appName) {
        try {
            return mDetector.voicePlay(packageName, appName);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return false;
    }
}