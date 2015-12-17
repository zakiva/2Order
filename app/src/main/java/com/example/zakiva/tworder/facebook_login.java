package com.example.zakiva.tworder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class facebook_login extends AppCompatActivity {

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook_login);
        final List<String> permissions = new ArrayList<String>();
        ParseFacebookUtils.logInWithReadPermissionsInBackground(this, permissions, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException err) {
                if (user == null) {
                    Log.d("MyApp", "Uh oh. The user cancelled the Facebook login.");
                } else if (user.isNew()) {
                    Log.d("MyApp", "User signed up and logged in through Facebook!");
                } else {
                    Log.d("MyApp", "User logged in through Facebook!");
                    Intent i = new Intent(getBaseContext(), customer_orders.class);
                    startActivity(i);
                }
            }
        });


    }

    public void onOkClick(View view){
        final EditText phoneInput = (EditText) findViewById(R.id.editText2);
        String phone = phoneInput.getText().toString();
        ParseUser user = ParseUser.getCurrentUser();
        user.put("phone", phone);
        user.put("kind", "customer");
        user.put("wants_notification", "yes");
        user.saveInBackground();
        Intent i = new Intent(this, customer_orders.class);
        startActivity(i);

    }
}
