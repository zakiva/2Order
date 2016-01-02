package com.example.zakiva.tworder;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.parse.ParseUser;
import com.parse.SaveCallback;

public class settings_business extends AppCompatActivity {

    int edit = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_business);

        TextView tv = (TextView) findViewById(R.id.textView11);
        int num = ParseUser.getCurrentUser().getInt("days_alert");
        tv.setText(Integer.toString(num) + " days");

        Switch mySwitch = (Switch) findViewById(R.id.switch2);
        if (ParseUser.getCurrentUser().getString("Auto_orders_numbers").equals("yes")){
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
        ParseUser user = ParseUser.getCurrentUser();
        int days = user.getInt("days_alert");
        EditText e = (EditText) findViewById(R.id.editText3);
        e.setText(String.format("%d", days));
    }

    public void changeClick(View view)
    {
        Button b = (Button) findViewById(R.id.button8);
        EditText e = (EditText) findViewById(R.id.editText3);

        if (edit == 0) {
            b.setBackgroundResource(R.drawable.done);
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
            edit = 1;
            e.setEnabled(true);
        } else {
            edit_done();
            e.setEnabled(false);
            ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(view.getWindowToken(), 0);
            b.setBackgroundResource(R.drawable.edit);
            edit = 0;
        }
    }

    public void edit_done(){
        try {
            EditText new_num = (EditText) findViewById(R.id.editText3);
            String num = new_num.getText().toString();
            ParseUser user = ParseUser.getCurrentUser();
            user.put("days_alert", Integer.parseInt(num));
            user.saveInBackground();
            TextView tv = (TextView) findViewById(R.id.textView11);
            tv.setText(num + " days");
        }
        catch (NumberFormatException n) {
            // ... Do nothing.
        }

    }
}
