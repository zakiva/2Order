package com.example.zakiva.tworder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.parse.ParseUser;
import com.parse.SaveCallback;

public class settings_business extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_business);

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
}
