package com.example.zakiva.tworder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.parse.ParseUser;
import com.parse.SaveCallback;

public class settings_business extends AppCompatActivity {

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
    }

    public void changeClick(View view)
    {
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
