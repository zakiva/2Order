package com.example.zakiva.tworder;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.RefreshCallback;
import com.parse.SaveCallback;

import java.util.List;

public class settings_business extends AppCompatActivity {

    int edit = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_business);


        TextView tv = (TextView) findViewById(R.id.textView11);
        double num = ParseUser.getCurrentUser().getDouble("days_alert");
        //Log.d("aaa:", Double.toString(num));
        //Float num2 = num.floatValue();
        tv.setText(Double.toString(num) + " days");

        Switch mySwitch = (Switch) findViewById(R.id.switch2);
        if (ParseUser.getCurrentUser().getString("Auto_orders_numbers").equals("yes")){
            mySwitch.setChecked(true);
        }else{
            mySwitch.setChecked(false);
        }

        Switch mySwitch2 = (Switch) findViewById(R.id.switch3);
        if (ParseUser.getCurrentUser().getString("Send_sms").equals("yes")){
            mySwitch2.setChecked(true);
        }else{
            mySwitch2.setChecked(false);
        }

        double days = ParseUser.getCurrentUser().getDouble("days_alert");
        EditText e2 = (EditText) findViewById(R.id.editText3);
        e2.setText(Double.toString(days));
        e2.setSelection(e2.getText().length());

        ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
        query.whereEqualTo("username", ParseUser.getCurrentUser().getString("username"));
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(final List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    Log.d("aaa:", objects.get(0).getString("Auto_orders_numbers"));
                    TextView tv = (TextView) findViewById(R.id.textView11);
                    double num = objects.get(0).getDouble("days_alert");
                    //Log.d("aaa:", Double.toString(num));
                    //Float num2 = num.floatValue();
                    tv.setText(Double.toString(num) + " days");

                    Switch mySwitch = (Switch) findViewById(R.id.switch2);
                    if (objects.get(0).getString("Auto_orders_numbers").equals("yes")){
                        mySwitch.setChecked(true);
                    }else{
                        mySwitch.setChecked(false);
                    }

                    mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                            if (isChecked) {
                                ParseUser user = ParseUser.getCurrentUser();
                                user.put("Auto_orders_numbers", "yes");
                                user.saveInBackground();
                            } else {
                                ParseUser user = ParseUser.getCurrentUser();
                                user.put("Auto_orders_numbers", "no");
                                user.saveInBackground();
                            }

                        }
                    });

                    Switch mySwitch2 = (Switch) findViewById(R.id.switch3);
                    if (objects.get(0).getString("Send_sms").equals("yes")){
                        mySwitch2.setChecked(true);
                    }else{
                        mySwitch2.setChecked(false);
                    }

                    mySwitch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                            if (isChecked) {
                                ParseUser user = ParseUser.getCurrentUser();
                                user.put("Send_sms", "yes");
                                user.saveInBackground();
                            } else {
                                ParseUser user = ParseUser.getCurrentUser();
                                user.put("Send_sms", "no");
                                user.saveInBackground();
                            }

                        }
                    });

                    double days = objects.get(0).getDouble("days_alert");
                    EditText e2 = (EditText) findViewById(R.id.editText3);
                    e2.setText(Double.toString(days));
                    e2.setSelection(e2.getText().length());


                }}});

    }

    public void changeClick(View view)
    {
        Button b = (Button) findViewById(R.id.button8);
        EditText e = (EditText) findViewById(R.id.editText3);

        if (edit == 0) {
            b.setBackgroundResource(R.drawable.blank_pink);
            b.setText("save");
        //    b.setLayoutParams(new LinearLayout.LayoutParams(30, 50));
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
            edit = 1;
            e.setEnabled(true);
        } else {
            edit_done();
            e.setEnabled(false);
            ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(view.getWindowToken(), 0);
            b.setBackgroundResource(R.drawable.edit);
            b.setText("");
         //   b.setLayoutParams(new LinearLayout.LayoutParams(30, 30));
            edit = 0;
        }
    }

    public void alertToast(String alert) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.my_custom_alert,
                (ViewGroup) findViewById(R.id.my_custom_layout_id));
        TextView text = (TextView) layout.findViewById(R.id.alertText);

        text.setText(alert);
        Toast toast2 = new Toast(getApplicationContext());
        toast2.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
        toast2.setDuration(Toast.LENGTH_LONG);
        toast2.setView(layout);
        toast2.show();
    }

    public void edit_done(){
        try {
            EditText new_num = (EditText) findViewById(R.id.editText3);
            final String num = new_num.getText().toString();
            ParseUser user = ParseUser.getCurrentUser();
            user.put("days_alert", Double.parseDouble(num));
            user.saveInBackground(new SaveCallback() {
                public void done(ParseException e) {
                    TextView tv = (TextView) findViewById(R.id.textView11);
                    tv.setText(num + " days");
                }
            });
        }
            catch(NumberFormatException n)
                {
                    alertToast("Please enter a valid days number");
                }

            }

    public void back_from_settings(View view) {
        Intent i = new Intent(this, business_orders__screen.class);
        startActivity(i);
    }
}
