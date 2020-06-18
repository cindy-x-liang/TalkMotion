# TalkMotion - The Gesture Recognition Android App
A simple Android app that allows the user to speak by making gestures with their phone.

## Installation Guide
1. You must have an Android device.
2. Follow this link --> https://play.google.com/store/apps/details?id=com.jkotheimer.TalkMotion
3. Install and run!

## User's Guide
1. The app will open on the main screen in the off mode. if you want the app to say anything do a test by holding the phone upright, turning the off button to on, and moving the phone left to right.
2. If you want to see the options for movements, click the settings button in the top left corner of the main screen and then click the gesture button.
3. Feel free to customize what each movement says by tapping on the current phrase next to each animation, making your change, and hitting save changes in the bottom of the screen.

## Developer's Guide
1. Download and use Android Studio to edit the code.
2. Code specs to take note of
  - MainActivity.java
    * DatabaseHelper initialized
    * StateMachine and TransitionHandler initialized with the DatabaseHelper
    * Linear-Accelerometer and gravity sensors initialized
      - Each sensor sends an event to onSensorChanged method every few milliseconds
      - Based on the event type, we do different stuff with the data
      - For an accelerometer event, we check if one of the values exeeds a certain threshold to trigger a sound
      - For a gravity sensor event, we check the orientation of the phone and change the state of the state machine accordingly
    * Event handler methods for each button 
      - Turn the event listening on or off
      - Change screens
      - Save settings
  - StateMachine.java
    * Has multiple states initialized
    * Reads input from MainActivity when a gravity sensor event comes through and changes the current state accordingly.
    * Reads input from MainActivity when an accelerometer sensor event comes through and triggers the current state's motion function.
  - State.java
    * interface for each orientation state.
    * each state has different implementations of each motion function. For example, the upright state's x-motion function says "Hello".
  - DatabaseHelper.java
    * Implements the android built in sqlite helper to store data locally on the phone.
    * Reads data from the MainActivity when the user is on the changeGesture page and presses save.
      - Any changes the user made to the text boxes will be saved, and the associated gesture will produce the saved phrase when made.
  - Speaker.java
    * Uses the Android text to speech library to convert the inputted phrase into speech
  - TransitionHandler.java
    * Sets up all of the UI stuff when changing between screens
  - GifWebview.java
    * Implements Android webView library, but loads a gif for the page that allows the user to change the phrase for any given gesture.
3. No further add-ons needed.
