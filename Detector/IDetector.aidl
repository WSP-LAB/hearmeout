/* 
** HearMeOut code exists.
** File path: frameworks/base/core/java/android/os/IDetector.aidl
** Code style follows the below:
**     1. https://source.android.com/setup/contribute/code-style
**     2. https://google.github.io/styleguide/javaguide.html
** HearMeOut Detector Android Interface Definition Language (AIDL).
** It defines the programming interface of HearMeOut Detector.
** This file contains definitions of functions which are exposed by 
** Android system service.
*/

package android.os;

/** @hide */

interface IDetector {
    // Records the outgoing call information when an outgoing call Intent action
    // starts in a ActivityThread.handleReceiver().
    boolean outgoingCallChanged(String originalNumber, String packageName, String appName);

    // Records the final outgoing call information when an outgoing call 
    // connection is created in 
    // TelephonyConnectionService.onCreateOutgoingConnection().
    boolean outgoingCallCreated(String finalNumber);

    // Records the view information when the view is displayed on the screen in
    // WindowManagerImpl.addView().
    // View view, ViewGroup.LayoutParams params
    boolean addView(String packageName, String appName, int width, int height);

    // Records context information when voice is played in MediaPlayer.start().
    boolean voicePlay(String packageName, String appName);

    // for testing
    int getData();

    // for testing
    void setData(int val);
}