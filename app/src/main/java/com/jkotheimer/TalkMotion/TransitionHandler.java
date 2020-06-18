package com.jkotheimer.TalkMotion;

import android.os.Build;
import androidx.annotation.RequiresApi;
import android.util.TypedValue;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.jkotheimer.TalkMotion.Database.DatabaseHelper;

import org.w3c.dom.Text;

import java.util.ArrayList;

import static android.text.InputType.TYPE_CLASS_TEXT;

class TransitionHandler {
    
    TransitionHandler(MainActivity a, DatabaseHelper db) {
        this.a = a;
        this.db = db;
    }
    private final MainActivity a;
    private final DatabaseHelper db;
    
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    void changeGesture(Boolean restoreToDefault) {

        // Change the view over to the gesture update page
        a.setContentView(R.layout.update_gestures);

        // Gather the static array containing all of the gesture names
        String[] gestureNames = db.getNames(a.getNameChoices());
        String[] gests = db.getGestures(a.getNameChoices());
        if (restoreToDefault) {
            gestureNames = a.getResources().getStringArray(R.array.names);
            gests = a.getResources().getStringArray(R.array.gestures);
        }


        // Get the container in which we will be placing all of the children
        LinearLayout LL = a.findViewById(R.id.LL);

        // create a new container to hold the text box and the picture in horizontally
        LinearLayout container = new LinearLayout(a);
        container.setOrientation(LinearLayout.HORIZONTAL);

        // Create a text input element and an image element for each gesture GUI element
        EditText et;
        GifWebView gv;

        // create a layout parameter to dynamically change the margin with each addition of an element
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        int margin = 0;

        // Iterate through each gesture name, create and append a new GUI child onto the parent
        for (int i = 0; i < gestureNames.length; i++) {
            // dynamically create the new text and image elements
            lp.setMargins(0,margin,0,0);
            et = new EditText(a);
            et.setText(gestureNames[i]);
            et.setWidth(650);
            et.setHeight(150);
            et.setTextSize(TypedValue.COMPLEX_UNIT_PX, 60);
            et.setInputType(TYPE_CLASS_TEXT);
            et.setLayoutParams(lp);


            lp.setMargins(10, margin,0,0);
            gv = new GifWebView(a, "file:///android_asset/" + gests[i] + ".gif");
            gv.setLayoutParams(lp);
            gv.setPadding(-20,0,0,0);

            Spinner spinner = new Spinner(a);
            spinner.setId(i);
            ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(a,
                    android.R.layout.simple_spinner_item, gests);
            adapter.setDropDownViewResource(android.R.layout
                    .simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            //DO not use listener for now, read the user's selection
            //when changes are saved
            //spinner.setOnItemSelectedListener(a);
            spinner.setLayoutParams(lp);
            spinner.setSelection(i);

            // Add our newly created elements onto the linear layout parent then add that to
            // the xml layout
            container.addView(et);
            //container.addView(gv);

            container.addView(spinner);
            LL.addView(container);

            // re-instantiate the elements to be iterated once more
            container = new LinearLayout(a);

            margin += 5;
        }
    }

    void saveGesture() {
        LinearLayout ll = a.findViewById(R.id.LL);
        ArrayList<EditText> editTexts = new ArrayList<>();
        ArrayList<String> spinners = new ArrayList<>();
        for( int i = 0; i < ll.getChildCount(); i++ ) {
            if (ll.getChildAt(i) instanceof LinearLayout){
                LinearLayout child = (LinearLayout) ll.getChildAt(i);
                for( int j = 0; j < child.getChildCount(); j++) {
                    if(child.getChildAt(j) instanceof EditText) {
                        editTexts.add((EditText) child.getChildAt(j));
                    } else if (child.getChildAt(j) instanceof  Spinner) {
                        Spinner spn = (Spinner) child.getChildAt(j);
                        //System.out.println(" gesture mapped is " + spn.getSelectedItem());
                        spinners.add(spn.getSelectedItem().toString());
                    }
                }
            }
        }

        String[] names = new String[editTexts.size()];
        String[] gestures = new String[editTexts.size()];
        for(int i = 0; i < editTexts.size(); i++) {
            names[i] = editTexts.get(i).getText().toString();
            gestures[i] = spinners.get(i);
            System.out.println(names[i] +" mapped to " + gestures[i]);
        }
        db.insertNames(names, gestures, a.getNameChoices());
        a.setContentView(R.layout.settings);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    void changeGestureDef(Boolean restoreToDefault) {

        // Change the view over to the gesture update page
        a.setContentView(R.layout.edit_gesture_definition);

        // Gather the static array containing all of the gesture names

        String[] gests = db.getGestures(a.getNameChoices());
        if (restoreToDefault) {
           gests = a.getResources().getStringArray(R.array.gestures);
        }


        // Get the container in which we will be placing all of the children
        LinearLayout LL = a.findViewById(R.id.LL1);

        // create a new container to hold the text box and the picture in horizontally
        LinearLayout container = new LinearLayout(a);
        container.setOrientation(LinearLayout.HORIZONTAL);

        // Create a text input element and an image element for each gesture GUI element
        EditText et;
        GifWebView gv;
        TextView label;

        // create a layout parameter to dynamically change the margin with each addition of an element
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        int margin = 0;

        // Iterate through each gesture name, create and append a new GUI child onto the parent
        for (int i = 0; i < gests.length; i++) {
            // dynamically create the new text and image elements
            lp.setMargins(0,margin,0,0);
            et = new EditText(a);
            et.setText(gests[i]);
            et.setWidth(650);
            et.setHeight(150);
            et.setTextSize(TypedValue.COMPLEX_UNIT_PX, 60);
            et.setInputType(TYPE_CLASS_TEXT);
            et.setLayoutParams(lp);


            lp.setMargins(10, margin,0,0);
            gv = new GifWebView(a, "file:///android_asset/" + i + ".gif");
            gv.setLayoutParams(lp);
            gv.setPadding(-20,0,0,0);

            lp.setMargins(10, margin,0,0);

            label = new TextView(a);
            label.setText(gests[i]);
            label.setVisibility(View.INVISIBLE);


            // Add our newly created elements onto the linear layout parent then add that to
            // the xml layout
            container.addView(et);
            container.addView(gv);

            container.addView(label);
            LL.addView(container);

            // re-instantiate the elements to be iterated once more
            container = new LinearLayout(a);

            margin += 5;
        }
    }

    void saveGestureDef() {
        LinearLayout ll = a.findViewById(R.id.LL1);
        ArrayList<EditText> editTexts = new ArrayList<>();
        ArrayList<String> nonEditTexts = new ArrayList<>();
        for( int i = 0; i < ll.getChildCount(); i++ ) {
            if (ll.getChildAt(i) instanceof LinearLayout){
                LinearLayout child = (LinearLayout) ll.getChildAt(i);
                for( int j = 0; j < child.getChildCount(); j++) {
                    if(child.getChildAt(j) instanceof EditText) {
                        editTexts.add((EditText) child.getChildAt(j));
                    } else if (child.getChildAt(j) instanceof  TextView) {
                        TextView textView = (TextView) child.getChildAt(j);
                        //System.out.println(" gesture mapped is " + spn.getSelectedItem());
                        nonEditTexts.add(textView.getText().toString());
                    }
                }
            }
        }

        String[] new_gest_names = new String[editTexts.size()];
        String[] old_gest_name = new String[editTexts.size()];
        for(int i = 0; i < editTexts.size(); i++) {
            new_gest_names[i] = editTexts.get(i).getText().toString();
            old_gest_name[i] = nonEditTexts.get(i);
            System.out.println(new_gest_names[i] +" mapped to " + old_gest_name[i]);
        }
        //db.insertNames(names, gestures, a.getNameChoices());
        db.updateGestureIds(new_gest_names, old_gest_name, 0);
        a.setContentView(R.layout.settings);
    }


    // Starting the display code
    void changeLogo(View v) {
        a.setContentView(R.layout.logo);
    }

    void changeSettings(View v) {
        a.setContentView(R.layout.settings);
    }

    void changeMain(View v) {
        a.setContentView(R.layout.activity_main);
    }
}
