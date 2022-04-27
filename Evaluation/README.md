# HearMeOut Evaluation method
We will explain how HearMeOut will be evaluated.

# Evaluation process
1. Install the given voice phishing app (phishing_app.apk). You have to accept installation time permissions.
2. Make an outgoing call
3. Check HearMeOut Detection result using ADB log or given Phishing monitor app (phishing_app.apk)

# Voice phishing app
As stated in the paper, it is very difficult to find a live phishing app, and the operating period of a live phishing app is very short.
Therefore, we use the app that implements the function of the phishing app for the experiment.
"phishing_app.apk" has implemented almost the same UI and functions as the phishing app.

This app has the following voice phishing function.
- Call redirection: When you make an outgoing call, the phishing app redirects the outgoing call to "01048572742". I made a UI that can set the call redirection target number on the main screen of the app.
- Fake screen: Displays fake call screen when redirected outgoing call occurs. The fake screen should display the UI suitable for each phone model, but the function is implemented briefly. The background or window size of the fake call screen may be unnatural.
- Fake voice: Play fake call voice when redirected outgoing call occurs. Fake voice plays one of the voice files included in the real phishing app.
Install "phishing_monitor.apk" app on HearMeOut installed smartphone.

# HearMeOut result verification
You can check the voice phishing behavior detection result of HearMeOut through the ADB log.
Also, for the convenience of evaluation, we provide a voice phishing behavior detection monitor app.
If HearMeOut detects phishing behavior when the monitor app is running, the detection result of the monitor app is displayed.
Install "phishing_monitor.apk" app on HearMeOut installed smartphone.