package com.example.zakiva.tworder;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.LikeView;
import com.facebook.share.widget.ShareButton;
import com.facebook.share.widget.ShareDialog;
import com.parse.ParseUser;

public class settings_customer extends AppCompatActivity {
    void notification_on(){
        ParseUser user = ParseUser.getCurrentUser();
        user.put("wants_notification", "yes");
        user.saveInBackground();
    }

    void notification_off(){
        ParseUser user = ParseUser.getCurrentUser();
        user.put("wants_notification", "no");
        user.saveInBackground();
    }

    public void shareOnFacebookClicked(View view){
        
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_customer);


        Switch mySwitch = (Switch) findViewById(R.id.switch1);
        if (ParseUser.getCurrentUser().getString("wants_notification").equals("yes")){
            mySwitch.setChecked(true);
        }else{
            mySwitch.setChecked(false);
        }

        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    notification_on();
                }else{
                    notification_off();
                }

            }
        });

    }
}
