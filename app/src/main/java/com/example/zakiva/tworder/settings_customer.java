package com.example.zakiva.tworder;

import android.content.Intent;
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
        ShareLinkContent content = new ShareLinkContent.Builder()
                .setContentUrl(Uri.parse("https://www.dropbox.com/s/2nw5w56ugsindo3/2Order.apk?dl=0"))
                .setContentTitle("I am using 2Order app, and I am loving it!")
                .build();
        //ShareButton shareButton = (ShareButton)findViewById(R.id.button14);
        //shareButton.setShareContent(content);
        ShareDialog.show(this, content);
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

    public void back_from_more(View view) {
        Intent i = new Intent(this, customer_orders.class);
        startActivity(i);
    }
}
