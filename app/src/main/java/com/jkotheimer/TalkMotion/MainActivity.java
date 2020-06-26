package com.jkotheimer.TalkMotion;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import android.widget.ListView;
import android.widget.ScrollView;



import com.jkotheimer.TalkMotion.Database.DatabaseHelper;
import com.jkotheimer.TalkMotion.StateMachine.StateMachine;

import static android.text.InputType.TYPE_CLASS_TEXT;


public class MainActivity extends AppCompatActivity implements SensorEventListener, AdapterView.OnItemSelectedListener {


    // A state machine to call each of the motion functions
    private StateMachine sm;

    // A transition handler to deal with the complexities of switching between pages
    // and dynamically adding, subtracting, or saving values
    private TransitionHandler transition;

    // isOn toggles whether the user wants to read their data currently or not.
    // Triggered by a button push
    private boolean isOn = false;





    // Boolean values that stop it from repeating itself
    private boolean wasX = false;
    private boolean wasY = false;
    private boolean wasZ = false;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Typical android setup stuff
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        // Instantiate the StateMachine and the Transition handler
        DatabaseHelper db = new DatabaseHelper(this);
        sm = new StateMachine(this, db);
        transition = new TransitionHandler(this, db);

        // Instantiate and register each sensor variable to each of it's
        // corresponding hardware sensors
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor linear_acceleration = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        Sensor gravity = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        Sensor gyro = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        sensorManager.registerListener(MainActivity.this, linear_acceleration, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(MainActivity.this, gravity, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(MainActivity.this, gyro, SensorManager.SENSOR_DELAY_NORMAL);







        }

    /**
     * This function needs to be here to prevent the super method from doing something weird
     */
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    /**
     * onSensorChanged
     *  - Called every time one of the sensors detects an event
     *  - Either an acceleration, gravity, or gyroscope event
     *  - Use the given value to change state or call function
     * @param event - the event object containing the sensor data
     */
    @Override
    public void onSensorChanged(SensorEvent event) {

        // If the sensor event was triggered by the accelerator, call checkMotion to play with it
        if (event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION && isOn) {
            checkMotion(event.values[0], event.values[1], event.values[2]);
        } // Else, if it was a gravity event, check for the orientation
        // and change the state accordingly
        else if (event.sensor.getType() == Sensor.TYPE_GRAVITY) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            if(z > 8.0 && z > y && z > x) {
                sm.toBack();
            }
            else if(z < -8.0 && z < y && z < x) {
                sm.toFront();
            }
            else if(y > 8.0 && y > z && y > z) {
                sm.toUpright();
            }
        } // Else if the sensor was a gyroscope reading, change the isOn variable accordingly
        /*if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            if(x > 1 || z > 1 || y > 1 || x < -1 || z < -1 || y < -1) {
                isOn = false;
            } else if(ax < 3 || ax > - 3.0 && az < 3 || az > - 3.0 && ay < 3 || ay > - 3.0) {
                isOn = true;
            }
        }*/
    }

    /**
     * Toggles the on/off button which determines if the user wants to be talking currently
     * @param v - the view of the event call from the button press
     */
    public void toggleOnOff(View v) {
        Button b = findViewById(R.id.OnOff);
        if(isOn) {
            isOn = false;
            wasZ = false;
            wasY = false;
            wasX = false;
            b.setText(R.string.off);
        }
        else {
            isOn = true;
            b.setText(R.string.on);
        }
    }


    // Starting the display code
    // public void changeLogo(View v) { transition.changeLogo(v) }

    public void changeSettings(View v) {
        transition.changeSettings(v);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void changeGesture(View v)
    {
        transition.changeGesture(false);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void changeGestureDef(View v)
    {
        transition.changeGestureDef(false);
    }


    public void changeMain(View v) {
        transition.changeMain(v);
        Button b = findViewById(R.id.OnOff);
        if(!isOn) b.setText(R.string.off);
    }

    public void saveGes(View v) {
        transition.saveGesture();
    }

    public void saveGesDef(View v) {
        transition.saveGestureDef();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void restoreToDefaultSettings(View v) {
        transition.changeGesture(true);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void restoreToDefaultGestureDef(View v) {
        transition.changeGestureDef(true);
    }
    /**
     * Check the motion of each axis when there is an acceleration event
     * If there is extraneous x movement, call x_move on the state machine
     * So on an so forth for each other axes.
     */
    public void checkMotion (final float ax, final float ay, final float az)
    {
        if ((ax > 5 || ax < -5) && !(sm.isPlaying()) && ((ax > ay) && (ax > az) || (ax < ay) && (ax <az)) && !wasX) {
            sm.x_move();
            wasX = true;
            wasY = false;
            wasZ = false;
        }
        else if ((az > 5 || az < -5) && !(sm.isPlaying()) && ((az > ay) && (az > ax) || (az < ay) && (az <ax)) && !wasZ) {
            sm.z_move();
            wasZ = true;
            wasY = false;
            wasX = false;
        }
        else if ((ay > 5 || ay < -5) && !(sm.isPlaying()) && ((ay > ax) && (ay > az) || (ay < ax) && (ay < az)) && !wasY) {
            sm.y_move();
            wasY = true;
            wasX = false;
            wasZ = false;
        }
    }

    /**
     * When the app is paused, stop all functionality so the beeper doesn't make any random noises
     * in the background
     */
    @Override
    public void onPause(){
        super.onPause();
        super.onStop();
        finish();
        isOn = false;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)

        Toast.makeText(parent.getContext(),
                "OnItemSelectedListener : " + parent.getItemAtPosition(pos).toString() + " position: " + pos + " id is : " + id,
                Toast.LENGTH_SHORT).show();
        System.out.println("OnItemSelectedListener : " + parent.getItemAtPosition(pos).toString() + " position: " + pos);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }
}
